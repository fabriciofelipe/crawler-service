package application.application.service;

import application.domain.Content;
import application.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.stereotype.Service;
import application.domain.Feed;
import application.domain.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static application.application.util.HtmlConstants.*;


/**
 * Classe de serviço com crawler para ler feeds
 * Autor Fabricio Carvalho
 */
@Service
@Slf4j
public class CrawlerService {


    public static final String TEXT_TYPE = "text";
    public static final String IMAGE_TYPE = "image";

    /**
     * Metodo para construir crowler a partir de uma determinada Url
     * @param url
     * @return
     */
    public Optional<Page> getFeed(String url) {

        List<Feed> feeds = new ArrayList<>();
        Feed feed = Feed.builder().build();
        List<Item> items = new ArrayList<>();
        try {
            Element doc = Jsoup.connect(url).get();
            doc.select(ITEM_TAG).forEach( it -> {
                items.add(Item.builder()
                        .title(it.select(TITLE_TAG).text())
                        .link(it.select(LINK_TAG).text())
                        .content(builderContent(it.select(DESCRIPTION_TAG).text()))
                        .build());
            });
            feed.setItem(items);
        } catch (IOException e) {
            log.error("Erro desserializar Url: {}", url);
        }
        feeds.add(feed);
        Optional<Page> page =  Optional.ofNullable(Page.builder().feed(feeds).build());
        return page;
    }

    /**
     * Metodo de construção do Bloco Content
     * @param type
     * @return
     */
    private List<Content> builderContent(String type) {
        Document doc = Jsoup.parse(type).normalise();
        List<Content> contents = new ArrayList<>();
        //percorre html gerado a partir da tag description
        doc.select(BODY_TAG).forEach( desc -> {
            //percorre tags filhas de body
            desc.childNodes().forEach(bodyChild -> {
                //percorre tags filhas das filhas de body
                bodyChild.childNodes().forEach(parentBody -> {

                    builderContentTagImg(contents, bodyChild, parentBody);

                    builderContentTagUl(contents, bodyChild, parentBody);

                    builderContentTagP(contents, bodyChild, parentBody);
                });
            });
        });

        return contents;
    }

    /**
     * Metodo de construção do bloco do tipo 'text'
     * @param contents
     * @param bodyChild
     * @param parentBody
     */
    private void builderContentTagP(List<Content> contents, Node bodyChild, Node parentBody) {
        if(bodyChild.nodeName().equals(P_TAG) && !parentBody.toString().equals(" &nbsp;") && !"".equals(parentBody.toString().trim())){

            List<String> value = new ArrayList<>();
            Content content = Content.builder().type(TEXT_TYPE).build();
            if(parentBody instanceof Element){
                value.add(((Element) parentBody).select(((Element) parentBody).tagName()).text());
            } else {
                value.add(parentBody.toString().replaceAll("&nbsp;", "").trim());
            }
            content.setContent(value);
            contents.add(content);
        }
    }

    /**
     * Metodo de construção do bloco do tipo 'image' com várias ocorrências
     * @param contents
     * @param bodyChild
     * @param parentBody
     */
    private void builderContentTagUl(List<Content> contents, Node bodyChild, Node parentBody) {
        if (bodyChild.nodeName().equals(DIV_TAG) && parentBody.nodeName().equals(UL_TAG)){
            List<String> href = new ArrayList<>();
            Content content = Content.builder().type(IMAGE_TYPE).build();
            parentBody.childNodes().forEach(childParentBody -> {
                if(childParentBody instanceof Element) {
                    href.add(((Element) childParentBody).children().attr(HREF_ATT));
                }
            });
            content.setContent(href);
            contents.add(content);
        }
    }

    /**
     * Metodo de construção do bloco do tipo 'image' com uma ocorrência
     * @param contents
     * @param bodyChild
     * @param parentBody
     */
    private void builderContentTagImg(List<Content> contents, Node bodyChild, Node parentBody) {
        if (bodyChild.nodeName().equals(DIV_TAG) && parentBody.nodeName().equals(IMG_TAG)){
            List<String> src = new ArrayList<>();
            Content content = Content.builder().type(IMAGE_TYPE).build();
            src.add(parentBody.attr(SRC_ATT));
            content.setContent(src);
            contents.add(content);

        }
    }

}

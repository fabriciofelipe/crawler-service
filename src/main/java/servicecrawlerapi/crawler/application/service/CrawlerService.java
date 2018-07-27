package servicecrawlerapi.crawler.application.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import servicecrawlerapi.crawler.domain.Content;
import servicecrawlerapi.crawler.domain.Feed;
import servicecrawlerapi.crawler.domain.Item;
import servicecrawlerapi.crawler.domain.Page;

import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CrawlerService {


    public Optional<Page> getFeed(String url) {

        List<Feed> feeds = new ArrayList<>();
        Feed feed = Feed.builder().build();
        List<Item> items = new ArrayList<>();
        try {
            Element doc = Jsoup.connect(url).get();
            doc.select("item").forEach( it -> {
                items.add(Item.builder()
                        .title(it.select("title").text())
                        .link(it.select("link").text())
                        .content(builderContent(it.select("description").text()))
                        .build());
            });
            feed.setItem(items);
        } catch (IOException e) {
            log.error("Erro ao desserelizar Url");
        }
        feeds.add(feed);
        Optional<Page> page =  Optional.ofNullable(Page.builder().feed(feeds).build());
        return page;
    }

    private List<Content> builderContent(String type) {
        Document doc = Jsoup.parse(type).normalise();
        List<Content> contents = new ArrayList<>();

        doc.select("body").forEach( desc -> {
            desc.childNodes().forEach(bodyChild -> {
                bodyChild.childNodes().forEach(parentBody -> {
                    if (bodyChild.nodeName().equals("div") && parentBody.nodeName().equals("img")){
                        List<String> src = new ArrayList<>();
                        Content content = Content.builder().type("image").build();
                        src.add(parentBody.attr("src"));
                        content.setContent(src);
                        contents.add(content);
                    }

                    if(bodyChild.nodeName().equals("p") && !parentBody.toString().equals(" &nbsp;")){

                        List<String> src = new ArrayList<>();
                        Content content = Content.builder().type("text").build();
                        if(parentBody instanceof Element){
                            src.add(((Element) parentBody).select(((Element) parentBody).tagName()).text());
                        } else {
                            src.add(parentBody.toString());
                        }
                        content.setContent(src);
                        contents.add(content);
                    }
                });
            });
        });

        return contents;
    }

}

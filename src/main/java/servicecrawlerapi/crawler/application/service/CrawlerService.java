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

import java.io.IOException;
import java.util.ArrayList;
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
                            .content(builderContentImages(it.select("description").text()))
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

  private List<Content> builderContentImages(String type) {
      Document doc = Jsoup.parse(type).normalise();
      List<String> src = new ArrayList<>();
      List<Content> contents = new ArrayList<>();
      Content content = Content.builder().type("image").build();
      doc.select("img").forEach( desc -> {
          src.add(desc.attr("src"));
          content.setContent(src);
      });
      contents.add(content);
      return contents;
  }

}

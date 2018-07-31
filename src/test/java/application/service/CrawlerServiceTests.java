package application.service;

import application.application.service.CrawlerService;
import application.domain.Content;
import application.domain.Item;
import application.domain.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import application.domain.Feed;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerServiceTests {

	@Autowired
	private CrawlerService crawlerService;

	@Test
	public void getFeedSucess() {
		// given
		String url = "https://revistaautoesporte.globo.com/rss/ultimas/feed.xml";

		// when
		Optional<Page> page =crawlerService.getFeed(url);

		// then
		assertThat(!page.get().getFeed().get(0).getItem().isEmpty())
				.isEqualTo(true);
	}

	@Test
	public void contratVerify() {
		// given
		Content contentImage = Content.builder().type("image").content(Arrays.asList("http://image1","http://image2")).build();
		Content contentText = Content.builder().type("text").content(Arrays.asList("text")).build();
		Item item = Item.builder().link("http://link").title("Title").content(Arrays.asList(contentImage,contentText)).build();
		Feed feed = Feed.builder().item(Collections.singletonList(item)).build();

		// when
		Page page = Page.builder().feed(Collections.singletonList(feed)).build();

		// then
		assertThat(page.toString())
				.isEqualTo("Page(feed=[Feed(item=[Item(title=Title, link=http://link, content=[Content(type=image, content=[http://image1, http://image2]), Content(type=text, content=[text])])])])");
	}

	@Test
	public void getFeedException() {
		// given
		String url = "https://httpstat.us/500";

		// when
		Optional<Page> page =crawlerService.getFeed(url);

		// then
		assertThatIOException();
	}

}

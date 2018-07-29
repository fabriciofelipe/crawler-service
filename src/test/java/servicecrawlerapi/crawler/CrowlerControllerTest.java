package servicecrawlerapi.crawler;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import servicecrawlerapi.crawler.application.rest.CrawlerApiController;
import servicecrawlerapi.crawler.application.service.CrawlerService;
import servicecrawlerapi.crawler.domain.Content;
import servicecrawlerapi.crawler.domain.Feed;
import servicecrawlerapi.crawler.domain.Item;
import servicecrawlerapi.crawler.domain.Page;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.of;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CrawlerApiController.class)
public class CrowlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrawlerService crawlerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getFeed_thenStatus200() throws Exception {

        //given
        Map<String, String> body = of("url", "https://revistaautoesporte.globo.com/rss/ultimas/feed.xml");
        final String json = objectMapper.writeValueAsString(body);

        when(crawlerService.getFeed(body.get("url"))).thenReturn(Optional.ofNullable(Page.builder().build()));
        this.mockMvc.perform(post("/crawler")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void getFeed_thenStatus404() throws Exception {

        //given
        Map<String, String> body = of("url", "https://revistaautoesporte.globo.com/rss/ultimas/feed.xml");
        final String json = objectMapper.writeValueAsString(body);

        when(crawlerService.getFeed(body.get("url"))).thenReturn(Optional.empty());
        this.mockMvc.perform(post("/crawler")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
}

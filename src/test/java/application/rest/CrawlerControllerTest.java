package application.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;
import application.application.rest.CrawlerApiController;
import application.application.service.CrawlerService;
import application.domain.Page;
import application.configuration.SecurityConfig;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        SecurityConfig.class, CrawlerApiController.class})
@WebMvcTest(CrawlerApiController.class)
public class CrawlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrawlerService crawlerService;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    public void getFeed_thenStatus200() throws Exception {

        //given
        Map<String, String> body = Collections.singletonMap("url", "https://revistaautoesporte.globo.com/rss/ultimas/feed.xml");
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
        Map<String, String> body = Collections.singletonMap("url", "https://revistaautoesporte.globo.com/rss/ultimas/feed.xml");
        final String json = objectMapper.writeValueAsString(body);

        when(crawlerService.getFeed(body.get("url"))).thenReturn(Optional.empty());
        this.mockMvc.perform(post("/crawler")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getFeed_thenStatus401() throws Exception {

        //given
        Map<String, String> body = Collections.singletonMap("url", "https://revistaautoesporte.globo.com/rss/ultimas/feed.xml");
        final String json = objectMapper.writeValueAsString(body);

        when(crawlerService.getFeed(body.get("url"))).thenReturn(Optional.ofNullable(Page.builder().build()));
        this.mockMvc.perform(post("/crawler")
                .content(json)
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("user:secret".getBytes()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

}

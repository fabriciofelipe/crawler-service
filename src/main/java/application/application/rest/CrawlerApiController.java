package application.application.rest;

import application.application.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import application.domain.Page;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class CrawlerApiController {

    private final CrawlerService crawlerService;

    @PostMapping (value = "/crawler",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page> getCrawler(@RequestBody Map<String,String> url) {
        Optional <Page> page =  crawlerService.getFeed(url.get("url"));
        return page.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

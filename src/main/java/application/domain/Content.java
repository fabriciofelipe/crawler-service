package application.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Content {

    private String type;
    private List<String> content;
}

package field.ryan.scraper.data.discogs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Release implements Serializable {

    private String title;
    private String id;
    @JsonProperty("data_quality")
    private String dataQuality;
    private String thumb;
    private List<Identifier> identifiers;
    private List<Artist> artists;
}

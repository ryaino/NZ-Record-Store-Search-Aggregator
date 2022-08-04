package field.ryan.scraper.data;

import lombok.Data;

import java.util.List;

@Data
public class SearchableObject {

    private String title;
    private List<String> artistNames;
    private List<String> barcodes;

}

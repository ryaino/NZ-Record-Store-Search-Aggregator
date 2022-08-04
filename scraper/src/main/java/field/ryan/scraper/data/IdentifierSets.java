package field.ryan.scraper.data;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class IdentifierSets {

    private Set<String> titles;
    private Set<String> artistNames;
    private Set<String> barcodes;

    public Set<String> getCombinedIdentifiers() {
        Set<String> set = new HashSet<>();
        set.addAll(this.titles);
        set.addAll(this.artistNames);
        set.addAll(this.barcodes);
        return set;
    }
}

package field.ryan.scraper.data.hits;

import field.ryan.scraper.data.hits.JustForTheRecordHit;
import field.ryan.scraper.data.hits.RealGroovyHit;
import lombok.Data;

import java.util.Map;

@Data
public class AllHits {

    private Map<String, RealGroovyHit> realGroovyHits;
    private Map<String, JustForTheRecordHit> justForTheRecordHits;
    private Map<String, MarbecksHit> marbeckHits;


}

package field.ryan.scraper.services;

import field.ryan.scraper.data.*;
import field.ryan.scraper.data.discogs.Artist;
import field.ryan.scraper.data.discogs.Identifier;
import field.ryan.scraper.data.discogs.Release;
import field.ryan.scraper.data.hits.AllHits;
import field.ryan.scraper.data.hits.JustForTheRecordHit;
import field.ryan.scraper.data.hits.MarbecksHit;
import field.ryan.scraper.data.hits.RealGroovyHit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class GatewayService {

    private final DiscogsService discogsService;
    private final RealGroovyScraperService realGroovyScraperService;
    private final JustForTheRecordService justForTheRecordService;
    private final MarbecksService marbecksService;

    public AllHits getMatchesFromMaster(String masterId) {
        List<Release> releases = this.discogsService.getReleasesInMaster(masterId);
        List<SearchableObject> searchableObjects = convertToSearchableObjects(releases);
        IdentifierSets sets = removeDuplicateIdentifiers(searchableObjects);
        return scrapeAllSites(sets);
    }

    public IdentifierSets removeDuplicateIdentifiers(List<SearchableObject> results) {
        IdentifierSets sets = new IdentifierSets();

        Set<String> titles = new HashSet<>();
        Set<String> artistNames = new HashSet<>();
        Set<String> barcodes = new HashSet<>();

        for (SearchableObject result : results) {
            titles.add(result.getTitle().toLowerCase());
            for (String artistName : result.getArtistNames()) {
                artistNames.add(artistName.toLowerCase());
            }
            for (String barcode : result.getBarcodes()) {
                barcodes.add(barcode);
            }
        }
        sets.setBarcodes(barcodes);
        sets.setTitles(titles);
        sets.setArtistNames(artistNames);

        return sets;
    }

    private List<SearchableObject> convertToSearchableObjects(List<Release> releases) {
        List<SearchableObject> searches = new ArrayList<>();

        for (Release release : releases) {
            Release detailedRelease = this.discogsService.getRelease(release.getId());
            SearchableObject search = new SearchableObject();
            List<String> artistNames = new ArrayList<>();
            List<String> barcodes = new ArrayList<>();

            search.setTitle(detailedRelease.getTitle());
            for (Artist artist : detailedRelease.getArtists()) {
                artistNames.add(artist.getName());
            }
            for (Identifier identifier : detailedRelease.getIdentifiers()) {
                if (identifier.getType().toLowerCase().contains("barcode")) {
                    barcodes.add(identifier.getValue());
                }
            }
            search.setBarcodes(barcodes);
            search.setArtistNames(artistNames);
            searches.add(search);
        }

        return searches;
    }

    private AllHits scrapeAllSites(IdentifierSets identifiers) {
        AllHits allHits = new AllHits();
        Map<String, RealGroovyHit> realGroovyHits = new HashMap<>();
        Map<String, JustForTheRecordHit> justForTheRecordHits = new HashMap<>();
        Map<String, MarbecksHit> marbecksHits = new HashMap<>();


        for (String identifier : identifiers.getCombinedIdentifiers()) {
            realGroovyHits.putAll(this.realGroovyScraperService.scrape(identifier, identifiers.getArtistNames(), identifiers.getTitles()));
            justForTheRecordHits.putAll(this.justForTheRecordService.scrape(identifier, identifiers.getArtistNames(), identifiers.getTitles()));
            marbecksHits.putAll(this.marbecksService.scrape(identifier, identifiers.getArtistNames(), identifiers.getTitles()));
        }

        allHits.setRealGroovyHits(realGroovyHits);
        allHits.setJustForTheRecordHits(justForTheRecordHits);
        allHits.setMarbeckHits(marbecksHits);
        return allHits;

    }

}

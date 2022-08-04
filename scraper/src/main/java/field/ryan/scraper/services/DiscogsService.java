package field.ryan.scraper.services;

import field.ryan.scraper.data.MasterReleaseVersions;
import field.ryan.scraper.data.discogs.Release;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DiscogsService {

    @Value("${discogs.api.endpoint}")
    private String apiRoot;
    private final RestTemplate restTemplate;

    public DiscogsService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    public Release test() {
        String url = apiRoot + "releases/249504";

       return this.restTemplate.getForObject(url, Release.class);

    }

    public List<Release> getReleasesInMaster(String masterId) {
        String url = apiRoot + "masters/" + masterId + "/versions?format=Vinyl";
        ResponseEntity<MasterReleaseVersions> response = this.restTemplate.getForEntity(url, MasterReleaseVersions.class);
        return response.getBody().getVersions();
    }

    public Release getRelease(String releaseId) {
        String url = apiRoot + "releases/" + releaseId;
        ResponseEntity<Release> response = this.restTemplate.getForEntity(url, Release.class);
        return response.getBody();
    }
}

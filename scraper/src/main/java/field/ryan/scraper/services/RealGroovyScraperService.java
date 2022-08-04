package field.ryan.scraper.services;

import field.ryan.scraper.data.hits.RealGroovyHit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RealGroovyScraperService {

    @Value("${realGroovy.scrape.endpoint}")
    private String scrapeRoot;

    private static final String searchEndpoint = "search?q=";

    private final ChromeDriver driver;

    @Autowired
    public RealGroovyScraperService(ChromeDriver driver) {
        this.driver = driver;
    }

    public Map<String, RealGroovyHit> scrape(final String value, Set<String> artistNames, Set<String> titles) {
        driver.get(scrapeRoot + searchEndpoint + value);
        final WebElement words = driver.findElement(By.id("hits"));
        final List<WebElement> hits = words.findElements(By.className("ais-hits--item"));
        List<WebElement> filteredHits = hits.stream().filter(hit -> {
            for (String title : titles) {
                if (hit.findElement(By.className("text")).getText().toLowerCase().contains(title) && hit.findElement(By.className("caption")).getText().contains("LP")) {
                    return true;
                }
            }
//            for (String artistName : artistNames) {
//                if (hit.findElement(By.className("subtitle")).getText().toLowerCase().contains(artistName) && hit.findElement(By.className("caption")).getText().contains("LP")) {
//                    return true;
//                }
//            }
            return false;
        }).collect(Collectors.toList());

        Map<String, RealGroovyHit> uniqueHits = new HashMap<>();
        filteredHits.forEach(hit -> {
            RealGroovyHit newHit = new RealGroovyHit();
            String link = hit.findElement(By.className("display-card")).getAttribute("href");
            String title = hit.findElement(By.className("text")).getText();
            String artistName = hit.findElement(By.className("subtitle")).getText();
            String price = hit.findElement(By.className("title")).getText();

            newHit.setLink(link);
            newHit.setTitle(title);
            newHit.setArtistName(artistName);
            newHit.setPrice(price);

            uniqueHits.put(link, newHit);
        });

        return uniqueHits;
    }

}


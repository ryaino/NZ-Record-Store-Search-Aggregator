package field.ryan.scraper.services;

import field.ryan.scraper.data.hits.JustForTheRecordHit;
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
public class JustForTheRecordService {

    @Value("${justForTheRecord.scrape.endpoint}")
    private String scrapeRoot;

    private static final String searchEndpoint = "albums/?filter%5Bkeyword%5D=";

    private final ChromeDriver driver;

    @Autowired
    public JustForTheRecordService(ChromeDriver driver) {
        this.driver = driver;
    }

    public Map<String, JustForTheRecordHit> scrape(final String value, Set<String> artistNames, Set<String> titles) {
        driver.get(scrapeRoot + searchEndpoint + value);

        final WebElement body = driver.findElement(By.tagName("body"));
        final List<WebElement> hits = body.findElements(By.className("product-item"));
        List<WebElement> filteredHits = hits.stream().filter(hit -> {
            for (String title : titles) {
                if (hit.findElement(By.className("name")).findElement(By.tagName("a")).getText().toLowerCase().contains(title)) {
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

        Map<String, JustForTheRecordHit> uniqueHits = new HashMap<>();
        filteredHits.forEach(hit -> {
            JustForTheRecordHit newHit = new JustForTheRecordHit();
            String link = hit.findElement(By.className("name")).findElement(By.tagName("a")).getAttribute("href");
            String title = hit.findElement(By.className("name")).findElement(By.tagName("a")).getText();
            String price = hit.findElement(By.className("price-new")).getText();

            newHit.setLink(link);
            newHit.setArtistAndTitle(title);
            newHit.setPrice(price);

            uniqueHits.put(link, newHit);
        });

        return uniqueHits;
    }
}

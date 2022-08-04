package field.ryan.scraper.services;

import field.ryan.scraper.data.hits.MarbecksHit;
import field.ryan.scraper.data.hits.RealGroovyHit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MarbecksService {


    @Value("${marbecks.scrape.endpoint}")
    private String scrapeRoot;

    private final ChromeDriver driver;

    @Autowired
    public MarbecksService(ChromeDriver driver) {
        this.driver = driver;
    }

    public Map<String, MarbecksHit> scrape(final String value, Set<String> artistNames, Set<String> titles) {
        driver.get(scrapeRoot);
        final WebElement searchInput = driver.findElement(By.id("searchTerm_quicksearch"));
        final WebElement searchSubmit = driver.findElement(By.id("submit_quicksearch_form"));
        Actions action = new Actions(driver);
        searchInput.clear();
        searchInput.sendKeys(value);
        searchSubmit.click();
        Map<String, MarbecksHit> uniqueHits = new HashMap<>();
        final List<WebElement> hits;

        try {
            hits = driver.findElement(By.className("resultslist")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        } catch (NoSuchElementException e) {
            return uniqueHits;
        }

        List<WebElement> filteredHits = hits.stream().filter(hit -> {
            for (String title : titles) {
                if (hit.findElement(By.className("albumtitle")).getText().toLowerCase().contains(title) && hit.findElement(By.className("description")).getText().contains("LP")) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());

        filteredHits.forEach(hit -> {
            MarbecksHit newHit = new MarbecksHit();
            String link = hit.findElement(By.className("details")).findElement(By.tagName("a")).getAttribute("href");
            String title = hit.findElement(By.className("albumtitle")).getText();
            String artistName = hit.findElement(By.className("artistname")).getText();
            String price = hit.findElement(By.className("price")).getText();

            newHit.setLink(link);
            newHit.setTitle(title);
            newHit.setArtistName(artistName);
            newHit.setPrice(price);

            uniqueHits.put(link, newHit);
        });

        return uniqueHits;
    }

}

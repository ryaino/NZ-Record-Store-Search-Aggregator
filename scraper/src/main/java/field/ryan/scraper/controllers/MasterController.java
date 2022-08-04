package field.ryan.scraper.controllers;

import field.ryan.scraper.data.hits.AllHits;
import field.ryan.scraper.services.GatewayService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MasterController {

    private final GatewayService gatewayService;

    @GetMapping("releases")
    public AllHits getMatches(@RequestParam String masterId) {
        return this.gatewayService.getMatchesFromMaster(masterId);
    }
}

package field.ryan.scraper.data;

import field.ryan.scraper.data.discogs.Release;
import lombok.Data;

import java.util.List;

@Data
public class MasterReleaseVersions {

    private List<Release> versions;
}

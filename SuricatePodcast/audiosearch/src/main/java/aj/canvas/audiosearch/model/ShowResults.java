
package aj.canvas.audiosearch.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "network",
        "categories",
        "description",
        "image_files",
        "sc_feed",
        "web_profiles",
        "buzz_score"
})
public class ShowResults {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("network")
    private String network;
    @JsonProperty("categories")
    private List<String> categories = null;
    @JsonProperty("description")
    private String description;
    @JsonProperty("image_files")
    private List<ImageFile> imageFiles = null;
    @JsonProperty("sc_feed")
    private String scFeed;
    @JsonProperty("web_profiles")
    private List<Object> webProfiles = null;
    @JsonProperty("buzz_score")
    private Integer buzzScore;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    @JsonProperty("network")
    public void setNetwork(String network) {
        this.network = network;
    }

    @JsonProperty("categories")
    public List<String> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("image_files")
    public List<ImageFile> getImageFiles() {
        return imageFiles;
    }

    @JsonProperty("image_files")
    public void setImageFiles(List<ImageFile> imageFiles) {
        this.imageFiles = imageFiles;
    }

    @JsonProperty("sc_feed")
    public String getScFeed() {
        return scFeed;
    }

    @JsonProperty("sc_feed")
    public void setScFeed(String scFeed) {
        this.scFeed = scFeed;
    }

    @JsonProperty("web_profiles")
    public List<Object> getWebProfiles() {
        return webProfiles;
    }

    @JsonProperty("web_profiles")
    public void setWebProfiles(List<Object> webProfiles) {
        this.webProfiles = webProfiles;
    }

    @JsonProperty("buzz_score")
    public Integer getBuzzScore() {
        return buzzScore;
    }

    @JsonProperty("buzz_score")
    public void setBuzzScore(Integer buzzScore) {
        this.buzzScore = buzzScore;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
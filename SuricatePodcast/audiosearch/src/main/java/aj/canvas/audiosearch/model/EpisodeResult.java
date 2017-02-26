
package aj.canvas.audiosearch.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "title",
    "description",
    "date_created",
    "identifier",
    "digital_location",
    "physical_location",
    "duration",
    "tags",
    "updated_at",
    "itunes_episode",
    "date_added",
    "show_id",
    "show_title",
    "audio_files",
    "image_files",
    "entities",
    "extra",
    "urls",
    "categories",
    "highlights",
    "network",
    "locations",
    "image_urls",
    "contributors",
    "excerpts",
    "topics"
})
public class EpisodeResult {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("date_created")
    private String dateCreated;
    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("digital_location")
    private String digitalLocation;
    @JsonProperty("physical_location")
    private String physicalLocation;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("tags")
    private List<String> tags = new ArrayList<String>();
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("itunes_episode")
    private String itunesEpisode;
    @JsonProperty("date_added")
    private String dateAdded;
    @JsonProperty("show_id")
    private Integer showId;
    @JsonProperty("show_title")
    private String showTitle;
    @JsonProperty("audio_files")
    private List<AudioFile> audioFiles = new ArrayList<AudioFile>();
    @JsonProperty("image_files")
    private List<ImageFile> imageFiles = new ArrayList<ImageFile>();
    @JsonProperty("entities")
    private List<Entity> entities = new ArrayList<Entity>();
    @JsonProperty("extra")
    private Object extra;
    @JsonProperty("urls")
    private Urls urls;
    @JsonProperty("categories")
    private List<Category> categories = new ArrayList<Category>();
    @JsonProperty("highlights")
    private Object highlights;
    @JsonProperty("network")
    private String network;
    @JsonProperty("locations")
    private Object locations;
    @JsonProperty("image_urls")
    private ImageUrls imageUrls;
    @JsonProperty("contributors")
    private Object contributors;
    @JsonProperty("excerpts")
    private Object excerpts;
    @JsonProperty("topics")
    private Object topics;


    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The dateCreated
     */
    @JsonProperty("date_created")
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * 
     * @param dateCreated
     *     The date_created
     */
    @JsonProperty("date_created")
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * 
     * @return
     *     The identifier
     */
    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    /**
     * 
     * @param identifier
     *     The identifier
     */
    @JsonProperty("identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * 
     * @return
     *     The digitalLocation
     */
    @JsonProperty("digital_location")
    public String getDigitalLocation() {
        return digitalLocation;
    }

    /**
     * 
     * @param digitalLocation
     *     The digital_location
     */
    @JsonProperty("digital_location")
    public void setDigitalLocation(String digitalLocation) {
        this.digitalLocation = digitalLocation;
    }

    /**
     * 
     * @return
     *     The physicalLocation
     */
    @JsonProperty("physical_location")
    public String getPhysicalLocation() {
        return physicalLocation;
    }

    /**
     * 
     * @param physicalLocation
     *     The physical_location
     */
    @JsonProperty("physical_location")
    public void setPhysicalLocation(String physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    /**
     * 
     * @return
     *     The duration
     */
    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The tags
     */
    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The updatedAt
     */
    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt
     *     The updated_at
     */
    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 
     * @return
     *     The itunesEpisode
     */
    @JsonProperty("itunes_episode")
    public Object getItunesEpisode() {
        return itunesEpisode;
    }

    /**
     * 
     * @param itunesEpisode
     *     The itunes_episode
     */
    @JsonProperty("itunes_episode")
    public void setItunesEpisode(String itunesEpisode) {
        this.itunesEpisode = itunesEpisode;
    }

    /**
     * 
     * @return
     *     The dateAdded
     */
    @JsonProperty("date_added")
    public String getDateAdded() {
        return dateAdded;
    }

    /**
     * 
     * @param dateAdded
     *     The date_added
     */
    @JsonProperty("date_added")
    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * 
     * @return
     *     The showId
     */
    @JsonProperty("show_id")
    public Integer getShowId() {
        return showId;
    }

    /**
     * 
     * @param showId
     *     The show_id
     */
    @JsonProperty("show_id")
    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    /**
     * 
     * @return
     *     The showTitle
     */
    @JsonProperty("show_title")
    public String getShowTitle() {
        return showTitle;
    }

    /**
     * 
     * @param showTitle
     *     The show_title
     */
    @JsonProperty("show_title")
    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    /**
     * 
     * @return
     *     The audioFiles
     */
    @JsonProperty("audio_files")
    public List<AudioFile> getAudioFiles() {
        return audioFiles;
    }

    /**
     * 
     * @param audioFiles
     *     The audio_files
     */
    @JsonProperty("audio_files")
    public void setAudioFiles(List<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
    }

    /**
     * 
     * @return
     *     The imageFiles
     */
    @JsonProperty("image_files")
    public List<ImageFile> getImageFiles() {
        return imageFiles;
    }

    /**
     * 
     * @param imageFiles
     *     The image_files
     */
    @JsonProperty("image_files")
    public void setImageFiles(List<ImageFile> imageFiles) {
        this.imageFiles = imageFiles;
    }

    /**
     * 
     * @return
     *     The entities
     */
    @JsonProperty("entities")
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * 
     * @param entities
     *     The entities
     */
    @JsonProperty("entities")
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    /**
     *
     * @param image_urls
     *     The image_urls
     */
    @JsonProperty("image_urls")
    public ImageUrls getImageUrls() {
        return imageUrls;
    }

    /**
     *
     * @param image_urls
     *     The image_urls
     */
    @JsonProperty("image_urls")
    public void setImageUrls(ImageUrls imageUrls) {
        this.imageUrls = imageUrls;
    }

    /**
     * 
     * @return
     *     The urls
     */
    @JsonProperty("urls")
    public Urls getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    @JsonProperty("urls")
    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    /**
     * 
     * @return
     *     The categories
     */
    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * 
     * @param highlights
     *     The highlights
     */
    @JsonProperty("highlights")
    public void setHighlights(Object highlights) {
        this.highlights = highlights;
    }

}


package aj.canvas.audiosearch.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "title",
    "show_id",
    "show_title",
    "tags",
    "network",
    "audio_files",
    "image_urls",
    "urls"
})
public class Episode {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("show_id")
    private Integer showId;
    @JsonProperty("show_title")
    private String showTitle;
    @JsonProperty("tags")
    private List<Object> tags = new ArrayList<Object>();
    @JsonProperty("network")
    private String network;
    @JsonProperty("audio_files")
    private List<AudioFile> audioFiles = new ArrayList<AudioFile>();
    @JsonProperty("image_urls")
    private ImageUrls imageUrls;
    @JsonProperty("urls")
    private Urls urls;

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
     *     The tags
     */
    @JsonProperty("tags")
    public List<Object> getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    @JsonProperty("tags")
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The network
     */
    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    /**
     * 
     * @param network
     *     The network
     */
    @JsonProperty("network")
    public void setNetwork(String network) {
        this.network = network;
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
     *     The imageUrls
     */
    @JsonProperty("image_urls")
    public ImageUrls getImageUrls() {
        return imageUrls;
    }

    /**
     * 
     * @param imageUrls
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

}

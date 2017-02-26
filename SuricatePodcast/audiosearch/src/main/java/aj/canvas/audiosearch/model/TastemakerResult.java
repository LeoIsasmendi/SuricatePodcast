
package aj.canvas.audiosearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "source",
    "tastemaker",
    "text",
    "published",
    "episode"
})
public class TastemakerResult {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("source")
    private String source;
    @JsonProperty("tastemaker")
    private Tastemaker tastemaker;
    @JsonProperty("text")
    private String text;
    @JsonProperty("published")
    private String published;
    @JsonProperty("episode")
    private Episode episode;

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
     *     The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     * 
     * @param source
     *     The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 
     * @return
     *     The tastemaker
     */
    @JsonProperty("tastemaker")
    public Tastemaker getTastemaker() {
        return tastemaker;
    }

    /**
     * 
     * @param tastemaker
     *     The tastemaker
     */
    @JsonProperty("tastemaker")
    public void setTastemaker(Tastemaker tastemaker) {
        this.tastemaker = tastemaker;
    }

    /**
     * 
     * @return
     *     The text
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 
     * @return
     *     The published
     */
    @JsonProperty("published")
    public String getPublished() {
        return published;
    }

    /**
     * 
     * @param published
     *     The published
     */
    @JsonProperty("published")
    public void setPublished(String published) {
        this.published = published;
    }

    /**
     * 
     * @return
     *     The episode
     */
    @JsonProperty("episode")
    public Episode getEpisode() {
        return episode;
    }

    /**
     * 
     * @param episode
     *     The episode
     */
    @JsonProperty("episode")
    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

}

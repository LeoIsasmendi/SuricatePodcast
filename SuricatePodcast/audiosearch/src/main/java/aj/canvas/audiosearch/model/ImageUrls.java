
package aj.canvas.audiosearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "thumb",
    "full"
})
public class ImageUrls {

    @JsonProperty("thumb")
    private String thumb;
    @JsonProperty("full")
    private String full;

    /**
     * 
     * @return
     *     The thumb
     */
    @JsonProperty("thumb")
    public String getThumb() {
        return thumb;
    }

    /**
     * 
     * @param thumb
     *     The thumb
     */
    @JsonProperty("thumb")
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     * 
     * @return
     *     The full
     */
    @JsonProperty("full")
    public String getFull() {
        return full;
    }

    /**
     * 
     * @param full
     *     The full
     */
    @JsonProperty("full")
    public void setFull(String full) {
        this.full = full;
    }

}

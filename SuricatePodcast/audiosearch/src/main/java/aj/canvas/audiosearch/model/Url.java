
package aj.canvas.audiosearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "full",
    "thumb"
})
public class Url {

    @JsonProperty("full")
    private String full;
    @JsonProperty("thumb")
    private String thumb;

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

}

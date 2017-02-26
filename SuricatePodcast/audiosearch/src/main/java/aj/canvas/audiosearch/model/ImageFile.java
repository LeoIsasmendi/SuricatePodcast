
package aj.canvas.audiosearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url"
})
public class ImageFile {

    @JsonProperty("url")
    private Url url;

    /**
     * 
     * @return
     *     The url
     */
    @JsonProperty("url")
    public Url getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    @JsonProperty("url")
    public void setUrl(Url url) {
        this.url = url;
    }

}

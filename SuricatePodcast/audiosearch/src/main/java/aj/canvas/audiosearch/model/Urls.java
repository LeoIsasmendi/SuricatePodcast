
package aj.canvas.audiosearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "self",
    "ui"
})
public class Urls {

    @JsonProperty("self")
    private String self;
    @JsonProperty("ui")
    private String ui;

    /**
     * 
     * @return
     *     The self
     */
    @JsonProperty("self")
    public String getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    @JsonProperty("self")
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * 
     * @return
     *     The ui
     */
    @JsonProperty("ui")
    public String getUi() {
        return ui;
    }

    /**
     * 
     * @param ui
     *     The ui
     */
    @JsonProperty("ui")
    public void setUi(String ui) {
        this.ui = ui;
    }

}

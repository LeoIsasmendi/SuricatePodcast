
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
    "trend",
    "twitter_url",
    "related_episodes"
})
public class TrendResult {

    @JsonProperty("trend")
    private String trend;
    @JsonProperty("twitter_url")
    private String twitterUrl;
    @JsonProperty("related_episodes")
    private List<RelatedEpisodes> relatedEpisodes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The trend
     */
    @JsonProperty("trend")
    public String getTrend() {
        return trend;
    }

    /**
     * 
     * @param trend
     *     The trend
     */
    @JsonProperty("trend")
    public void setTrend(String trend) {
        this.trend = trend;
    }

    /**
     * 
     * @return
     *     The twitterUrl
     */
    @JsonProperty("twitter_url")
    public String getTwitterUrl() {
        return twitterUrl;
    }

    /**
     * 
     * @param twitterUrl
     *     The twitter_url
     */
    @JsonProperty("twitter_url")
    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    /**
     * 
     * @return
     *     The relatedEpisodes
     */
    @JsonProperty("related_episodes")
    public List<RelatedEpisodes> getRelatedEpisodes() {
        return relatedEpisodes;
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

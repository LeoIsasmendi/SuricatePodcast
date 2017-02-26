
package aj.canvas.audiosearch.model;

import java.util.ArrayList;
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
    "query",
    "total_results",
    "page",
    "results_per_page",
    "results"
})
public class EpisodeQueryResult {

    @JsonProperty("query")
    private String query;
    @JsonProperty("total_results")
    private Integer totalResults;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("results_per_page")
    private Integer resultsPerPage;
    @JsonProperty("results")
    private List<EpisodeResult> results = new ArrayList<EpisodeResult>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The query
     */
    @JsonProperty("query")
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query
     *     The query
     */
    @JsonProperty("query")
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     *
     * @return
     *     The totalResults
     */
    @JsonProperty("total_results")
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     *     The total_results
     */
    @JsonProperty("total_results")
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
     *
     * @return
     *     The page
     */
    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @param page
     *     The page
     */
    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     *
     * @return
     *     The resultsPerPage
     */
    @JsonProperty("results_per_page")
    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     *
     * @param resultsPerPage
     *     The results_per_page
     */
    @JsonProperty("results_per_page")
    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    /**
     *
     * @return
     *     The results
     */
    @JsonProperty("results")
    public List<EpisodeResult> getResults() {
        return results;
    }

    /**
     *
     * @param results
     *     The results
     */
    @JsonProperty("results")
    public void setResults(List<EpisodeResult> results) {
        this.results = results;
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

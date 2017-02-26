
package aj.canvas.audiosearch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "parent_id",
        "name",
        "parent_name",
        "name_lc"
})
public class Category {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("parent_id")
    private Object parentId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("parent_name")
    private String parent_name;
    @JsonProperty("name_lc")
    private String nameLc;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The parentId
     */
    @JsonProperty("parent_id")
    public Object getParentId() {
        return parentId;
    }

    /**
     * @param parentId The parent_id
     */
    @JsonProperty("parent_id")
    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The nameLc
     */
    @JsonProperty("name_lc")
    public String getNameLc() {
        return nameLc;
    }

    /**
     * @param nameLc The name_lc
     */
    @JsonProperty("name_lc")
    public void setNameLc(String nameLc) {
        this.nameLc = nameLc;
    }

}

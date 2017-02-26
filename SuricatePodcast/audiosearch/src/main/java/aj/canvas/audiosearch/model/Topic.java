
package aj.canvas.audiosearch.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "is_confirmed",
    "identifier",
    "name",
    "score",
    "category",
    "entity_type",
    "item_id",
    "extra",
    "created_at",
    "updated_at"
})
public class Topic {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("is_confirmed")
    private Boolean isConfirmed;
    @JsonProperty("identifier")
    private Object identifier;
    @JsonProperty("name")
    private String name;
    @JsonProperty("score")
    private Float score;
    @JsonProperty("category")
    private String category;
    @JsonProperty("entity_type")
    private Object entityType;
    @JsonProperty("item_id")
    private Integer itemId;
    @JsonProperty("extra")
    private String extra;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     *     The isConfirmed
     */
    @JsonProperty("is_confirmed")
    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    /**
     * 
     * @param isConfirmed
     *     The is_confirmed
     */
    @JsonProperty("is_confirmed")
    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    /**
     * 
     * @return
     *     The identifier
     */
    @JsonProperty("identifier")
    public Object getIdentifier() {
        return identifier;
    }

    /**
     * 
     * @param identifier
     *     The identifier
     */
    @JsonProperty("identifier")
    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The score
     */
    @JsonProperty("score")
    public Float getScore() {
        return score;
    }

    /**
     * 
     * @param score
     *     The score
     */
    @JsonProperty("score")
    public void setScore(Float score) {
        this.score = score;
    }

    /**
     * 
     * @return
     *     The category
     */
    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 
     * @return
     *     The entityType
     */
    @JsonProperty("entity_type")
    public Object getEntityType() {
        return entityType;
    }

    /**
     * 
     * @param entityType
     *     The entity_type
     */
    @JsonProperty("entity_type")
    public void setEntityType(Object entityType) {
        this.entityType = entityType;
    }

    /**
     * 
     * @return
     *     The itemId
     */
    @JsonProperty("item_id")
    public Integer getItemId() {
        return itemId;
    }

    /**
     * 
     * @param itemId
     *     The item_id
     */
    @JsonProperty("item_id")
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * 
     * @return
     *     The extra
     */
    @JsonProperty("extra")
    public String getExtra() {
        return extra;
    }

    /**
     * 
     * @param extra
     *     The extra
     */
    @JsonProperty("extra")
    public void setExtra(String extra) {
        this.extra = extra;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

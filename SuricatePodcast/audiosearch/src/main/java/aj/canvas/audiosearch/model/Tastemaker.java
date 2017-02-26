
package aj.canvas.audiosearch.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "type_of",
    "identifier",
    "description",
    "link",
    "img_asset",
    "categories",
    "created_at",
    "updated_at",
    "extra"
})
public class Tastemaker {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type_of")
    private String typeOf;
    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("description")
    private String description;
    @JsonProperty("link")
    private String link;
    @JsonProperty("img_asset")
    private String imgAsset;
    @JsonProperty("categories")
    private List<String> categories = new ArrayList<String>();
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("extra")
    private Extra extra;

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
     *     The typeOf
     */
    @JsonProperty("type_of")
    public String getTypeOf() {
        return typeOf;
    }

    /**
     * 
     * @param typeOf
     *     The type_of
     */
    @JsonProperty("type_of")
    public void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }

    /**
     * 
     * @return
     *     The identifier
     */
    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    /**
     * 
     * @param identifier
     *     The identifier
     */
    @JsonProperty("identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The link
     */
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    /**
     * 
     * @param link
     *     The link
     */
    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 
     * @return
     *     The imgAsset
     */
    @JsonProperty("img_asset")
    public String getImgAsset() {
        return imgAsset;
    }

    /**
     * 
     * @param imgAsset
     *     The img_asset
     */
    @JsonProperty("img_asset")
    public void setImgAsset(String imgAsset) {
        this.imgAsset = imgAsset;
    }

    /**
     * 
     * @return
     *     The categories
     */
    @JsonProperty("categories")
    public List<String> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    @JsonProperty("categories")
    public void setCategories(List<String> categories) {
        this.categories = categories;
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

    /**
     * 
     * @return
     *     The extra
     */
    @JsonProperty("extra")
    public Extra getExtra() {
        return extra;
    }

    /**
     * 
     * @param extra
     *     The extra
     */
    @JsonProperty("extra")
    public void setExtra(Extra extra) {
        this.extra = extra;
    }

}

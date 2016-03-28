
package com.example.arking.vkstore.model.Market;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Category {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("section")
    @Expose
    private Section section;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The section
     */
    public Section getSection() {
        return section;
    }

    /**
     * 
     * @param section
     *     The section
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

}

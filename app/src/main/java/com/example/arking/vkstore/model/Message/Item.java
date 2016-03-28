
package com.example.arking.vkstore.model.Message;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Item {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("from_id")
    @Expose
    private Integer fromId;
    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("read_state")
    @Expose
    private Integer readState;
    @SerializedName("out")
    @Expose
    private Integer out;

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
     *     The body
     */
    public String getBody() {
        return body;
    }

    /**
     * 
     * @param body
     *     The body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The fromId
     */
    public Integer getFromId() {
        return fromId;
    }

    /**
     * 
     * @param fromId
     *     The from_id
     */
    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    /**
     * 
     * @return
     *     The date
     */
    public Integer getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The date
     */
    public void setDate(Integer date) {
        this.date = date;
    }

    /**
     * 
     * @return
     *     The readState
     */
    public Integer getReadState() {
        return readState;
    }

    /**
     * 
     * @param readState
     *     The read_state
     */
    public void setReadState(Integer readState) {
        this.readState = readState;
    }

    /**
     * 
     * @return
     *     The out
     */
    public Integer getOut() {
        return out;
    }

    /**
     * 
     * @param out
     *     The out
     */
    public void setOut(Integer out) {
        this.out = out;
    }

}

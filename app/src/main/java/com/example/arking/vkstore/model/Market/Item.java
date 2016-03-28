
package com.example.arking.vkstore.model.Market;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

import com.example.arking.vkstore.Likes;
import com.example.arking.vkstore.Photo;
import com.example.arking.vkstore.Price;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Item {

    @SerializedName("views_count")
    @Expose
    private Integer viewsCount;
    @SerializedName("thumb_photo")
    @Expose
    private String thumbPhoto;
    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("can_comment")
    @Expose
    private Integer canComment;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = new ArrayList<Photo>();
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("likes")
    @Expose
    private Likes likes;
    @SerializedName("owner_id")
    @Expose
    private Integer ownerId;
    @SerializedName("can_repost")
    @Expose
    private Integer canRepost;
    @SerializedName("availability")
    @Expose
    private Integer availability;

    /**
     * 
     * @return
     *     The viewsCount
     */
    public Integer getViewsCount() {
        return viewsCount;
    }

    /**
     * 
     * @param viewsCount
     *     The views_count
     */
    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    /**
     * 
     * @return
     *     The thumbPhoto
     */
    public String getThumbPhoto() {
        return thumbPhoto;
    }

    /**
     * 
     * @param thumbPhoto
     *     The thumb_photo
     */
    public void setThumbPhoto(String thumbPhoto) {
        this.thumbPhoto = thumbPhoto;
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
     *     The canComment
     */
    public Integer getCanComment() {
        return canComment;
    }

    /**
     * 
     * @param canComment
     *     The can_comment
     */
    public void setCanComment(Integer canComment) {
        this.canComment = canComment;
    }

    /**
     * 
     * @return
     *     The photos
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * 
     * @param photos
     *     The photos
     */
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

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
     *     The category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * 
     * @param price
     *     The price
     */
    public void setPrice(Price price) {
        this.price = price;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The likes
     */
    public Likes getLikes() {
        return likes;
    }

    /**
     * 
     * @param likes
     *     The likes
     */
    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    /**
     * 
     * @return
     *     The ownerId
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * 
     * @param ownerId
     *     The owner_id
     */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 
     * @return
     *     The canRepost
     */
    public Integer getCanRepost() {
        return canRepost;
    }

    /**
     * 
     * @param canRepost
     *     The can_repost
     */
    public void setCanRepost(Integer canRepost) {
        this.canRepost = canRepost;
    }

    /**
     * 
     * @return
     *     The availability
     */
    public Integer getAvailability() {
        return availability;
    }

    /**
     * 
     * @param availability
     *     The availability
     */
    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

}

package com.example.arking.vkstore.model;


public class userGroup {
    private String name;
    private Long id;
    private String photoURL;
    private Long contactId;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public userGroup(String name, Long id,String photoURL) {
        this.name = name;
        this.id = id;
        this.photoURL=photoURL;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public userGroup(String name, Long id,String photoURL,Long contactId) {
        this.name = name;
        this.id = id;
        this.photoURL=photoURL;
        this.contactId=contactId;

    }
    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

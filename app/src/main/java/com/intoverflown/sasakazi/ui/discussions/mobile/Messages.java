package com.intoverflown.sasakazi.ui.discussions.mobile;

public class Messages {

    private String text;
    private String fullname;
    private String photoUrl;
    private String imageUrl;

    public Messages() {
    }

    public Messages(String text, String fullname, String photoUrl, String imageUrl) {
        this.text = text;
        this.fullname = fullname;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

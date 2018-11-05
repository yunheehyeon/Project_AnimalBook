package edu.android.teamproject;

import java.util.Date;
import java.util.List;

public class ComItem {

    private String title;
    private String text;
    private List<String> tag;
    private List<String> images;
    private String commentTableId;
    private String date;
    private int viewCount;
    private String itemId;
    private String userId;
    private boolean image;
    private int commentCount;

    public ComItem() {
    }

    public ComItem(String title, String text, List<String> tag, List<String> images, String commentTableId, String date, int viewCount, String itemId, String userId, boolean image, int commentCount) {
        this.title = title;
        this.text = text;
        this.tag = tag;
        this.images = images;
        this.commentTableId = commentTableId;
        this.date = date;
        this.viewCount = viewCount;
        this.itemId = itemId;
        this.userId = userId;
        this.image = image;
        this.commentCount = commentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCommentTableId() {
        return commentTableId;
    }

    public void setCommentTableId(String commentTableId) {
        this.commentTableId = commentTableId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}

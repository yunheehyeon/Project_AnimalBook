package edu.android.teamproject;

import java.util.Date;
import java.util.List;

public class ComItem {

    private String itemId;
    private String title;
    private String userId;
    private Date date;
    private int viewCount;
    private List<String> tag;
    private String text;
    private List<String> images;
    private String commentTableId;

    public ComItem(String itemId, String title, String userId, Date date, int viewCount, List<String> tag, String text, List<String> images, String commentTableId) {
        this.itemId = itemId;
        this.title = title;
        this.userId = userId;
        this.date = date;
        this.viewCount = viewCount;
        this.tag = tag;
        this.text = text;
        this.images = images;
        this.commentTableId = commentTableId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

}

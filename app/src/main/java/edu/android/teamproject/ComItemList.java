package edu.android.teamproject;

import java.util.Date;
import java.util.List;

public class ComItemList {

    private String itemId;
    private String title;
    private String userId;
    private Date date;
    private int viewCount;
    private List<String> tag;
    private boolean image;
    private int commentCount;

    public ComItemList(String itemId, String title, String userId, Date date, int viewCount, List<String> tag, boolean image, int commentCount) {
        this.itemId = itemId;
        this.title = title;
        this.userId = userId;
        this.date = date;
        this.viewCount = viewCount;
        this.tag = tag;
        this.image = image;
        this.commentCount = commentCount;
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

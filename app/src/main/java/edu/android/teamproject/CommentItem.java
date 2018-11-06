package edu.android.teamproject;

public class CommentItem {

    private String commentItemId;
    private String commentUserId;
    private String commentText;
    private String commentDate;

    public CommentItem() {
    }

    public String getCommentItemId() {
        return commentItemId;
    }

    public void setCommentItemId(String commentItemId) {
        this.commentItemId = commentItemId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}

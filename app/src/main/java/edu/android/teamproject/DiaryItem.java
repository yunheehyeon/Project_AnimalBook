package edu.android.teamproject;

import java.util.Date;
import java.util.List;

public class DiaryItem {
    private List<String> diaryImages;
    private String diaryText;
    private Date diaryDate;
    private List<String> diaryTag;
    private int diaryId;

    public DiaryItem(){

    }

    public DiaryItem(List<String> diaryImages, String diaryText, Date diaryDate, List<String> diaryTag, int diaryId) {
        this.diaryImages = diaryImages;
        this.diaryText = diaryText;
        this.diaryDate = diaryDate;
        this.diaryTag = diaryTag;
        this.diaryId = diaryId;
    }

    public List<String> getDiaryImages() {
        return diaryImages;
    }

    public void setDiaryImages(List<String> diaryImages) {
        this.diaryImages = diaryImages;
    }

    public String getDiaryText() {
        return diaryText;
    }

    public void setDiaryText(String diaryText) {
        this.diaryText = diaryText;
    }

    public Date getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(Date diaryDate) {
        this.diaryDate = diaryDate;
    }

    public List<String> getDiaryTag() {
        return diaryTag;
    }

    public void setDiaryTag(List<String> diaryTag) {
        this.diaryTag = diaryTag;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
    }

    @Override
    public String toString() {
        return "DiaryItem{" +
                "diaryImages=" + diaryImages +
                ", diaryText='" + diaryText + '\'' +
                ", diaryDate=" + diaryDate +
                ", diaryTag=" + diaryTag +
                ", diaryId=" + diaryId +
                '}';
    }
}
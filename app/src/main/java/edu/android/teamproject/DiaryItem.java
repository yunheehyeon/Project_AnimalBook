package edu.android.teamproject;

import android.provider.BaseColumns;

import java.util.Date;
import java.util.List;

public class DiaryItem {

    private String diaryTitle;
    private List<String> diaryImages;
    private String diaryText;
    private List<String> diaryTag;
    private String diaryDate;
    private String diaryId;

    public DiaryItem(){

    }

    public DiaryItem(String diaryTitle, List<String> diaryImages, String diaryText, List<String> diaryTag, String diaryDate, String diaryId) {
        this.diaryTitle = diaryTitle;
        this.diaryImages = diaryImages;
        this.diaryText = diaryText;
        this.diaryTag = diaryTag;
        this.diaryDate = diaryDate;
        this.diaryId = diaryId;
    }

    public String getDiaryTitle() {
        return diaryTitle;
    }

    public void setDiaryTitle(String diaryTitle) {
        this.diaryTitle = diaryTitle;
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

    public List<String> getDiaryTag() {
        return diaryTag;
    }

    public void setDiaryTag(List<String> diaryTag) {
        this.diaryTag = diaryTag;
    }

    public String getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(String diaryDate) {
        this.diaryDate = diaryDate;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }
}
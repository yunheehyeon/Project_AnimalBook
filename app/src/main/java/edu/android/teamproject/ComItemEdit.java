package edu.android.teamproject;

import java.util.List;

public class ComItemEdit {

    static class Comitems {
        private String comTitle;
        private String comImage;
        private String comText;
        private String comTag;

        public Comitems() {}

        public Comitems(String comTitle, String comImage, String comText, String comTag) {
            this.comTitle = comTitle;
            this.comImage = comImage;
            this.comText = comText;
            this.comTag = comTag;
        }

        public String getComTitle() {
            return comTitle;
        }

        public String getComImage() {
            return comImage;
        }

        public String getComText() {
            return comText;
        }

        public String getComTag() {
            return comTag;
        }
    }

    private String comUri;
    private List<Comitems> comitemList;

    public ComItemEdit() {}

    public ComItemEdit(String comUri, List<Comitems> comitemList) {
        this.comUri = comUri;
        this.comitemList = comitemList;
    }

    public String getComUri() {
        return comUri;
    }

    public void setComUri(String comUri) {
        this.comUri = comUri;
    }

    public List<Comitems> getComitemList() {
        return comitemList;
    }

    public void setComitemList(List<Comitems> comitemList) {
        this.comitemList = comitemList;
    }
}

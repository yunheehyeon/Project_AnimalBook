package edu.android.teamproject;

import java.util.List;

public class MyPageProfile {

    static class ProfileItem {
        private String profileItemName;
        private String profileItemText;

        public ProfileItem() {
        }

        public ProfileItem(String profileItemName, String profileItemText) {
            this.profileItemName = profileItemName;
            this.profileItemText = profileItemText;
        }

        public String getProfileItemName() {
            return profileItemName;
        }

        public String getProfileItemText() {
            return profileItemText;
        }

        public void setProfileItemName(String profileItemName) {
            this.profileItemName = profileItemName;
        }

        public void setProfileItemText(String profileItemText) {
            this.profileItemText = profileItemText;
        }
    }

    private String photoUri;
    private List<ProfileItem> profileItems;

    public MyPageProfile() {
    }

    public MyPageProfile(String photoUri, List<ProfileItem> profileItems) {
        this.photoUri = photoUri;
        this.profileItems = profileItems;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public List<ProfileItem> getProfileItems() {
        return profileItems;
    }

    public void setProfileItems(List<ProfileItem> profileItems) {
        this.profileItems = profileItems;
    }

    @Override
    public String toString() {
        return "MyPageProfile{" +
                "photoUri='" + photoUri + '\'' +
                ", profileItems=" + profileItems +
                '}';
    }
}

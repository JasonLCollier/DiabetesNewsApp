package net.healthtechhub.diabetesnewsapp;

public class Post {

    private String mUrl;
    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mDate;

    public Post(String url, String title, String section, String author, String date) {
        mUrl = url;
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }
}

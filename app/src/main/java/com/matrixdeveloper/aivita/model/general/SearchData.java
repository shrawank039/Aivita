package com.matrixdeveloper.aivita.model.general;

public class SearchData {

    private String tag_name;
    private String video_count;
    private String fb_id;

    public SearchData() {
    }

    public SearchData(String tag_name, String video_count, String fb_id) {
        this.tag_name = tag_name;
        this.fb_id = fb_id;
        this.video_count = video_count;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getVideo_count() {
        return video_count;
    }

    public void setVideo_count(String video_count) {
        this.video_count = video_count;
    }

    @Override
    public String toString() {
        return "SearchData{" +
                "tag_name='" + tag_name + '\'' +
                ", video_count='" + video_count + '\'' +
                '}';
    }
}

package com.matrixdeveloper.aivita.model.request;

import com.google.gson.annotations.SerializedName;

public class SearchRequest {

    @SerializedName("key")
    private String key;

    @SerializedName("start")
    private String start;

    @SerializedName("end")
    private String end;

    @SerializedName("type")
    private String type;


    public SearchRequest() {
    }

    public SearchRequest(String key, String start, String end, String type ) {
        this.key = key;
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String type) {
        this.type = type;
    }

    public String getType() {
        return start;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "key='" + key + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}

package com.matrixdeveloper.aivita.model.response;


import com.matrixdeveloper.aivita.model.general.SearchData;

import java.util.ArrayList;

public class SearchResponse {

    private String status;
    private String code;
    private String msg;
    private ArrayList<SearchData> data;

    public SearchResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<SearchData> getData() {
        return data;
    }

    public void setData(ArrayList<SearchData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "status='" + status + '\'' +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.matrixdeveloper.aivita.model.request;

public class SignupModel {

    private String deviceId;
    private String fname;
    private String lname;
    private String email;
    private String mob;
    private String address;
    private float lat;
    private float lon;

    public SignupModel() {
    }

    public SignupModel(String deviceId, String fname, String lname, String email, String mob, String address, float lat, float lon) {
        this.deviceId = deviceId;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.mob = mob;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return deviceId;
    }

    public void setId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "deviceId='" + deviceId + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", mob='" + mob + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    public String toJson() {
        return "{" +
                "\"deviceId\":\"" + deviceId + "\"," +
                "\"fname\":\"" + fname + "\"," +
                "\"lname\":\"" + lname + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"mob\":\"" + mob + "\"," +
                "\"address\":\"" + address + "\"," +
                "\"lat\":\"" + lat + "\"," +
                "\"lon\":\"" + lon + "\"" +
                "}";
    }
}

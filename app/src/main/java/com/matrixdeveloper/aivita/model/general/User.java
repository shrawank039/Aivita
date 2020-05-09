package com.matrixdeveloper.aivita.model.general;

public class User {
    private String userId;
    private String fname;
    private String lname;
    private String email;
    private String mob;
    private String address;
    private String profilePic;

    private float lat;
    private float lon;

    public User() {

    }

    public User(String userId, String fname, String lname, String email, String mob, String address, String profilePic, float lat, float lon) {
        this.userId = userId;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.mob = mob;
        this.address = address;
        this.profilePic = profilePic;
        this.lat = lat;
        this.lon = lon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", mob='" + mob + '\'' +
                ", address='" + address + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}

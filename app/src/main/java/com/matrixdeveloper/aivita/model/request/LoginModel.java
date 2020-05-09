package com.matrixdeveloper.aivita.model.request;

public class LoginModel {

    private String email;
    private String password;
    private String deviceId;

    public LoginModel() {
    }

    public LoginModel(String email, String password, String deviceId) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "SignupModel{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

    public String toJson() {
        return "{" +
                "\"email\":\"" + email + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"deviceId\":\"" + deviceId + "\"" +
                "}";
    }

}

package com.matrixdeveloper.aivita.model;

public class RegisterModel {


    private Object username;
    private String email;
    private String phone;
    private String password;
    private String fileToUpload;

    public RegisterModel(Object username, String email, String phone, String password, String fileToUpload) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fileToUpload = fileToUpload;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFileToUpload() {
        return fileToUpload;
    }

    public void setFileToUpload(String fileToUpload) {
        this.fileToUpload = fileToUpload;
    }
}

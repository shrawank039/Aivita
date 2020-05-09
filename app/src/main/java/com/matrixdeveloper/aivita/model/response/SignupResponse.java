package com.matrixdeveloper.aivita.model.response;

public class SignupResponse {

    private boolean errorStatus;
    private String message;
    private String email;
    private String pass;

    public SignupResponse(){}

    public SignupResponse(boolean errorStatus, String message, String email, String pass) {
        this.errorStatus = errorStatus;
        this.message = message;
        this.email = email;
        this.pass = pass;
    }

    public boolean isErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(boolean errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "SignupResponse{" +
                "errorStatus=" + errorStatus +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}

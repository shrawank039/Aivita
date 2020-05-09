package com.matrixdeveloper.aivita.model.response;


import com.matrixdeveloper.aivita.model.general.User;

public class LoginResponse {

    private boolean errorStatus;
    private String message;
    private User user;

    public LoginResponse() {
    }

    public LoginResponse(boolean errorStatus, String message, User user) {
        this.errorStatus = errorStatus;
        this.message = message;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "errorStatus=" + errorStatus +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}

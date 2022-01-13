package com.decoupigny.easywork.models.ticket;

public class Commentary {
    private String userId;
    private String firstName;
    private String lastName;
    private String text;
    private String  sendingDate;

    public Commentary(String userId, String firstName, String lastName, String text, String sendingDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.text = text;
        this.sendingDate = sendingDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(String sendingDate) {
        this.sendingDate = sendingDate;
    }
}

package com.decoupigny.easywork.models.ticket;

public class Participant {
    private String userId;
    private String status;
    private String firstName;
    private String lastName;
    private String role;

    public Participant(String userId, String status, String firstName, String lastName, String role) {
        this.userId = userId;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

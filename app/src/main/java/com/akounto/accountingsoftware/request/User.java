package com.akounto.accountingsoftware.request;

public class User {
    private String Email;
    private String FirstName;
    private String LastName;
    private String Password;
    private String Role;

    public String getFirstName() {
        return this.FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getRole() {
        return this.Role;
    }

    public void setRole(String role) {
        this.Role = role;
    }
}

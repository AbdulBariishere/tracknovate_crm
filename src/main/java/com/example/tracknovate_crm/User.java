package com.example.tracknovate_crm;

import java.util.Date;

public class User {


    Date date = new Date();
    private String Username;
    private String Firstname;
    private String Lastname;
    private String Contact;
    private String Email;
    private String Password;
    private String ConfirmPassword;
    private String Department;
    private String State;
    private String city;
    private String CreatedDate;
    private String CreatedBy;
    private String ModifiedDate;
    private String ModifiedBy;
    private String Token;


    public Date getDate() {
        return date;
    }

    public void setDate(Date Date) {
        this.date = date;
    }
    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }


    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {

        this.Username = Username;
    }

    public String getFirstname() {

        return Firstname;
    }

    public void setFirstname(String Firstname) {

        this.Firstname = Firstname;
    }

    public String getLastname() {

        return Lastname;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }


    public String getContact() {

        return Contact;
    }

    public void setContact(String Contact) {

        this.Contact = Contact;
    }

    public String getEmail() {

        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {

        return Password;
    }

    public void setPassword(String Password) {

        this.Password = Password;
    }


    public String getConfirmPassword() {

        return ConfirmPassword;
    }

    public void setConfirmPassword(String ConfirmPassword) {
        this.ConfirmPassword = ConfirmPassword;
    }

    public String getDepartment() {

        return Department;
    }

    public void setDepartment(String Department) {

        this.Department = Department;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {

        this.CreatedDate = CreatedDate;
    }

    public String getCreatedBy() {

        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {

        this.CreatedBy = CreatedBy;
    }


    /*

     */


}
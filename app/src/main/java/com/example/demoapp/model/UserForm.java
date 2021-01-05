package com.example.demoapp.model;

public class UserForm {

    public String formKey, date,gender,message,userid,username,useremail;

    public UserForm(String formkey,String date, String gender, String message,String userid,String username,String useremail) {
        this.formKey=formkey;
        this.date = date;
        this.gender = gender;
        this.message = message;
        this.userid=userid;
        this.username=username;
        this.useremail=useremail;
    }

    public UserForm() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }
}

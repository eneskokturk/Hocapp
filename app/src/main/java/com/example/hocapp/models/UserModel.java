package com.example.hocapp.models;

public class UserModel {

    private String biography;
    private String email;
    private String publisher;


    public UserModel(String biography, String email, String gender, String userName) {
        this.biography = biography;
        this.email = email;
        this.publisher = publisher;

    }

    public UserModel(){

    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }



}

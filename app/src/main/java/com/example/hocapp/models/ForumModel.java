package com.example.hocapp.models;

public class ForumModel {
    private String postid;
    private String forumTitle;
    private String forumContent;
   // private Long date;
    private String publisher;



    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getForumContent() {
        return forumContent;
    }

    public void setForumContent(String forumContent) {
        this.forumContent = forumContent;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    /*


    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }


     */




    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

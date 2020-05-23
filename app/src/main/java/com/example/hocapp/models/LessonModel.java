package com.example.hocapp.models;

public class LessonModel {
    private String lesson;
    private String lessonField;
    private LessonLatLng lessonLatLng;
    private String lessonPrice;
    private String lessonUserEmail;
    private String lessonCity;

    public String getLessonCity() {
        return lessonCity;
    }

    public void setLessonCity(String lessonCity) {
        this.lessonCity = lessonCity;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getLessonField() {
        return lessonField;
    }

    public void setLessonField(String lessonField) {
        this.lessonField = lessonField;
    }

    public LessonLatLng getLessonLatLng() {
        return lessonLatLng;
    }

    public void setLessonLatLng(LessonLatLng lessonLatLng) {
        this.lessonLatLng = lessonLatLng;
    }

    public String getLessonPrice() {
        return lessonPrice;
    }

    public void setLessonPrice(String lessonPrice) {
        this.lessonPrice = lessonPrice;
    }

    public String getLessonUserEmail() {
        return lessonUserEmail;
    }


    public void setLessonUserEmail(String lessonUserEmail) {
        this.lessonUserEmail = lessonUserEmail;
    }
}

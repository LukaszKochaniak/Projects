package com.layla.doctor;

import java.util.Date;

public class Note {

    private String date, selfmood,  mood, heartrate, comment;

    public Note() {
    }

    public Note(String mood, String selfmood, String heartrate, String comment, String date) {
        this.date = date;
        this.selfmood=selfmood;
        this.mood = mood;
        this.heartrate = heartrate;
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSelfmood() {
        return selfmood;
    }

    public void setSelfmood(String selfmood) {
        this.selfmood = selfmood;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getHeart() {
        return heartrate;
    }

    public void setHeart(String heartrate) {
        this.heartrate = heartrate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

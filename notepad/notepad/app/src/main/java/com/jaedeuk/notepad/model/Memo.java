package com.jaedeuk.notepad.model;

import java.io.Serializable;


public class Memo implements Serializable{ // 직렬화
    String title;
    String text;
    boolean important;
    boolean alarm;
    String date;
    String time;
    String url;


    public Memo(String title, String text, boolean important, boolean alarm) {
        this.title = title;
        this.text = text;
        this.important = important;
        this.alarm = alarm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

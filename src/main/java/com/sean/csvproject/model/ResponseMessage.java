package com.sean.csvproject.model;

public class ResponseMessage {
    private String message;
    private int cnt;
    private double time;

    public ResponseMessage() {
    }

    public ResponseMessage(String message, int cnt) {
        this.message = message;
        this.cnt = cnt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}

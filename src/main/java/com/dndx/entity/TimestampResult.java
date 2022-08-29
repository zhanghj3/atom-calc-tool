package com.dndx.entity;

public class TimestampResult {
    private int timestamp;
    private String result;

    public TimestampResult(){};

    public TimestampResult(int timestamp, String result) {
        this.timestamp = timestamp;
        this.result = result;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "[" + timestamp + ", " + result + "]";
    }
}

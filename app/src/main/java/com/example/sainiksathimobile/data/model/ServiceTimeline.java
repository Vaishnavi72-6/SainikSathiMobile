package com.example.sainiksathimobile.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ServiceTimeline {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String year;
    public String position;

    // Optional getters and setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

package com.example.sainiksathimobile.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ServiceTimelineRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private int year;
    private String rank;

    public ServiceTimelineRecord(int id, int userId, int year, String rank) {
        this.id = id;
        this.userId = userId;
        this.year = year;
        this.rank = rank;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getYear() {
        return year;
    }

    public String getRank() {
        return rank;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}

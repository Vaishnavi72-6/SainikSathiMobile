package com.example.sainiksathimobile.data.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class PensionRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String pensionType;
    private double amount;
    private String lastPaidDate;
    private String remarks;

    public PensionRecord() {
    }

    public PensionRecord(int id, int userId, String pensionType, double amount, String lastPaidDate, String remarks) {
        this.id = id;
        this.userId = userId;
        this.pensionType = pensionType;
        this.amount = amount;
        this.lastPaidDate = lastPaidDate;
        this.remarks = remarks;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getPensionType() { return pensionType; }
    public double getAmount() { return amount; }
    public String getLastPaidDate() { return lastPaidDate; }
    public String getRemarks() { return remarks; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setPensionType(String pensionType) { this.pensionType = pensionType; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setLastPaidDate(String lastPaidDate) { this.lastPaidDate = lastPaidDate; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}

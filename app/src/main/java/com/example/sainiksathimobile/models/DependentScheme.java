package com.example.sainiksathimobile.models;

public class DependentScheme {
    public String title;
    public String description;
    public String educationLevel; // e.g., "Undergraduate"
    public int maxAge;
    public int maxIncome;
    public String applicableRelation; // e.g., "Daughter"
    public String applicableState; // e.g., "Telangana" or "All India"
    public String applyUrl;
    public DependentScheme(String title, String description, String educationLevel,
                           int maxAge, int maxIncome, String applicableRelation, String applicableState,String applyUrl) {
        this.title = title;
        this.description = description;
        this.educationLevel = educationLevel;
        this.maxAge = maxAge;
        this.maxIncome = maxIncome;
        this.applicableRelation = applicableRelation;
        this.applicableState = applicableState;
        this.applyUrl = applyUrl;
    }
}

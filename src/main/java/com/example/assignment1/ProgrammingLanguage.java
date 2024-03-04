package com.example.assignment1;

public class ProgrammingLanguage {
    private int id;
    private String language;
    private double percentage;
    private String creationYear;
    private String paradigm;

    public ProgrammingLanguage(int id, String language, double percentage, String creationYear, String paradigm) {
        this.id = id;
        this.language = language;
        this.percentage = percentage;
        this.creationYear = creationYear;
        this.paradigm = paradigm;
    }

    // Getters for all the fields
    public int getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getCreationYear() {
        return creationYear;
    }

    public String getParadigm() {
        return paradigm;
    }
}
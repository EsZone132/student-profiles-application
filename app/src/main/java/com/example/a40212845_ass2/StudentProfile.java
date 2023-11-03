package com.example.a40212845_ass2;

public class StudentProfile {

    private String Name;
    private String Surname;
    private Integer ProfileID;
    private Float GPA;
    private String DateCreated;

    public StudentProfile(String surname, String name, Integer profileID, Float GPA, String dateCreated) {
        Surname = surname;
        Name = name;
        ProfileID = profileID;
        this.GPA = GPA;
        DateCreated = dateCreated;
    }

    public StudentProfile(String name, String surname, Integer profileID, Float GPA) {
        Name = name;
        Surname = surname;
        ProfileID = profileID;
        this.GPA = GPA;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public Integer getProfileID() {
        return ProfileID;
    }

    public void setProfileID(Integer profileID) {
        ProfileID = profileID;
    }

    public Float getGPA() {
        return GPA;
    }

    public void setGPA(Float GPA) {
        this.GPA = GPA;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }
}

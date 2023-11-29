package com.example.oodcw;

public class Clubs {
    private int clubID;
    private String clubName;
    private String clubCategory;
    private String clubAdvisor;

    private String clubMotto;

    public int getClubID() {
        return clubID;
    }

    public String getClubName() {
        return clubName;
    }

    public String getClubCategory() {
        return clubCategory;
    }

    public String getClubAdvisor() {
        return clubAdvisor;
    }

    public String getClubMotto() {
        return clubMotto;
    }

    public Clubs(int clubID, String clubName, String clubCategory, String clubAdvisor, String clubMotto) {
        this.clubID = clubID;
        this.clubName = clubName;
        this.clubCategory = clubCategory;
        this.clubAdvisor = clubAdvisor;
        this.clubMotto = clubMotto;
    }

}

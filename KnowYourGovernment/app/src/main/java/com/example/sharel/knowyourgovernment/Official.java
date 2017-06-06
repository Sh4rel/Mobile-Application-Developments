package com.example.sharel.knowyourgovernment;

import java.io.Serializable;

/**
 * Created by Sharel on 3/25/2017.
 */

public class Official implements Serializable {

    private String name;
    private String office;
    private String party;
    private  String address;
    private String phone;
    private  String email;
    private String website;
    private String photo;
    private String gPLus;
    private  String faceBook;
    private String twitter;
    private String youTube;


    public Official(String name, String office, String party, String address, String phone,  String website, String photo, String gPLus, String faceBook, String twitter, String youTube) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.photo = photo;
        this.gPLus = gPLus;
        this.faceBook = faceBook;
        this.twitter = twitter;
        this.youTube = youTube;
    }

    public Official(String name, String office, String party, String address, String phone, String website, String photo) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.photo = photo;
    }

    public Official(String name, String office, String party, String address, String phone,String email, String website, String photo) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.email=email;
        this.website = website;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getgPLus() {
        return gPLus;
    }

    public void setgPLus(String gPLus) {
        this.gPLus = gPLus;
    }

    public String getFaceBook() {
        return faceBook;
    }

    public void setFaceBook(String faceBook) {
        this.faceBook = faceBook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYouTube() {
        return youTube;
    }

    public void setYouTube(String youTube) {
        this.youTube = youTube;
    }
}

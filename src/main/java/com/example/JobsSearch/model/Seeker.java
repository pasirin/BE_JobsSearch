package com.example.JobsSearch.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "seekers")
public class Seeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "userId")
    private User user;

    private String photo;

    private String phone_number;

    private Date dob;

    private String address;

    private String website;

    private String education;

    private String experience;

    private String skills;

    private String achievements;

    private String other_details;

    @OneToMany(mappedBy = "primaryKey.seeker")
    private Set<History> histories = new HashSet<>();

    public Seeker() {
    }

    public Seeker(User user) {
        this.user = user;
    }

    public void setProfile(String photo, String phone_number, Date dob, String address, String website, String education, String experience, String skills, String achievements, String other_details) {
        this.photo = photo;
        this.phone_number = phone_number;
        this.dob = dob;
        this.address = address;
        this.website = website;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
        this.achievements = achievements;
        this.other_details = other_details;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    @Transient
    public String getEmail() {
        return user.getEmail();
    }

    @Transient
    public void setEmail(String email) {
        user.setEmail(email);
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getOther_details() {
        return other_details;
    }

    public void setOther_details(String other_details) {
        this.other_details = other_details;
    }

    public Set<History> getHistories() {
        return histories;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }
}

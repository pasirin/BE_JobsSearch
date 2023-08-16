package com.example.JobsSearch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // Support better search result
    private String name = "";

    private String photo;

    // Support better search result
    private String phoneNumber = "";

    private Date dob;

    private String address;

    private String website;

    private String education;

    private String experience;

    private String skills;

    private String achievements;

    private String other_details;

    @OneToMany(mappedBy = "primaryKey.seeker", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<History> histories = new HashSet<>();

    public Seeker() {
    }

    public Seeker(User user) {
        this.user = user;
    }

    public void setProfile(String photo, String phone_number, Date dob, String address, String website, String education, String experience, String skills, String achievements, String other_details) {
        this.photo = photo;
        this.phoneNumber = phone_number;
        this.dob = dob;
        this.address = address;
        this.website = website;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
        this.achievements = achievements;
        this.other_details = other_details;
    }
    @Transient
    public String getEmail() {
        return user.getEmail();
    }

    @Transient
    public void setEmail(String email) {
        user.setEmail(email);
    }
}

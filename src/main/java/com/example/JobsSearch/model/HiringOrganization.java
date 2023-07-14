package com.example.JobsSearch.model;

import com.example.JobsSearch.model.util.OrganizationType;

import javax.persistence.*;

@Entity
@Table(name = "hiring_organization")
public class HiringOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String email;

    private String phone_number;

    private String website;

    private String address;

    private String introduction;

    private OrganizationType organizationType;

    public HiringOrganization() {
    }

    public HiringOrganization(Long id, User user, String name, String email, String phone_number, String website, String address, String introduction, OrganizationType organizationType) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.website = website;
        this.address = address;
        this.introduction = introduction;
        this.organizationType = organizationType;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }
}

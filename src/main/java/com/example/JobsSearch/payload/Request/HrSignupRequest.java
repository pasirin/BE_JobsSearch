package com.example.JobsSearch.payload.Request;

import com.example.JobsSearch.model.util.OrganizationType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class HrSignupRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone_number;

    private String website;

    @NotBlank
    private String address;

    private String introduction;

    private OrganizationType organizationType;

    public HrSignupRequest(String username, String password, String name, String email, String phone_number, String website, String address, String introduction, OrganizationType organizationType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.website = website;
        this.address = address;
        this.introduction = introduction;
        this.organizationType = organizationType;
    }

    public HrSignupRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

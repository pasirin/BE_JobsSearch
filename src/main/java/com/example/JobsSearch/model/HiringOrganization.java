package com.example.JobsSearch.model;

import com.example.JobsSearch.model.util.OrganizationType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "hiring_organization")
public class HiringOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String phoneNumber;

    private String website;

    private String address;

    private String introduction;

    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;

    public HiringOrganization() {
    }

    public HiringOrganization(User user) {
        this.user = user;
    }
    public HiringOrganization(User user, String name, String phoneNumber, String website, String address, String introduction, OrganizationType organizationType) {
        this.user = user;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.address = address;
        this.introduction = introduction;
        this.organizationType = organizationType;
    }
}

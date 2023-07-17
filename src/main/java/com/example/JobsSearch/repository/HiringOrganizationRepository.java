package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.HiringOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HiringOrganizationRepository extends JpaRepository<HiringOrganization, Long> {
    Boolean existsByName (String name);
}

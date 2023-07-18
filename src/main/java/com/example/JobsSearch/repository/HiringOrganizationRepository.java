package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.model.Seeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HiringOrganizationRepository extends JpaRepository<HiringOrganization, Long> {
    Boolean existsByName(String name);

    Optional<HiringOrganization> findByUserId(Long id);

    Optional<HiringOrganization> findById(Long id);

    List<HiringOrganization> findByNameLike(String name);

}

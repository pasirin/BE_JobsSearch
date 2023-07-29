package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.Seeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SeekerRepository extends JpaRepository<Seeker, Long> {

  Optional<Seeker> findByUserId(Long id);

  List<Seeker> findByNameLike(String name);

  Collection<Seeker> findByUserEmailLikeAndNameLikeAndPhoneNumberLike(
      String email, String name, String phoneNumber);
}

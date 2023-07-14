package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Các phương thức truy vấn tương ứng với thao tác trên bảng User

    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
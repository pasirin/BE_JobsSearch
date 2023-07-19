package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Các phương thức truy vấn tương ứng với thao tác trên bảng User

    List<User> findByUsernameLike(String name);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);
    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

}
package com.capstone.shop.user.v1.repository;

import com.capstone.shop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    //admin
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

}

package com.app.auth.repository;

import com.app.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository - Database operations for User entity
 *
 * This interface provides CRUD operations and custom queries for User entity.
 * Extends JpaRepository to get built-in methods like save(), findAll(), delete(), etc.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address
     *
     * @param email User's email address
     * @return Optional containing User if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
}


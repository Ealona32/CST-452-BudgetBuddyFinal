package com.budget.app.repository;

import com.budget.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// This is my repository for the User entity.
// By extending JpaRepository, I automatically get a bunch of basic methods
// like save(), findById(), findAll(), deleteById(), etc. without writing SQL.
public interface UserRepository extends JpaRepository<User, Long> {

    // This is a custom finder method that Spring Data JPA builds for me
    // just by reading the method name.
    //
    // It looks for a user row where BOTH the email and password match.
    // I’m using this in my AuthController to handle login.
    //
    // In a real production app, I would not check passwords like this directly.
    // I’d store an encoded password and verify it with a PasswordEncoder.
    //
    // I wrap the result in Optional so I can cleanly handle the "not found" case.
    Optional<User> findByEmailAndPassword(String email, String password);
}

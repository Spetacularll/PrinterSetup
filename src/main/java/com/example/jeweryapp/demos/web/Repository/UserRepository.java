package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

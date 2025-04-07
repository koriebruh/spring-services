package com.koriebruh.authservice.repository;

import com.koriebruh.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Optional<User> findByUsernameAndDeletedAtIsNull(String username);
//
//    Boolean existsByUsernameAndDeletedAtIsNull(String username);
//
//    List<User> findAllByDeletedAtIsNull(String username);

}

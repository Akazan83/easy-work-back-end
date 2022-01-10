package com.decoupigny.easywork.repository;

import java.util.Optional;

import com.decoupigny.easywork.models.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
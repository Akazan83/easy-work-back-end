package com.decoupigny.easywork.repository;

import java.util.Optional;

import com.decoupigny.easywork.models.user.ERole;
import com.decoupigny.easywork.models.user.Role;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
package com.decoupigny.easywork.controllers;

import com.decoupigny.easywork.models.user.User;
import com.decoupigny.easywork.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(tags = "Users")
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Operation(summary = "Retrieve all users", description = "Get all users from database")
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        try {
            List<User> users = new ArrayList<>();
            for (User usr : userRepository.findAll()) {
                User user = new User(usr.getId(), usr.getFirstName(), usr.getLastName());
                users.add(user);
            }
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
package com.riannegreiros.springecommerce.controller;

import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public User saveUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable(name = "id")UUID id) {
        User userResponse = userService.updateUser(user, id);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}

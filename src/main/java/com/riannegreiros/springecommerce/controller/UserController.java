package com.riannegreiros.springecommerce.controller;

import com.riannegreiros.springecommerce.entity.User;
import com.riannegreiros.springecommerce.service.UserService;
import com.riannegreiros.springecommerce.utils.AppConstants;
import com.riannegreiros.springecommerce.utils.GetAllUsersResponse;
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

    @GetMapping
    public GetAllUsersResponse getAllUsers(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return userService.getAllUsers(page, size, sortBy, sortDir);
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

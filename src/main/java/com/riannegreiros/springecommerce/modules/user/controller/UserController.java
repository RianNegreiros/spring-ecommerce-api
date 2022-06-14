package com.riannegreiros.springecommerce.modules.user.controller;

import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.modules.user.service.UserService;
import com.riannegreiros.springecommerce.utils.AppConstants;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Api(value = "CRUD for user resource", tags = {"User Controller"})
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get all users with pagination")
    @GetMapping
    public FindAllResponse findAll(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return userService.findAll(page, size, sortBy, sortDir);
    }

    @ApiOperation(value = "Create a file .csv with all users")
    @GetMapping("/csv")
    public void usersInCSV(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"users.csv\"");
        userService.writeUsersToCSV(servletResponse.getWriter());
    }

    @ApiOperation(value = "Create a new user")
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<User> save(@RequestBody User user, @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        User savedUser = userService.save(user);
            if (!multipartFile.isEmpty()) userService.saveImage(multipartFile, savedUser.getId());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update user")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable(name = "id")UUID id) {
        User userResponse = userService.update(user, id);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete user")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") UUID id) throws IOException {
        userService.delete(id);
        return new ResponseEntity<>("User has been deleted successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Upload user image")
    @PostMapping("/image/{id}")
    public ResponseEntity<String> saveImage(@RequestParam("image") MultipartFile multipartFile, @PathVariable(name = "id") UUID id) throws IOException {
        userService.saveImage(multipartFile, id);
        return new ResponseEntity<>("Image has been saved successfully", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Download user image")
    @GetMapping("/image/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "id") UUID id) throws IOException {
        byte[] data = userService.findImage(id);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment: filename=\"" + id + "\"")
                .body(resource);
    }
}

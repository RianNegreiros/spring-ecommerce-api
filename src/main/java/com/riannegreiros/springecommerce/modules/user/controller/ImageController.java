package com.riannegreiros.springecommerce.modules.user.controller;

import com.riannegreiros.springecommerce.modules.user.service.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/{id}")
    public void savePhoto(@RequestParam("image") MultipartFile multipartFile, @PathVariable(name = "id") UUID id) throws IOException {
        imageService.save(multipartFile, id);
    }
}

package com.riannegreiros.springecommerce.modules.user.service.Impl;

import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.modules.user.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.user.repository.UserRepository;
import com.riannegreiros.springecommerce.modules.user.service.ImageService;
import com.riannegreiros.springecommerce.modules.user.utils.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    UserRepository userRepository;

    @Override
    public void save(MultipartFile multipartFile, UUID id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhoto(fileName);
        String uploadDir = "user-images/" + user.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }

    @Override
    public String getPath(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));

        return "/user-images/" + id + "/" + user.getPhoto();
    }
}

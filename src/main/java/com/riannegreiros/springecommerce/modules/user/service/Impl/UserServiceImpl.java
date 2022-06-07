package com.riannegreiros.springecommerce.modules.user.service.Impl;

import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.user.repository.UserRepository;
import com.riannegreiros.springecommerce.modules.user.service.UserService;
import com.riannegreiros.springecommerce.utils.FileUploadUtil;
import com.riannegreiros.springecommerce.utils.FindAllResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        Optional<User> findUser = userRepository.findUserByEmail(user.getEmail());
        if (findUser.isPresent()) {
            throw new Error("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user, UUID id) {
        User findUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));

        findUser.setEmail(user.getEmail());
        findUser.setFirstName(user.getFirstName());
        findUser.setLastName(user.getLastName());
        findUser.setPassword(user.getPassword());
        findUser.setPhoto(user.getPhoto());
        findUser.setRoles(user.getRoles());

        return userRepository.save(findUser);
    }

    @Override
    public FindAllResponse findAll(Integer page, Integer size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);
        List<User> userList = users.getContent();

        return new FindAllResponse(userList,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast());
    }

    @Override
    public void delete(UUID id) throws IOException {
        User userExist = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        FileUploadUtil.deleteFile(userExist.getImagePath());
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public void writeUsersToCSV(Writer writer) throws IOException {
        List<User> userList = userRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("ID", "E-mail", "First Name", "Last Name", "Roles"))) {
            for (User user : userList) {
                csvPrinter.printRecord(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRoles());
            }
        } catch (IOException ex) {
            throw new IOException("Could not save file." + ex);
        }
    }

    @Override
    public void saveImage(MultipartFile multipartFile, UUID id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhoto(fileName);
        String uploadDir = "user-images/" + user.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }
}

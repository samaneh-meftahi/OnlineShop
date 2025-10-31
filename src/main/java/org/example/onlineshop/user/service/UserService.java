package org.example.onlineshop.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.onlineshop.user.repository.UserRepository;
import org.example.onlineshop.user.dto.UserRequestDTO;
import org.example.onlineshop.user.dto.UserResponseDTO;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.user.UserMapper;
import org.example.onlineshop.user.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO register(UserRequestDTO dto) {
        if (dto == null) {
            log.warn("Attempt to register null user");
            throw new IllegalArgumentException("User cannot be null");
        }

        log.info("Registering user with email={}", dto.getEmail());

        User user = new User();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        userRepository.save(user);
        log.info("User registered successfully: id={}", user.getId());

        return UserMapper.toDto(user);
    }

    public Optional<UserResponseDTO> logIn(String email, String password) {
        log.debug("Attempting login for email={}", email);
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()) && user.isActive())
                .map(user -> {
                    log.info("User logged in: id={}", user.getId());
                    return UserMapper.toDto(user);
                });
    }

    public void logOut() {
        log.info("User logged out");
        // Typically handled via token/session clearing
    }

    public Optional<User> findById(Long id) {
        log.debug("Fetching user by id={}", id);
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        log.debug("Fetching user by email={}", email);
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        log.debug("Fetching user by phone={}", phone);
        return userRepository.findByPhone(phone);
    }

    public List<User> findUsersByIds(List<Long> ids) {
        log.debug("Fetching users by ids={}", ids);
        return userRepository.findAllById(ids);
    }


    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        log.info("Updating user: id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found: id={}", id);
                    return new ResourceNotFoundException("User with id: " + id + " not found");
                });

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());

        userRepository.save(user);
        log.info("User updated: id={}", user.getId());

        return UserMapper.toDto(user);
    }

    @Transactional
    public void delete(Long id) {
        log.warn("Deleting user: id={}", id);
        userRepository.findById(id).ifPresent(user -> {
            userRepository.delete(user);
            log.info("User deleted: id={}", id);
        });
    }

    @Transactional
    public Optional<User> changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Changing password for userId={}", id);
        return userRepository.findById(id).map(user -> {
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                log.warn("Invalid old password for userId={}", id);
                throw new IllegalArgumentException("Invalid old password");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            log.info("Password changed for userId={}", id);
            return user;
        });
    }
}

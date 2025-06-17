package org.example.onlineshop.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.model.User;
import org.example.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> logIn(String email, String password) {
        Optional<User> userOptional = findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return userOptional;
            }
        }
        return Optional.empty();
    }
    public void logOut(){

    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            if (updatedUser.getName() != null) user.setName(updatedUser.getName());
            if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPhone() != null) user.setPhone(updatedUser.getPhone());
            if (updatedUser.getAddress() != null) user.setAddress(updatedUser.getAddress());
            if (updatedUser.isAdmin() != user.isAdmin()) user.setAdmin(updatedUser.isAdmin());
            if (updatedUser.isActive() != user.isActive()) user.setActive(updatedUser.isActive());
            return userRepository.save(user);
        });
    }


    @Transactional
    public User save(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        Optional<User> user = findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public Optional<User> changePassword(Long id, String oldPassword, String newPassword) {
        return userRepository.findById(id).map(user -> {
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                return userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Invalid old password");
            }
        });
    }
//    public void restPassword(String email) {
//        Optional<User> user = findByEmail(email);
//        user.isPresent(user->{
//            String re
//        })
//    }
//
//    public boolean activateUser(Long id,String activationCode){
//        Optional<User> userOptional=userRepository.findById(id);
//        if (userOptional.isPresent()&&activationCode.equals(userOptional.get().isActive())){
//            u
//        }
//    }
}

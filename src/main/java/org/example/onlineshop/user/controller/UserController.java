package org.example.onlineshop.user.controller;

import org.example.onlineshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
//
//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        user.setCreatedAt(LocalDateTime.now());
//////        User savedUser = userService.sa(user);
////        return ResponseEntity.ok(savedUser);
//    }

    @GetMapping("/hello/{name}")
    public String sayHelloPath(@PathVariable String name) {
        return "hello " + name;
    }
}

package com.example.security_2fa.controller;

import com.example.security_2fa.model.dto.UserDto;
import com.example.security_2fa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView moveSignup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup");

        return modelAndView;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.signup(userDto));
        } catch (NoSuchAlgorithmException e) {
            Map<String, String> map = new HashMap<>();
            map.put("result", "fail");

            return ResponseEntity.badRequest().body(map);
        }
    }
}

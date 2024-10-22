package com.example.assignment2.controller;

import com.example.assignment2.model.User;
import com.example.assignment2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    // Using Java's built in regex patterns and matchers, so I can check passwords and emails. The regexes I use here are fairly standard ones that are used for validating these things
    // Static and final because these should never change
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    // This is a Spring dependency, used to encrypt passwords
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public String submit(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password, Model model){
        // Built in JPA things to check if something exists by a field, also had to add them to the interface
        if (userRepository.existsByUserName(username)||userRepository.existsByEmail(email)) {
            model.addAttribute("error", "That account already exists!");
            return "signupfailure";
        }
        // Not checking for if a variable is empty since that's built into the HTML form
        // The built in email form does have slight validation, but this is more comprehensive then that
        Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        if (!emailMatcher.matches()) {
            model.addAttribute("error", "Not a valid email format!");
            return "signupfailure";
        }
        Matcher passwordMatcher = PASSWORD_PATTERN.matcher(password);
        if (!passwordMatcher.matches()) {
            model.addAttribute("error", "A password must be at least 8 characters long, have an upper and lower case letter, number and special character!");
            return "signupfailure";
        }
        // Hash the password AFTER checking the length, otherwise it doesn't work
        String hashedPassword = passwordEncoder.encode(password);

        // Give hashed password instead of normal password
        User user = new User(username, email, hashedPassword);
        userRepository.save(user);
        return "redirect:/signupsuccess";
    }

    @GetMapping("/viewdata")
    public String viewdata(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "viewdata";
    }

}
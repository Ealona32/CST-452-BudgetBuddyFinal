package com.budget.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.budget.app.model.User;
import com.budget.app.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    // This is my UserRepository so I can talk to the users table in the database.
    private final UserRepository userRepo;

    // I’m using constructor injection here so Spring can give me the UserRepository.
    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ======================= LOGIN PAGE =======================

    // When the user goes to /login with a GET request, I just show the login.html page.
    @GetMapping("/login")
    public String showLogin() {
        // This returns the Thymeleaf view name "login".
        return "login";
    }

    // ======================= REGISTER PAGE =======================

    // When the user goes to /register with a GET request, I show the register.html page.
    @GetMapping("/register")
    public String showRegister() {
        // Same idea: this maps to my register view.
        return "register";
    }

    // ======================= HANDLE REGISTRATION =======================

    // This method handles the registration form submit.
    // I grab the name, email, and password straight from the form.
    @PostMapping("/register")
    public String doRegister(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirect) {

        // Here I create a new User object with the data from the form.
        // In a more advanced version, I’d probably validate and hash the password,
        // but for this project I’m keeping it simple.
        User user = new User(name, email, password);

        // Save the new user to the database.
        userRepo.save(user);

        // I use RedirectAttributes to send a one-time success message to the login page.
        redirect.addFlashAttribute("message", "Account created! Please log in.");

        // After registration, I redirect them to the login page so they can sign in.
        return "redirect:/login";
    }

    // ======================= HANDLE LOGIN =======================

    // This method handles the login form POST.
    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirect) {

        // I call my custom repository method to find a user
        // that matches the email and password.
        // (Again, simple version – not using password hashing here.)
        return userRepo.findByEmailAndPassword(email, password)
                .map(user -> {
                    // If a match is found, I store the user's name in the session.
                    // I can use this later to show "Welcome, [name]" in the UI.
                    session.setAttribute("userName", user.getName());

                    // Then I redirect them to the home page ("/"),
                    // which in my app is the main dashboard.
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    // If no user is found, I send an error message back to the login page.
                    redirect.addFlashAttribute("error", "Invalid email or password");

                    // Redirect back to /login so they can try again.
                    return "redirect:/login";
                });
    }

    // ======================= LOGOUT =======================

    // This method logs the user out.
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // I just clear out the whole session so they’re fully logged out.
        session.invalidate();

        // Then send them back to the login page.
        return "redirect:/login";
    }
}

package com.david.fightmetrics.controller;

import com.david.fightmetrics.entity.User;
import com.david.fightmetrics.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(user);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException exception) {
            model.addAttribute(
                    "registerError",
                    exception.getMessage()
            );

            return "auth/register";
        }
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "auth/access-denied";
    }
}
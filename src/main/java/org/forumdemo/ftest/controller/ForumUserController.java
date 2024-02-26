package org.forumdemo.ftest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.forumdemo.ftest.service.ForumUserService;
import org.forumdemo.ftest.validation.UserRegistrationForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ForumUserController {
    private final ForumUserService forumUserService;

    @GetMapping("/signup")
    public String signup(UserRegistrationForm userRegistrationForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserRegistrationForm userRegistrationForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.toString());
            return "signup_form";
        }
        if(!userRegistrationForm.getPassword().equalsIgnoreCase(userRegistrationForm.getPasswordDup())) {
            bindingResult.rejectValue("passwordDup", "differentPasswords", "passwords do not coincide");
            return "signup_form";
        }
        forumUserService.addUser(userRegistrationForm.getUsername(),
                userRegistrationForm.getNickname(),
                userRegistrationForm.getPassword(),
                userRegistrationForm.getEmail());

        return "redirect:/api/v1/posts";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

//    @PostMapping("/login")
//    public String login()
}

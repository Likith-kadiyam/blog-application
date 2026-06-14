package com.Likith.BlogApplication.Controller;

import com.Likith.BlogApplication.Entity.User;
import com.Likith.BlogApplication.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @PostMapping("/make-author/{id}")
    public String makeAuthor(
            @PathVariable Long id){

        User user =
                userRepository.findById(id)
                        .orElseThrow();

        user.setRole("ROLE_AUTHOR");

        userRepository.save(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/make-author/{id}")
    @ResponseBody
    public String makeAuthorGet(
            @PathVariable Long id){

        User user =
                userRepository.findById(id)
                        .orElseThrow();

        user.setRole("ROLE_AUTHOR");

        userRepository.save(user);

        return id + " changed to author";
    }
}
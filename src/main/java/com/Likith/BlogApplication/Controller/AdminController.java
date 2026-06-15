package com.Likith.BlogApplication.Controller;
import com.Likith.BlogApplication.Entity.User;
import com.Likith.BlogApplication.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin-users";
    }

    @PostMapping("/make-author/{id}")
    public String makeAuthor(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole("ROLE_AUTHOR");
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/make-user/{id}")
    public String makeUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "redirect:/admin/users";
    }
}
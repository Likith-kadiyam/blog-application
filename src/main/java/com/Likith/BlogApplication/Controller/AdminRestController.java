package com.Likith.BlogApplication.Controller;

import com.Likith.BlogApplication.Entity.User;
import com.Likith.BlogApplication.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserRepository userRepository;

    @PostMapping("/make-author/{id}")
    public ResponseEntity<String> makeAuthor(
            @PathVariable Long id){

        User user =
                userRepository.findById(id)
                        .orElseThrow();

        user.setRole("ROLE_AUTHOR");

        userRepository.save(user);

        return ResponseEntity.ok(
                id + " changed to author"
        );
    }
}
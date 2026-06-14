package com.Likith.BlogApplication.Service;

import com.Likith.BlogApplication.Dto.RegisterDto;
import com.Likith.BlogApplication.Entity.User;
import com.Likith.BlogApplication.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDto dto) {

        if(userRepository.findByEmail(dto.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Email already exists");
        }

        User user = new User();

        user.setName(dto.getName());

        user.setEmail(dto.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        user.setRole("ROLE_USER");

        userRepository.save(user);
    }
}
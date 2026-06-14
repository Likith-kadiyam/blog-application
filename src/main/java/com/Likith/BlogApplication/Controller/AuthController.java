package com.Likith.BlogApplication.Controller;

import com.Likith.BlogApplication.Dto.RegisterDto;
import com.Likith.BlogApplication.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

//    @GetMapping("/encode")
//    @ResponseBody
//    public String encode(){
//
//        return new BCryptPasswordEncoder()
//                .encode("admin123");
//    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("registerDto", new RegisterDto() );
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute RegisterDto dto) {

        userService.register(dto);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }
}
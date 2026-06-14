package com.Likith.BlogApplication.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFound(
            PostNotFoundException ex,
            Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String UsernameNotFoundException( UsernameNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/noAccess";
    }
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("error", "You are not allowed to perform this action.");

        return "access-denied";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(
            Exception ex,
            Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/500";
    }

}
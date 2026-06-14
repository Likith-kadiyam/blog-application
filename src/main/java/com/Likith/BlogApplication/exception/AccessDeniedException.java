package com.Likith.BlogApplication.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message){
        super(message);
    }
    public AccessDeniedException(){
        super("you dont have access !!");
    }
}

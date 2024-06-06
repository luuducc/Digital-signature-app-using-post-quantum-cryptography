package com.example.graduationproject.exception;

public class MyException extends Exception{
    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable err) {
        super(message, err);
    }
}

package ru.andrewalehin.calculator.util;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Illegal argument")
public class IllegalArgumentException extends RuntimeException {

  public IllegalArgumentException(String msg) {
    super(msg);
  }
}
package com.postr.app.controller;

import com.postr.app.dto.ErrorDto;
import com.postr.app.dto.UserDto;
import com.postr.app.service.interfaces.UserService;
import com.postr.app.util.ResponseUtil;
import com.postr.app.validator.UserValidator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/users"})
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping()
  public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserDto userDto, BindingResult results) {
    return userService.createUser(userDto, results);
  }
}

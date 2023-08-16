package com.postr.app.service.interfaces;

import com.postr.app.dto.UserDto;
import com.postr.app.model.User;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface UserService {

  ResponseEntity<Map<String, Object>> createUser(UserDto userDto, BindingResult results);
}

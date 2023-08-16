package com.postr.app.service.interfaces;

import com.postr.app.dto.UserDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface UserService {

  ResponseEntity<Map<String, Object>> createUser(UserDto userDto);
}

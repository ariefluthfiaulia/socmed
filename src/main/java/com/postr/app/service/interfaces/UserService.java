package com.postr.app.service.interfaces;

import com.postr.app.dto.request.UserRequestDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface UserService {

  ResponseEntity<Map<String, Object>> createUser(UserRequestDto userRequestDto);
}

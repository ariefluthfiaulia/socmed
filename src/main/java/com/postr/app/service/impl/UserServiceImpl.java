package com.postr.app.service.impl;

import com.postr.app.dto.request.UserRequestDto;
import com.postr.app.model.User;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.interfaces.UserService;
import com.postr.app.util.ResponseUtil;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  @Transactional
  public ResponseEntity<Map<String, Object>> createUser(UserRequestDto userRequestDto) {
    if (isInvalidUserDto(userRequestDto)) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid input", null);
    }

    Optional<User> oldUser = userRepository.findByUsername(userRequestDto.getUsername());
    if (oldUser.isPresent()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "User not found", null);
    }

    User user = modelMapper.map(userRequestDto, User.class);
    return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        userRepository.save(user));
  }

  private boolean isInvalidUserDto(UserRequestDto userRequestDto) {
    return userRequestDto == null || userRequestDto.getUsername() == null || userRequestDto.getUsername().isBlank();
  }
}

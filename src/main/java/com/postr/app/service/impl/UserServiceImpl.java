package com.postr.app.service.impl;

import com.postr.app.dto.UserDto;
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
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  @Transactional
  public ResponseEntity<Map<String, Object>> createUser(UserDto userDto) {
    if (userDto == null || userDto.getUsername() == null || userDto.getUsername().isBlank()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
          null);
    }

    Optional<User> oldUser = userRepository.findByUsername(userDto.getUsername());
    if (oldUser.isPresent()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
          null);
    } else {
      User user = modelMapper.map(userDto, User.class);
      return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
          userRepository.save(user));
    }
  }
}

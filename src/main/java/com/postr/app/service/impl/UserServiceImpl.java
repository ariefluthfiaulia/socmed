package com.postr.app.service.impl;

import com.postr.app.dto.ErrorDto;
import com.postr.app.dto.UserDto;
import com.postr.app.model.User;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.interfaces.UserService;
import com.postr.app.util.ResponseUtil;
import com.postr.app.validator.UserValidator;
import java.util.List;
import java.util.Map;
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
  private final UserValidator userValidator;
  private static final ModelMapper modelMapper = new ModelMapper();

  @Override
  @Transactional
  public ResponseEntity<Map<String, Object>> createUser(UserDto userDto, BindingResult results) {
    userValidator.validate(userDto, results);
    if (results.hasErrors()) {
      List<ErrorDto> errorList = ResponseUtil.getErrorList(results);
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
          errorList);
    }

    User user = modelMapper.map(userDto, User.class);
    return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        userRepository.save(user));
  }
}

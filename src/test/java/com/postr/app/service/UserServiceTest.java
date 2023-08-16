package com.postr.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postr.app.dto.ErrorDto;
import com.postr.app.dto.UserDto;
import com.postr.app.model.User;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.impl.UserServiceImpl;
import com.postr.app.util.ResponseUtil;
import com.postr.app.validator.UserValidator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@SpringBootTest
public class UserServiceTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserValidator userValidator;

  @Test
  public void testCreateUser_Success() {
    // Arrange
    UserDto userDto = new UserDto();
    BindingResult bindingResult = mock(BindingResult.class);

    when(bindingResult.hasErrors()).thenReturn(false);

    // Mock validation behavior (user is valid)
    doNothing().when(userValidator).validate(userDto, bindingResult);

    // Mock repository save behavior
    User savedUser = new User();
    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    // Act
    ResponseEntity<Map<String, Object>> response = userService.createUser(userDto, bindingResult);

    // Assert
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals("OK", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  public void testCreateUser_UsernameExists() {
    // Arrange
    UserDto userDto = new UserDto();
    userDto.setUsername("existingUsername");

    User user = new User();
    user.setUsername("existingUsername");
    BindingResult bindingResult = mock(BindingResult.class);

    when(bindingResult.hasErrors()).thenReturn(true);
    when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of((user)));

    // Act
    ResponseEntity<Map<String, Object>> response = userService.createUser(userDto, bindingResult);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Bad Request", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }
}

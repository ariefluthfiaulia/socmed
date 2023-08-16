package com.postr.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.postr.app.dto.request.UserRequestDto;
import com.postr.app.model.User;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.impl.UserServiceImpl;
import com.postr.app.service.interfaces.UserService;
import com.postr.app.util.ResponseUtil;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class UserServiceTest {


  private final UserRepository userRepository = mock(UserRepository.class);
  private final ModelMapper modelMapper = mock(ModelMapper.class);
  private final UserService userService = new UserServiceImpl(userRepository, modelMapper);


  @Test
  public void testCreateUser_Success() {
    // Arrange
    UserRequestDto userRequestDto = new UserRequestDto();
    userRequestDto.setUsername("newUsername");

    when(userRepository.findByUsername("newUsername")).thenReturn(Optional.empty());
    when(modelMapper.map(userRequestDto, User.class)).thenReturn(new User());
    when(userRepository.save(any(User.class))).thenReturn(new User());

    // Act
    ResponseEntity<Map<String, Object>> response = userService.createUser(userRequestDto);

    // Assert
    verify(userRepository).findByUsername("newUsername");
    verify(modelMapper).map(userRequestDto, User.class);
    verify(userRepository).save(any(User.class));

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(HttpStatus.OK.getReasonPhrase(), response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
    assertNotNull(response.getBody().get(ResponseUtil.RESPONSE_API_BODY));
  }

  @Test
  public void testCreateUser_UsernameExists() {
    // Arrange
    UserRequestDto userRequestDto = new UserRequestDto();
    userRequestDto.setUsername("existingUsername");

    when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(new User()));

    // Act
    ResponseEntity<Map<String, Object>> response = userService.createUser(userRequestDto);

    // Assert
    verify(userRepository).findByUsername("existingUsername");
    verify(modelMapper, never()).map(any(UserRequestDto.class), eq(User.class));
    verify(userRepository, never()).save(any(User.class));

    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("User not found", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
    assertNull(response.getBody().get(ResponseUtil.RESPONSE_API_BODY));
  }

  @Test
  public void testCreateUser_InvalidInput() {
    // Arrange
    UserRequestDto userRequestDto = new UserRequestDto();

    // Act
    ResponseEntity<Map<String, Object>> response = userService.createUser(userRequestDto);

    // Assert
    verify(userRepository, never()).findByUsername(anyString());
    verify(modelMapper, never()).map(any(UserRequestDto.class), eq(User.class));
    verify(userRepository, never()).save(any(User.class));

    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Invalid input", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
    assertNull(response.getBody().get(ResponseUtil.RESPONSE_API_BODY));
  }
}

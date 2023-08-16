package com.postr.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postr.app.dto.UserDto;
import com.postr.app.service.interfaces.UserService;
import com.postr.app.validator.UserValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@WebMvcTest(UserController.class)
@ComponentScan(basePackages = "com.postr.app")
@SpringJUnitConfig
public class UserControllerTest {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserService userService;

  @Mock
  private UserValidator userValidator;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testRegisterUser_Success() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername("newUser");

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isOk())
        .andExpect((ResultMatcher) jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect((ResultMatcher) jsonPath("$.message").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect((ResultMatcher) jsonPath("$.data").exists());
  }
//
//  @Test
//  public void testRegisterUser_UsernameExists() throws Exception {
//    UserDto userDto = new UserDto();
//    userDto.setUsername("existingUser");
//
//    List<FieldError> fieldErrors = new ArrayList<>();
//    fieldErrors.add(new FieldError("username", "username.already.used", "Username already used"));
//
//    BindingResult bindingResult = mock(BindingResult.class);
//    when(bindingResult.hasErrors()).thenReturn(true);
//    when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
//
//    when(userValidator.supports(UserDto.class)).thenReturn(true);
//    when(userValidator.validate(any(), any())).thenReturn(bindingResult);
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(userDto)))
//        .andExpect(status().isBadRequest())
//        .andExpect((ResultMatcher) jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
//        .andExpect((ResultMatcher) jsonPath("$.message").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
//        .andExpect((ResultMatcher) jsonPath("$.errors[0].field").value("username"))
//        .andExpect((ResultMatcher) jsonPath("$.errors[0].message").value("Username already used"));
//  }
}

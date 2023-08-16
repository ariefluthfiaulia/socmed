package com.postr.app.validator;

import com.postr.app.dto.UserDto;
import com.postr.app.model.User;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.interfaces.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("userValidator")
@RequiredArgsConstructor
public class UserValidator implements Validator {

  private final UserRepository userRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return false;
  }

  @Override
  public void validate(Object target, Errors errors) {
    UserDto request = (UserDto) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty", "Username empty");

    Optional<User> user = userRepository.findByUsername(request.getUsername());
    if (user.isPresent()) {
      errors.rejectValue("username", "username.already.used", "Username already used");
    }
  }
}

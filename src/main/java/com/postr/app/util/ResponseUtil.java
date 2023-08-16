package com.postr.app.util;

import com.postr.app.dto.ErrorDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ResponseUtil {

  public static final String RESPONSE_API_STATUS_CODE = "statusCode";
  public static final String RESPONSE_API_STATUS_MESSAGE = "statusMessage";
  public static final String RESPONSE_API_BODY = "body";
  public static final String RESPONSE_API_ERROR = "errors";

  public static ResponseEntity<Map<String, Object>> returnResponse(
      int statusCode, String statusMessage, Object body) {
    Map<String, Object> responses = new HashMap<>();
    responses.put(RESPONSE_API_STATUS_CODE, statusCode);
    responses.put(RESPONSE_API_STATUS_MESSAGE, statusMessage);
    responses.put(RESPONSE_API_BODY, body);
    return ResponseEntity.status(statusCode).body(responses);
  }

  public static ResponseEntity<Map<String, Object>> returnErrorResponse(
      int statusCode, String statusMessage, Object body) {
    Map<String, Object> responses = new HashMap<>();
    responses.put(RESPONSE_API_STATUS_CODE, statusCode);
    responses.put(RESPONSE_API_STATUS_MESSAGE, statusMessage);
    responses.put(RESPONSE_API_ERROR, body);
    return ResponseEntity.status(statusCode).body(responses);
  }

  public static List<ErrorDto> getErrorList(BindingResult result) {
    List<ErrorDto> errors = new ArrayList<>();
    for (FieldError fieldError : result.getFieldErrors()) {
      ErrorDto errorDto =
          new ErrorDto(fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage());
      errors.add(errorDto);
    }
    return errors;
  }
}

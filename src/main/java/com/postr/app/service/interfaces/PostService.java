package com.postr.app.service.interfaces;

import com.postr.app.dto.PostDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface PostService {

  ResponseEntity<Map<String, Object>> createPost(PostDto postDto, BindingResult results);
}

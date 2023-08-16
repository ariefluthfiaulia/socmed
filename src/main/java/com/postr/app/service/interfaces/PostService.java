package com.postr.app.service.interfaces;

import com.postr.app.dto.PostDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface PostService {

  ResponseEntity<Map<String, Object>> createPost(PostDto postDto);
}

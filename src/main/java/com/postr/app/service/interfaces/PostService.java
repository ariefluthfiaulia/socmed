package com.postr.app.service.interfaces;

import com.postr.app.dto.PostDto;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PostService {

  ResponseEntity<Map<String, Object>> createPost(PostDto postDto);

  ResponseEntity<Map<String, Object>> getUserNewestPost(String username, Pageable paging);
}

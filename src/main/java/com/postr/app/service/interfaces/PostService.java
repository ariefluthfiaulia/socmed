package com.postr.app.service.interfaces;

import com.postr.app.dto.request.PostRequestDto;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PostService {

  ResponseEntity<Map<String, Object>> createPost(PostRequestDto postRequestDto);

  ResponseEntity<Map<String, Object>> getUserNewestPost(String username, Pageable paging);

  ResponseEntity<Map<String, Object>> getDetailPost(String id);
}

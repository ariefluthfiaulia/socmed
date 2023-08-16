package com.postr.app.controller;

import com.postr.app.dto.PostDto;
import com.postr.app.dto.UserDto;
import com.postr.app.service.interfaces.PostService;
import com.postr.app.service.interfaces.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/posts"})
@RequiredArgsConstructor
public class PostController {
  private final PostService postService;

  @PostMapping()
  public ResponseEntity<Map<String, Object>> createPost(@RequestBody PostDto postDto) {
    return postService.createPost(postDto);
  }
}

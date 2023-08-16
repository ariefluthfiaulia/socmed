package com.postr.app.controller;

import com.postr.app.dto.PostDto;
import com.postr.app.dto.UserDto;
import com.postr.app.service.interfaces.PostService;
import com.postr.app.service.interfaces.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping()
  public ResponseEntity<Map<String, Object>> getUserNewestPost(
      @RequestParam String username,
      @RequestParam(required = false, value = "page", defaultValue = "0") int page,
      @RequestParam(required = false, value = "size", defaultValue = "20") int size) {
    Pageable paging = PageRequest.of(page, size);
    return postService.getUserNewestPost(username, paging);
  }
}

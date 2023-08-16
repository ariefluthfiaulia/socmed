package com.postr.app.service.impl;

import com.postr.app.dto.PostDto;
import com.postr.app.dto.UserDto;
import com.postr.app.model.Post;
import com.postr.app.model.User;
import com.postr.app.repository.PostRepository;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.interfaces.PostService;
import com.postr.app.util.ResponseUtil;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public ResponseEntity<Map<String, Object>> createPost(PostDto postDto) {
    if (postDto == null || postDto.getUsername() == null || postDto.getContent() == null || postDto.getUsername()
        .isBlank() || postDto.getContent().isBlank()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
          null);
    }

    if (postDto.getContent().length() > 100) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Content exceeds maximum length",
          null);
    }

    Optional<User> user = userRepository.findByUsername(postDto.getUsername());
    if (user.isPresent()) {
      Post post = Post.builder().user(user.get()).content(postDto.getContent()).build();
      return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
          postRepository.save(post));
    } else {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
          null);
    }
  }
}

package com.postr.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postr.app.dto.PostDto;
import com.postr.app.model.Post;
import com.postr.app.model.User;
import com.postr.app.repository.PostRepository;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.impl.PostServiceImpl;
import com.postr.app.util.ResponseUtil;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class PostServiceTest {

  private final UserRepository userRepository = mock(UserRepository.class);
  private final PostRepository postRepository = mock(PostRepository.class);
  private final PostServiceImpl postService = new PostServiceImpl(postRepository, userRepository);

  @Test
  void testCreatePost_Success() {
    // Arrange
    PostDto postDto = new PostDto();
    postDto.setUsername("testUser");
    postDto.setContent("Test content");

    User user = new User();
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    Post savedPost = new Post();
    when(postRepository.save(any(Post.class))).thenReturn(savedPost);

    // Act
    ResponseEntity<Map<String, Object>> response = postService.createPost(postDto);

    // Assert
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(HttpStatus.OK.getReasonPhrase(), response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));

    Post responsePost = (Post) response.getBody().get(ResponseUtil.RESPONSE_API_BODY);
    assertNotNull(responsePost);
    assertSame(savedPost, responsePost);
  }

  @Test
  void testCreatePost_InvalidInput() {
    // Arrange
    PostDto postDto = new PostDto();
    postDto.setUsername(""); // Invalid username
    postDto.setContent("Test content");

    // Act
    ResponseEntity<Map<String, Object>> response = postService.createPost(postDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(),
        response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  void testCreatePost_UserNotFound() {
    // Arrange
    PostDto postDto = new PostDto();
    postDto.setUsername("nonExistentUser");
    postDto.setContent("Test content");

    when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

    // Act
    ResponseEntity<Map<String, Object>> response = postService.createPost(postDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(),
        response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  void testCreatePost_ContentExceedsMaxLength() {
    // Arrange
    PostDto postDto = new PostDto();
    postDto.setUsername("testUser");
    postDto.setContent("This is a very long content exceeding 100 characters. "
        + "This is a very long content exceeding 100 characters.");

    User user = new User();
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    // Act
    ResponseEntity<Map<String, Object>> response = postService.createPost(postDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Content exceeds maximum length", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }
}

package com.postr.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postr.app.dto.request.ReplyRequestDto;
import com.postr.app.model.Post;
import com.postr.app.model.Reply;
import com.postr.app.model.User;
import com.postr.app.repository.PostRepository;
import com.postr.app.repository.ReplyRepository;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.impl.ReplyServiceImpl;
import com.postr.app.util.ResponseUtil;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class ReplyServiceTest {
  private final UserRepository userRepository = mock(UserRepository.class);
  private final PostRepository postRepository = mock(PostRepository.class);
  private final ReplyRepository replyRepository = mock(ReplyRepository.class);
  private final ReplyServiceImpl replyService = new ReplyServiceImpl(postRepository, userRepository, replyRepository);

  @Test
  void testCreateReply_Success() {
    // Arrange
    ReplyRequestDto replyRequestDto = new ReplyRequestDto();
    replyRequestDto.setUsername("testUser");
    replyRequestDto.setPostId("testPost");
    replyRequestDto.setContent("Test content");

    User user = new User();
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    Post post = new Post();
    when(postRepository.findById("testPost")).thenReturn(Optional.of(post));

    Reply savedReply = new Reply();
    when(replyRepository.save(any(Reply.class))).thenReturn(savedReply);

    // Act
    ResponseEntity<Map<String, Object>> response = replyService.createReply(replyRequestDto);

    // Assert
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(HttpStatus.OK.getReasonPhrase(), response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));

    Reply responseReply = (Reply) response.getBody().get(ResponseUtil.RESPONSE_API_BODY);
    assertNotNull(responseReply);
    assertSame(savedReply, responseReply);
  }

  @Test
  void testCreateReply_InvalidInput() {
    // Arrange
    ReplyRequestDto replyRequestDto = new ReplyRequestDto();
    replyRequestDto.setUsername(""); // Invalid username
    replyRequestDto.setPostId(""); // Invalid post
    replyRequestDto.setContent("Test content");

    // Act
    ResponseEntity<Map<String, Object>> response = replyService.createReply(replyRequestDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Invalid input", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  void testCreateReply_UserNotFound() {
    // Arrange
    ReplyRequestDto replyRequestDto = new ReplyRequestDto();
    replyRequestDto.setUsername("nonExistentUser");
    replyRequestDto.setPostId("postId");
    replyRequestDto.setContent("Test content");

    when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

    Post post = new Post();
    when(postRepository.findById("testPost")).thenReturn(Optional.of(post));

    // Act
    ResponseEntity<Map<String, Object>> response = replyService.createReply(replyRequestDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("User not found", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  void testCreateReply_PostNotFound() {
    // Arrange
    ReplyRequestDto replyRequestDto = new ReplyRequestDto();
    replyRequestDto.setUsername("testUser");
    replyRequestDto.setPostId("nonExistentPost");
    replyRequestDto.setContent("Test content");

    User user = new User();
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    when(postRepository.findById("nonExistentPost")).thenReturn(Optional.empty());

    // Act
    ResponseEntity<Map<String, Object>> response = replyService.createReply(replyRequestDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Post not found", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  void testCreateReply_ContentExceedsMaxLength() {
    // Arrange
    ReplyRequestDto replyRequestDto = new ReplyRequestDto();
    replyRequestDto.setUsername("testUser");
    replyRequestDto.setPostId("testPostId");
    replyRequestDto.setContent("This is a very long content exceeding 100 characters. "
        + "This is a very long content exceeding 100 characters.");

    User user = new User();
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    Post post = new Post();
    when(postRepository.findById("testPostId")).thenReturn(Optional.of(post));

    // Act
    ResponseEntity<Map<String, Object>> response = replyService.createReply(replyRequestDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Content exceeds maximum length", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }
}

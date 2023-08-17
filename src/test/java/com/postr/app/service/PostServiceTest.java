package com.postr.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postr.app.dto.request.PostRequestDto;
import com.postr.app.dto.response.PostDetailResponseDto;
import com.postr.app.dto.response.ReplyResponseDto;
import com.postr.app.model.Post;
import com.postr.app.model.Reply;
import com.postr.app.model.User;
import com.postr.app.repository.PostRepository;
import com.postr.app.repository.ReplyRepository;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.impl.PostServiceImpl;
import com.postr.app.util.ResponseUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class PostServiceTest {

  private final UserRepository userRepository = mock(UserRepository.class);
  private final PostRepository postRepository = mock(PostRepository.class);
  private final ReplyRepository replyRepository = mock(ReplyRepository.class);
  private final PostServiceImpl postService = new PostServiceImpl(postRepository, replyRepository, userRepository);

  @Test
  void testCreatePost_Success() {
    // Arrange
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setUsername("testUser");
    postRequestDto.setContent("Test content");

    User user = new User();
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    Post savedPost = new Post();
    when(postRepository.save(any(Post.class))).thenReturn(savedPost);

    // Act
    ResponseEntity<Map<String, Object>> response = postService.createPost(postRequestDto);

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
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setUsername(""); // Invalid username
    postRequestDto.setContent("Test content");

    // Act
    ResponseEntity<Map<String, Object>> response = postService.createPost(postRequestDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Invalid input", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  void testCreatePost_UserNotFound() {
    // Arrange
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setUsername("nonExistentUser");
    postRequestDto.setContent("Test content");

    when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

    // Act
    ResponseEntity<Map<String, Object>> response = postService.createPost(postRequestDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("User not found", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  void testCreatePost_ContentExceedsMaxLength() {
    // Arrange
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setUsername("testUser");
    postRequestDto.setContent("This is a very long content exceeding 100 characters. "
        + "This is a very long content exceeding 100 characters.");

    User user = new User();
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

    // Act
    ResponseEntity<Map<String, Object>> response = postService.createPost(postRequestDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Content exceeds maximum length", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  public void testGetUserNewestPost_Success() {
    // Arrange
    String username = "testUser";
    Pageable paging = Pageable.ofSize(10).withPage(0);

    List<Post> posts = new ArrayList<>();
    User user = User.builder().username("testUser").build();
    Post post = Post.builder().content("test").user(user).build();
    posts.add(post);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(postRepository.findByUserOrderByCreatedDateDesc(eq(user), any(Pageable.class)))
        .thenReturn(new PageImpl<>(posts, paging, posts.size()));

    // Act
    ResponseEntity<Map<String, Object>> response = postService.getUserNewestPost(username, paging);

    // Assert
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(HttpStatus.OK.getReasonPhrase(), response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));

    List<Post> retrievedPage = (List<Post>) response.getBody().get(ResponseUtil.RESPONSE_API_BODY);
    assertNotNull(retrievedPage);
    assertEquals(posts.size(), retrievedPage.size());
  }

  @Test
  void testGetUserNewestPost_InvalidInput() {
    // Arrange
    String username = "";
    Pageable paging = Pageable.ofSize(10).withPage(0);

    // Act
    ResponseEntity<Map<String, Object>> response = postService.getUserNewestPost(username, paging);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("Invalid input", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }

  @Test
  public void testGetUserNewestPost_UserNotFound() {
    // Arrange
    String username = "nonExistentUser";
    Pageable paging = Pageable.ofSize(10).withPage(0);

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<Map<String, Object>> response = postService.getUserNewestPost(username, paging);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    assertEquals("User not found", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
    assertNull(response.getBody().get(ResponseUtil.RESPONSE_API_BODY));
  }

  @Test
  public void testGetDetailPost_Success() {
    // Arrange
    String postId = "postId";

    User user = new User();
    user.setUsername("user123");

    Post post = new Post();
    post.setId(postId);
    post.setUser(user);
    post.setContent("Post content");

    Reply reply1 = new Reply();
    reply1.setId("replyId1");
    reply1.setUser(user);
    reply1.setContent("Reply content 1");

    Reply reply2 = new Reply();
    reply2.setId("replyId2");
    reply2.setUser(user);
    reply2.setContent("Reply content 2");

    List<Reply> replies = Arrays.asList(reply1, reply2);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(replyRepository.findByPostOrderByCreatedDate(post)).thenReturn(replies);

    // Act
    ResponseEntity<Map<String, Object>> response = postService.getDetailPost(postId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("OK", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));

    PostDetailResponseDto responseBody = (PostDetailResponseDto) response.getBody().get(ResponseUtil.RESPONSE_API_BODY);

    assertNotNull(responseBody);
    assertEquals("user123", responseBody.getUsername());
    assertEquals("Post content", responseBody.getContent());

    List<ReplyResponseDto> replyDtos = responseBody.getReplies();
    assertNotNull(replyDtos);
    assertEquals(2, replyDtos.size());

    ReplyResponseDto replyDto1 = replyDtos.get(0);
    assertEquals("user123", replyDto1.getUsername());
    assertEquals("Reply content 1", replyDto1.getContent());

    ReplyResponseDto replyDto2 = replyDtos.get(1);
    assertEquals("user123", replyDto2.getUsername());
    assertEquals("Reply content 2", replyDto2.getContent());
  }

  @Test
  public void testGetDetailPost_PostNotFound() {
    // Arrange
    String postId = "nonExistentId";

    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<Map<String, Object>> response = postService.getDetailPost(postId);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Post not found", response.getBody().get(ResponseUtil.RESPONSE_API_STATUS_MESSAGE));
  }
}

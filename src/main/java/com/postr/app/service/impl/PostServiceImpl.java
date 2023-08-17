package com.postr.app.service.impl;

import com.postr.app.dto.request.PostRequestDto;
import com.postr.app.dto.response.PostDetailResponseDto;
import com.postr.app.dto.response.ReplyResponseDto;
import com.postr.app.model.Post;
import com.postr.app.model.Reply;
import com.postr.app.model.User;
import com.postr.app.repository.PostRepository;
import com.postr.app.repository.ReplyRepository;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.interfaces.PostService;
import com.postr.app.util.ResponseUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final ReplyRepository replyRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public ResponseEntity<Map<String, Object>> createPost(PostRequestDto postRequestDto) {
    if (isInvalidPostDto(postRequestDto)) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid input", null);
    }

    if (postRequestDto.getContent().length() > 100) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Content exceeds maximum length", null);
    }

    Optional<User> user = userRepository.findByUsername(postRequestDto.getUsername());
    if (user.isEmpty()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "User not found", null);
    }

    Post post = Post.builder()
        .user(user.get())
        .content(postRequestDto.getContent())
        .build();

    return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        postRepository.save(post));
  }

  private boolean isInvalidPostDto(PostRequestDto postRequestDto) {
    return postRequestDto == null || postRequestDto.getUsername() == null || postRequestDto.getContent() == null ||
        postRequestDto.getUsername().isBlank() || postRequestDto.getContent().isBlank();
  }

  @Override
  public ResponseEntity<Map<String, Object>> getUserNewestPost(String username, Pageable paging) {
    if (isInvalidUsername(username)) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid input", null);
    }

    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "User not found", null);
    }

    Page<Post> userPosts = postRepository.findByUserOrderByCreatedDateDesc(user.get(), paging);
    List<Post> posts = userPosts.getContent();

    return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), posts);
  }

  private boolean isInvalidUsername(String username) {
    return username == null || username.isBlank();
  }

  public ResponseEntity<Map<String, Object>> getDetailPost(String id) {
    Optional<Post> postOptional = postRepository.findById(id);
    if (postOptional.isEmpty()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Post not found", null);
    }

    Post post = postOptional.get();
    PostDetailResponseDto postDetailResponseDto = PostDetailResponseDto.builder()
        .username(post.getUser().getUsername())
        .content(post.getContent())
        .build();

    List<Reply> replies = replyRepository.findByPostOrderByCreatedDate(postOptional.get());
    List<ReplyResponseDto> replyResponseDtoList = new ArrayList<>();
    for (Reply reply : replies) {
      ReplyResponseDto replyResponseDto = ReplyResponseDto.builder()
          .username(reply.getUser().getUsername())
          .content(reply.getContent())
          .build();
      replyResponseDtoList.add(replyResponseDto);
    }

    postDetailResponseDto.setReplies(replyResponseDtoList);

    return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), postDetailResponseDto);
  }
}

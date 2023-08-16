package com.postr.app.service.impl;

import com.postr.app.dto.request.ReplyRequestDto;
import com.postr.app.model.Post;
import com.postr.app.model.Reply;
import com.postr.app.model.User;
import com.postr.app.repository.PostRepository;
import com.postr.app.repository.ReplyRepository;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.interfaces.ReplyService;
import com.postr.app.util.ResponseUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ReplyRepository replyRepository;

  @Override
  @Transactional
  public ResponseEntity<Map<String, Object>> createReply(ReplyRequestDto replyRequestDto) {
    if (isInvalidReplyDto(replyRequestDto)) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid input", null);
    }

    if (replyRequestDto.getContent().length() > 100) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Content exceeds maximum length", null);
    }

    Optional<User> user = userRepository.findByUsername(replyRequestDto.getUsername());
    if (user.isEmpty()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "User not found", null);
    }

    Optional<Post> post = postRepository.findById(replyRequestDto.getPostId());
    if (post.isEmpty()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Post not found", null);
    }

    Reply reply = Reply.builder()
        .user(user.get())
        .post(post.get())
        .content(replyRequestDto.getContent())
        .build();

    return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        replyRepository.save(reply));
  }

  private boolean isInvalidReplyDto(ReplyRequestDto replyRequestDto) {
    return replyRequestDto == null || replyRequestDto.getUsername() == null || replyRequestDto.getContent() == null ||
        replyRequestDto.getPostId() == null || replyRequestDto.getUsername().isBlank() ||
        replyRequestDto.getContent().isBlank() || replyRequestDto.getPostId().isBlank();
  }
}

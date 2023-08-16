package com.postr.app.service.impl;

import com.postr.app.dto.ReplyDto;
import com.postr.app.model.Post;
import com.postr.app.model.Reply;
import com.postr.app.model.User;
import com.postr.app.repository.PostRepository;
import com.postr.app.repository.ReplyRepository;
import com.postr.app.repository.UserRepository;
import com.postr.app.service.interfaces.ReplyService;
import com.postr.app.util.ResponseUtil;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ReplyRepository replyRepository;

  @Override
  public ResponseEntity<Map<String, Object>> createReply(ReplyDto replyDto) {
    if (replyDto == null || replyDto.getUsername() == null || replyDto.getContent() == null
        || replyDto.getPostId() == null || replyDto.getUsername()
        .isBlank() || replyDto.getContent().isBlank() || replyDto.getPostId().isBlank()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid input", null);
    }

    if (replyDto.getContent().length() > 100) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Content exceeds maximum length",
          null);
    }

    Optional<User> user = userRepository.findByUsername(replyDto.getUsername());
    if (!user.isPresent()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "User not found", null);
    }

    Optional<Post> post = postRepository.findById(replyDto.getPostId());
    if (!post.isPresent()) {
      return ResponseUtil.returnErrorResponse(HttpStatus.BAD_REQUEST.value(), "Post not found",
          null);
    }

    Reply reply = Reply.builder().user(user.get()).post(post.get()).content(replyDto.getContent())
        .createdDate(new Date()).build();
    return ResponseUtil.returnResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        replyRepository.save(reply));

  }
}

package com.postr.app.controller;

import com.postr.app.dto.PostDto;
import com.postr.app.dto.ReplyDto;
import com.postr.app.service.interfaces.ReplyService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/replies"})
@RequiredArgsConstructor
public class ReplyController {
  private final ReplyService replyService;

  @PostMapping()
  public ResponseEntity<Map<String, Object>> createReply(@RequestBody ReplyDto replyDto) {
    return replyService.createReply(replyDto);
  }
}

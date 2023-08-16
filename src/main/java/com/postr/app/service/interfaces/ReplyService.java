package com.postr.app.service.interfaces;

import com.postr.app.dto.request.ReplyRequestDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface ReplyService {

  ResponseEntity<Map<String, Object>> createReply(ReplyRequestDto replyRequestDto);
//  ResponseEntity<Map<String, Object>> getPostAndAllOfTheReplies(String postId);
}

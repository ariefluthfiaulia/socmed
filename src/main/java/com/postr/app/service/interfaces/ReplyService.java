package com.postr.app.service.interfaces;

import com.postr.app.dto.ReplyDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface ReplyService {

  ResponseEntity<Map<String, Object>> createReply(ReplyDto replyDto);
}

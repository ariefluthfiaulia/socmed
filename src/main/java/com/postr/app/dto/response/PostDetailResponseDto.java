package com.postr.app.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostDetailResponseDto {

  private String username;
  private String content;
  private List<ReplyResponseDto> replies;
}



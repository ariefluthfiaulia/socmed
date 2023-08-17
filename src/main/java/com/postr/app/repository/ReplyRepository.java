package com.postr.app.repository;

import com.postr.app.common.CommonRepository;
import com.postr.app.model.Post;
import com.postr.app.model.Reply;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends CommonRepository<Reply, String> {

  List<Reply> findByPostOrderByCreatedDate(Post post);
}

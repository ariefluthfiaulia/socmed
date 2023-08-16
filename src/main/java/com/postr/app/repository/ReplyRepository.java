package com.postr.app.repository;

import com.postr.app.common.CommonRepository;
import com.postr.app.model.Reply;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends CommonRepository<Reply, String> {

}

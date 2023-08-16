package com.postr.app.repository;

import com.postr.app.common.CommonRepository;
import com.postr.app.model.Post;
import com.postr.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CommonRepository<Post, String> {

  Page<Post> findByUserOrderByCreatedDateDesc(User user, Pageable paging);
}

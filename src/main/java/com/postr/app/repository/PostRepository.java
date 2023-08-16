package com.postr.app.repository;

import com.postr.app.common.CommonRepository;
import com.postr.app.model.Post;
import com.postr.app.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CommonRepository<Post, String> {

  @Query("SELECT model FROM Post AS model JOIN model.user WHERE model.id=:id")
  Post findByPostId(String id);

  Page<Post> findByUserOrderByCreatedDateDesc(User user, Pageable paging);
}

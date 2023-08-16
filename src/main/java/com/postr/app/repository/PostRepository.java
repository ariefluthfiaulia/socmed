package com.postr.app.repository;

import com.postr.app.common.CommonRepository;
import com.postr.app.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CommonRepository<Post, String> {

}

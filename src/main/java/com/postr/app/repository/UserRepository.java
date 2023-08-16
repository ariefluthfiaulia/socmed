package com.postr.app.repository;

import com.postr.app.common.CommonRepository;
import com.postr.app.model.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonRepository<User, String> {
  Optional<User> findByUsername(String username);
}

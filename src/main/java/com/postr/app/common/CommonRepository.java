package com.postr.app.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface CommonRepository<T, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID>,
    CrudRepository<T, ID> {

}

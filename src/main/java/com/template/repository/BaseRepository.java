package com.template.repository;

import com.template.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends MongoRepository<T, String> {

    @Query("{ 'isActive': true }")
    Page<T> findAllActive(Pageable pageable);

    @Query("{ 'isActive': true }")
    List<T> findAllActive();

    @Query("{ '_id': ?0, 'isActive': true }")
    Optional<T> findByIdAndActive(String id);
}

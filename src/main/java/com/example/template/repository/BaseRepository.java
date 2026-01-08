package com.example.template.repository;

import com.example.template.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.isActive = true ORDER BY e.createdAt DESC")
    Page<T> findAllActive(Pageable pageable);

    @Query("SELECT e FROM #{#entityName} e WHERE e.isActive = true ORDER BY e.createdAt DESC")
    List<T> findAllActive();

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.isActive = true")
    Optional<T> findByIdAndActive(Long id);
}

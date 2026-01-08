package com.template.repository;

import com.template.entity.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String email);

    @Query("{ 'email': ?0, 'isActive': true }")
    Optional<User> findByEmailAndActive(String email);

    @Query("{ 'phoneNumber': ?0, 'isActive': true }")
    Optional<User> findByPhoneNumberAndActive(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}

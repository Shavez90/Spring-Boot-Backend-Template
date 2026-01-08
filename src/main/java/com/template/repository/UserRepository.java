package com.template.repository;

import com.template.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findByEmailAndActive(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.isActive = true")
    Optional<User> findByPhoneNumberAndActive(@Param("phoneNumber") String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}

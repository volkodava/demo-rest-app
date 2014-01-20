package com.demo.web.repository;

import com.demo.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("userRepository")
@Transactional
public interface UserRepository extends JpaRepository<User, Long>, UserDetailsService {

    @Transactional(readOnly = true)
    @Query("select u from User u where u.name = :name")
    @Override
    User loadUserByUsername(@Param("name") String name);
}

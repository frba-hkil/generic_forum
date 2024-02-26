package org.forumdemo.ftest.repository;

import org.forumdemo.ftest.entities.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumUserRepository extends JpaRepository<ForumUser, Integer> {
    public Optional<ForumUser> findByusername(String username);
}

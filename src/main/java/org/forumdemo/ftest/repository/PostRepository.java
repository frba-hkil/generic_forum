package org.forumdemo.ftest.repository;

import org.forumdemo.ftest.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByTitleOrContentLike(String title, String content);
    Page<Post> findAll(Pageable pageable);

    @Query("select " +
            "distinct p " +
            "from Post p " +
            "left outer join ForumUser u1 on p.author=u1 " +
            "left outer join Comment c on c.relatedPost=p " +
            "left outer join ForumUser u2 on c.author=u2 " +
            "where p.title like %:keyword% " +
            "or p.content like %:keyword% " +
            "or c.content like %:keyword% " +
            "or u1.nickname like %:keyword% " +
            "or u2.nickname like %:keyword% ")
    Page<Post> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}

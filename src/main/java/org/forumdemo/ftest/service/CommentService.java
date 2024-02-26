package org.forumdemo.ftest.service;

import lombok.RequiredArgsConstructor;
import org.forumdemo.ftest.entities.Comment;
import org.forumdemo.ftest.entities.ForumUser;
import org.forumdemo.ftest.entities.Post;
import org.forumdemo.ftest.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment addComment(Post post, String content, ForumUser user) {

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setRelatedPost(post);
        comment.setAuthor(user);
        comment.setCommentTime(LocalDate.now());

        return commentRepository.save(comment);
    }

    public Comment getComment(int id) {
        return this.commentRepository.findById(id).get();
    }

    public void modifyComment(Comment comment, String content) {
        comment.setContent(content);
        comment.setLastModifyTime(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public void deleteComment(int id) {
        this.commentRepository.deleteById(id);
    }
}

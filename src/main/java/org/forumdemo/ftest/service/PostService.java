package org.forumdemo.ftest.service;

import lombok.RequiredArgsConstructor;
import org.forumdemo.ftest.entities.ForumUser;
import org.forumdemo.ftest.entities.Post;
import org.forumdemo.ftest.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    public Post addPost(String title, String content, ForumUser user) {
        Post newPost = new Post();
        newPost.setPostTime(LocalDateTime.now());
        newPost.setContent(content);
        newPost.setTitle(title);
        newPost.setAuthor(user);
        newPost.setRelatedComments(null);
        return postRepository.save(newPost);
    }

    public void modifyPost(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        post.setLastModifyTime(LocalDateTime.now());
        this.postRepository.save(post);

    }

    public void upvote(Post post, ForumUser user) {
        post.getUpvoters().add(user);
        this.postRepository.save(post);
    }

    public void downvote(Post post, ForumUser user) {
        post.getDownvoters().add(user);
        this.postRepository.save(post);
    }

    public Boolean existsById(Integer id) {
        return postRepository.existsById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Page<Post> getList(int page, String keyword) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.desc("postTime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        return this.postRepository.findAllByKeyword(keyword, pageable);
    }
    public Optional<Post> findPostById(int id) {
        return postRepository.findById(id);
    }

    public List<Post> findPostByTitleOrContent(String keyword) {
        String condition = "%" + keyword + "%";
        return postRepository.findByTitleOrContentLike(condition, condition);
    }

    public void deletePostById(int id) {
        postRepository.deleteById(id);
    }

    public void deleteAllPosts() {
        postRepository.deleteAll();
    }
}

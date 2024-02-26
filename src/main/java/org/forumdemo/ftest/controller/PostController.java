package org.forumdemo.ftest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.forumdemo.ftest.entities.Comment;
import org.forumdemo.ftest.entities.ForumUser;
import org.forumdemo.ftest.entities.Post;
import org.forumdemo.ftest.service.ForumUserService;
import org.forumdemo.ftest.service.PostService;
import org.forumdemo.ftest.validation.CommentForm;
import org.forumdemo.ftest.validation.PostForm;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final ForumUserService userService;

//    @GetMapping("/")
//    public String root() {
//        return "redirect:/api/v1/post";
//    }
//
    @PostMapping("/write")
    public String create(@Valid PostForm postForm, BindingResult bindingResult, Principal principal) throws Exception{
        ForumUser author = this.userService.getUser(principal.getName());
        if(bindingResult.hasErrors()) {
            return "post_form.html";
        }
        Post op = postService.addPost(postForm.getTitleInput(), postForm.getWritePost(), author);

        return "redirect:/api/v1/posts";
    }

    @GetMapping("/{id}/edit")
    public String update(PostForm postForm, @PathVariable("id") int id, Principal principal) {
        Optional<Post> result = this.postService.findPostById(id);
        postForm.setTitleInput(result.get().getTitle());
        postForm.setWritePost(result.get().getContent());

        return "post_form";
    }

    @PostMapping("/{id}/edit")
    public String modifyPost(@Valid PostForm postForm, BindingResult bindingResult,
                             @PathVariable("id") int id, Principal principal) {
        if(bindingResult.hasErrors()){
            return "post_form";
        }
        Post post = this.postService.findPostById(id).get();
        this.postService.modifyPost(post, postForm.getTitleInput(), postForm.getWritePost());

        return String.format("redirect:/api/v1/posts/%d", id);
    }

    @GetMapping("/write")
    public String createPost(PostForm postForm) {

        return "post_form.html";
    }

    @GetMapping
    public String getAll(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Page<Post> postList = postService.getList(page, keyword);
        model.addAttribute("posts", postList);
        model.addAttribute("keyword", keyword);
        return "posts.html";
    }

    @GetMapping("/{id}/upvote")
    public String upvotePost(@PathVariable("id") int id, Principal principal) throws Exception {
        Post post = this.postService.findPostById(id).get();
        ForumUser forumUser = this.userService.getUser(principal.getName());
        if(!post.getUpvoters().contains(forumUser)) {
            this.postService.upvote(post, forumUser);
        }

        return String.format("redirect:/api/v1/posts/%d", id);
    }

    @GetMapping("/{id}/downvote")
    public String downvotePost(@PathVariable("id") int id, Principal principal) throws Exception {
        Post post = this.postService.findPostById(id).get();
        ForumUser forumUser = this.userService.getUser(principal.getName());
        if(!post.getDownvoters().contains(forumUser)) {
            this.postService.downvote(post, forumUser);
        }

        return String.format("redirect:/api/v1/posts/%d", id);
    }

    @GetMapping("/{id}")
    public String getOneById(Model model, @PathVariable("id") int id, CommentForm commentForm) {
        Optional<Post> result = postService.findPostById(id);
        if(result.isPresent()) {
            if(result.get().getRelatedComments() != null) {
                result.get().getRelatedComments().sort(new Comparator<Comment>() {
                    @Override
                    public int compare(Comment o1, Comment o2) {
                        if(o1.getId() < o2.getId())
                            return -1;
                        else if(o1.getId() > o2.getId())
                            return 1;
                        else return 0;
                    }
                });
            }
            model.addAttribute("post", result.get());
            return "post.html";
        }
        return ResponseEntity.status(404).toString();
        //return ResponseEntity.status(404).toString();
        //return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{keyword}")
    public String getAllLike(Model model, @PathVariable String keyword) {
        List<Post> postList =  postService.findPostByTitleOrContent(keyword);
//        System.out.println(postList);
        model.addAttribute("posts", postList);
        return "posts.html";
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable int id) {
        postService.deletePostById(id);
        return "redirect:/api/v1/posts";
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Post> deleteAll() {
        postService.deleteAllPosts();
        return ResponseEntity.noContent().build();
    }
}

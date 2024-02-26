package org.forumdemo.ftest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.forumdemo.ftest.entities.Comment;
import org.forumdemo.ftest.entities.ForumUser;
import org.forumdemo.ftest.entities.Post;
import org.forumdemo.ftest.service.CommentService;
import org.forumdemo.ftest.service.ForumUserService;
import org.forumdemo.ftest.service.PostService;
import org.forumdemo.ftest.validation.CommentEditForm;
import org.forumdemo.ftest.validation.CommentForm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;
    private final ForumUserService forumUserService;

    @PostMapping("/api/v1/posts/{id}/new_comment")
    public String createComment(Model model, @PathVariable("id") int id,
                                @Valid CommentForm commentForm, BindingResult bindingResult,
                                Principal principal) throws Exception {
        Post post = null;
        if(postService.findPostById(id).isPresent())
            post = postService.findPostById(id).get();
        if(post.getRelatedComments() != null) {
            post.getRelatedComments().sort(new Comparator<Comment>() {
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
        ForumUser author = this.forumUserService.getUser(principal.getName());
        if(bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post.html";
        }
        Comment comment = commentService.addComment(post, commentForm.getCommentText(), author);

        return String.format("redirect:/api/v1/posts/%d", id);
    }
    @PostMapping("api/v1/posts/{pid}/comments/{cid}/edit")
    public String editComment(@Valid CommentForm commentForm,
                              BindingResult bindingResult,
                              @PathVariable("pid") int pid,
                              @PathVariable("cid") int cid,
                              Model model
                              ) {
        Post post = this.postService.findPostById(pid).get();
        post.getRelatedComments().sort(new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                if(o1.getId() < o2.getId())
                    return -1;
                else if(o1.getId() > o2.getId())
                    return 1;
                else return 0;
            }
        });
        if(bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post.html";
        }
        Comment comment = this.commentService.getComment(cid);

        this.commentService.modifyComment(comment, commentForm.getCommentText());
        commentForm.setCommentText("");
        model.addAttribute("post", post);

        return "post.html";
    }

    @GetMapping("/api/v1/posts/{pid}/comments/{cid}/delete")
    public String removeComment(Model model,
                                @PathVariable("pid") int pid,
                                @PathVariable("cid") int cid,
                                CommentForm commentForm,
                                BindingResult bindingResult
                                ) {

        this.commentService.deleteComment(this.commentService.getComment(cid).getId());
        model.addAttribute("post", this.postService.findPostById(pid).get());
        return "post.html";
    }
}

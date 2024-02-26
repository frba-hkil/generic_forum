package org.forumdemo.ftest.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Comment implements Comparable<Comment>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "text")
    private String content;
    private LocalDate commentTime;
    @ManyToOne
    private Post relatedPost;
    @ManyToOne
    private ForumUser author;
    private LocalDateTime lastModifyTime;

    public Comment() {
    }

    public Comment(int id, String content, LocalDate commentTime, Post relatedPost) {
        this.id = id;
        this.content = content;
        this.commentTime = commentTime;
        this.relatedPost = relatedPost;
    }

    @Override
    public int compareTo(Comment comment) {
        if(this.id < comment.getId())
            return -1;
        else if(this.id > comment.getId())
            return 1;
        else return 0;
    }
}

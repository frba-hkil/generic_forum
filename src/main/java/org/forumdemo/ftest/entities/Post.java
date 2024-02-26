package org.forumdemo.ftest.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 32)
    private String title;
    @Column(columnDefinition = "text")
    private String content;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDateTime postTime;
    @OneToMany(mappedBy = "relatedPost", cascade = CascadeType.REMOVE)
    private List<Comment> relatedComments;
    @ManyToOne
    private ForumUser author;
    private LocalDateTime lastModifyTime;
    @ManyToMany
    private Set<ForumUser> upvoters;
    @ManyToMany
    private Set<ForumUser> downvoters;

    public Post() {
    }

    public Post(int id, String title, String content, LocalDateTime postTime, List<Comment> relatedComments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.relatedComments = relatedComments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", postTime=" + postTime +
                ", relatedComments=" + relatedComments +
                '}';
    }
}

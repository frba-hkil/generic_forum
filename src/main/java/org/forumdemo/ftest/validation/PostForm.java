package org.forumdemo.ftest.validation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostForm {
    @NotEmpty(message = "title can't be empty")
    @Size(max=64)
    private String titleInput;
    @NotEmpty(message = "post content can't be empty")
    private String writePost;
}

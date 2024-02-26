package org.forumdemo.ftest.validation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentEditForm {
    @NotEmpty(message = "Comment can't be empty")
    private String commentText;
}

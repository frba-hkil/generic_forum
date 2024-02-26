package org.forumdemo.ftest.validation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationForm {

    @Size(min=6, max=16, message = "Username length must be between 6 and 16 characters")
    @NotEmpty(message = "You must enter a username")
    private String username;
    @NotEmpty(message = "You must define a nickname for registration")
    private String nickname;
    @NotEmpty(message = "You must define a password")
    private String password;

    @NotEmpty(message = "You must re-enter the password")
    private String passwordDup;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "You must enter a valid email")
    private String email;
}

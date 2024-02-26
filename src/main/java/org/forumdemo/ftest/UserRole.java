package org.forumdemo.ftest;

import lombok.Getter;
import org.apache.catalina.User;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String role;

    UserRole(String role) {
        this.role = role;
    }
}

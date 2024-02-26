package org.forumdemo.ftest.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.forumdemo.ftest.entities.ForumUser;
import org.forumdemo.ftest.repository.ForumUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForumUserService {
    private final ForumUserRepository forumUserRepository;
    private final PasswordEncoder passwordEncoder;

    public ForumUser addUser(String username, String nickname, String password, String email) {
        ForumUser forumUser = new ForumUser();
        forumUser.setUsername(username);
        forumUser.setNickname(nickname);
        forumUser.setPassword(passwordEncoder.encode(password));
        forumUser.setEmail(email);
        this.forumUserRepository.save(forumUser);

        return forumUser;
    }

    public ForumUser getUser(String username) throws Exception{
        Optional<ForumUser> user = this.forumUserRepository.findByusername(username);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new Exception("Site user not found");
        }
    }
}

package org.forumdemo.ftest.service;

import lombok.RequiredArgsConstructor;
import org.forumdemo.ftest.UserRole;
import org.forumdemo.ftest.entities.ForumUser;
import org.forumdemo.ftest.repository.ForumUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
    private final ForumUserRepository forumUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ForumUser> user = this.forumUserRepository.findByusername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }
        ForumUser forumUser = user.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(forumUser.getUsername().equalsIgnoreCase("admin")) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getRole()));
        }
        else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getRole()));
        }
        return new User(forumUser.getUsername(), forumUser.getPassword(), authorities);
    }
}

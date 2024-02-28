package com.redue.newsflow.service;

import com.redue.newsflow.entities.User;
import com.redue.newsflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetail implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsernameOrEmail(username, username);

        if (user == null) {
            throw new UsernameNotFoundException("User not exists by Username");
        }
        return new CustomUserDetail(user);
    }

}

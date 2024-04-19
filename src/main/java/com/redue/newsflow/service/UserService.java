package com.redue.newsflow.service;

import com.redue.newsflow.dto.user.UserUpdateDTO;
import com.redue.newsflow.entities.User;
import com.redue.newsflow.mapper.UserMapper;
import com.redue.newsflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    public UserUpdateDTO updateUser(String username, UserUpdateDTO dto) {
        User upd = repository.findByUsernameOrEmail(username, username);
        upd.setUsername(dto.username());
        upd.setEmail(dto.email());
        return mapper.toUpdate(repository.save(upd));
    }
}

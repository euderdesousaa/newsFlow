package com.redue.newsflow.service;


import com.redue.newsflow.dto.SignUpDto;
import com.redue.newsflow.entities.User;
import com.redue.newsflow.enums.Roles;
import com.redue.newsflow.mapper.UserMapper;
import com.redue.newsflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public SignUpDto registerUser(@RequestBody SignUpDto dto) {
        User user = repository.save(mapper.toInsertDto(dto));
        user.setRoles(Roles.USER);
        user.setPassword(passwordEncoder.encode(dto.password()));
        return mapper.toEntityInsert(user);
    }
}

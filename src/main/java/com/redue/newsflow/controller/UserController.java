package com.redue.newsflow.controller;

import com.redue.newsflow.dto.user.UserUpdateDTO;
import com.redue.newsflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public String hello(){
        return "Hello World";
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserUpdateDTO> updateDto(@PathVariable String username,
                                                   @RequestBody UserUpdateDTO dto){
        service.updateUser(username, dto);
        return ResponseEntity.ok().body(dto);
    }
}

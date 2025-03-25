package com.redue.newsflow.controller;

import com.redue.newsflow.dto.LoginDto;
import com.redue.newsflow.dto.LoginResponseDTO;
import com.redue.newsflow.dto.SignUpDto;
import com.redue.newsflow.security.jwt.JwtUtils;
import com.redue.newsflow.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Value("${redue.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @PostMapping("/signup")
    public ResponseEntity<SignUpDto> registerUser(@Valid @RequestBody SignUpDto dto,
                                                  HttpServletRequest request) {
        SignUpDto sign = service.registerUser(dto);
        return ResponseEntity.ok(sign);
    }

    @PostMapping("/login")
    public LoginResponseDTO authenticateUser(@RequestBody LoginDto loginDto, HttpServletResponse response,
                                             HttpServletRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        ResponseCookie cookie = ResponseCookie.from("accessToken", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtExpirationMs)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return LoginResponseDTO.builder().
                jwt(jwt).
                build();
    }

}



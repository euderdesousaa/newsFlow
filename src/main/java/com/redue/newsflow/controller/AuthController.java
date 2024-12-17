package com.redue.newsflow.controller;

import com.maxmind.geoip2.exception.GeoIp2Exception;
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

import java.io.IOException;

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
                                                  HttpServletRequest request) throws IOException, GeoIp2Exception {
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
        getClientIPv4(request);
        return LoginResponseDTO.builder().
                jwt(jwt).
                ipAddress(getClientIPv4(request)).
                build();
    }

    private String getClientIPv4(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader != null && !xfHeader.isEmpty()) {
            // Processa a lista de IPs no cabeçalho X-Forwarded-For
            String[] ipAddresses = xfHeader.split(",");
            for (String ip : ipAddresses) {
                ip = ip.trim();
                if (isValidIPv4(ip) && !isPrivateOrLoopback(ip)) {
                    return ip;
                }
            }
        }

        // Fallback para request.getRemoteAddr()
        String remoteIp = request.getRemoteAddr();

        if (isValidIPv4(remoteIp) && !isPrivateOrLoopback(remoteIp)) {
            return remoteIp;
        }

        // Caso nenhum IP válido seja encontrado
        return "IP não disponível";
    }

    // Valida se é um endereço IPv4 válido
    private boolean isValidIPv4(String ip) {
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ip.matches(ipv4Pattern);
    }

    // Verifica se o IP é loopback ou privado
    private boolean isPrivateOrLoopback(String ip) {
        return ip.startsWith("10.") ||                // Faixa privada
                ip.startsWith("192.168.") ||          // Faixa privada
                (ip.startsWith("172.") && is172Private(ip)) || // Faixa privada 172.16.x.x a 172.31.x.x
                ip.equals("127.0.0.1");               // Loopback
    }

    // Verifica se um IP na faixa 172.x.x.x é privado
    private boolean is172Private(String ip) {
        String[] parts = ip.split("\\.");
        int secondOctet = Integer.parseInt(parts[1]);
        return secondOctet >= 16 && secondOctet <= 31;
    }

}



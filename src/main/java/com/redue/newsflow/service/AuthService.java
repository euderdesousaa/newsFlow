package com.redue.newsflow.service;

import com.redue.newsflow.dto.GeoLocationDTO;
import com.redue.newsflow.dto.SignUpDto;
import com.redue.newsflow.entities.User;
import com.redue.newsflow.entities.UserLocation;
import com.redue.newsflow.enums.Roles;
import com.redue.newsflow.mapper.UserMapper;
import com.redue.newsflow.repositories.UserLocationRepository;
import com.redue.newsflow.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final UserLocationRepository userLocationRepo;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final GeoLocationService geoLocationService;

    public SignUpDto registerUser(SignUpDto dto, HttpServletRequest request) {
        User user = mapper.toEntityInsert(dto);
        user.setRoles(Roles.USER);
        user.setPassword(passwordEncoder.encode(dto.password()));
        User savedUser = repository.save(user);
        user.setLocation(addUserLocation(dto, savedUser, request));
        return mapper.toInsertDto(savedUser);
    }

   /* public UserLocation addUserLocation(SignUpDto dto, User user) throws IOException, GeoIp2Exception {
        String sameIp = "45.234.138.97";
        InetAddress ipAddress = InetAddress.getByName(sameIp);
        String country = databaseReader.country(ipAddress).getCountry().getName();
        UserLocation loc = mapper.toLocationEntity(dto);
        loc.setCountry(country);
        loc.setUser(user);
        loc.setIsoCode(findUserByIso(ipAddress));
        loc.setEnabled(true);
        return userLocationRepo.save(loc);
    }
    */

    public UserLocation addUserLocation(SignUpDto dto, User user, HttpServletRequest request) {
        try {
            String clientIp = getClientIPv4(request);
            if ("IP não disponível".equals(clientIp)) {
                throw new RuntimeException("IP do cliente não pôde ser determinado.");
            }

            GeoLocationDTO locationDTO = geoLocationService.fetchGeoLocation(clientIp);

            UserLocation loc = mapper.toLocationEntity(dto);
            loc.setCountry(locationDTO.getCountry());
            loc.setIsoCode(locationDTO.getCountry_code());
            loc.setUser(user);
            loc.setEnabled(true);

            return userLocationRepo.save(loc);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar localização do usuário: " + e.getMessage(), e);
        }
    }

    private String getClientIPv4(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader != null && !xfHeader.isEmpty()) {
            String[] ipAddresses = xfHeader.split(",");
            for (String ip : ipAddresses) {
                ip = ip.trim();
                if (isValidIPv4(ip) && !isPrivateOrLoopback(ip)) {
                    return ip;
                }
            }
        }

        String remoteIp = request.getRemoteAddr();

        if (isValidIPv4(remoteIp) && !isPrivateOrLoopback(remoteIp)) {
            return remoteIp;
        }

        return "IP não disponível";
    }

    private boolean isValidIPv4(String ip) {
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ip.matches(ipv4Pattern);
    }

    private boolean isPrivateOrLoopback(String ip) {
        return ip.startsWith("10.") ||
                ip.startsWith("192.168.") ||
                (ip.startsWith("172.") && is172Private(ip)) ||
                ip.equals("127.0.0.1");
    }

    private boolean is172Private(String ip) {
        String[] parts = ip.split("\\.");
        int secondOctet = Integer.parseInt(parts[1]);
        return secondOctet >= 16 && secondOctet <= 31;
    }
}

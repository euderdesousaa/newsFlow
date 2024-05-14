package com.redue.newsflow.service;


import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.redue.newsflow.dto.SignUpDto;
import com.redue.newsflow.entities.User;
import com.redue.newsflow.entities.UserLocation;
import com.redue.newsflow.enums.Roles;
import com.redue.newsflow.mapper.UserMapper;
import com.redue.newsflow.repositories.UserLocationRepository;
import com.redue.newsflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final UserLocationRepository userLocationRepo;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final DatabaseReader databaseReader;

    public SignUpDto registerUser(SignUpDto dto) throws IOException, GeoIp2Exception {
        User user = mapper.toEntityInsert(dto);
        user.setRoles(Roles.USER);
        user.setPassword(passwordEncoder.encode(dto.password()));
        User savedUser = repository.save(user);
        user.setLocation(addUserLocation(dto, savedUser));
        return mapper.toInsertDto(savedUser);
    }

    public UserLocation addUserLocation(SignUpDto dto, User user) throws IOException, GeoIp2Exception {
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

    private String findUserByIso(InetAddress ipAddress) {
        try {
            CityResponse response = getCityResponse(ipAddress);
            Country country = response.getCountry();
            return country.getIsoCode();
        } catch (IOException | GeoIp2Exception e) {
            return null;
        }
    }

    private CityResponse getCityResponse(InetAddress address) throws IOException, GeoIp2Exception {
        return databaseReader.city(address);
    }
}

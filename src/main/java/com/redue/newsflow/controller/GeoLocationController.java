package com.redue.newsflow.controller;

import com.redue.newsflow.dto.GeoLocationDTO;
import com.redue.newsflow.service.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/geolocation")
public class GeoLocationController {
    
    private final GeoLocationService geoLocationService;
    
    @GetMapping("/{ip}")
    public ResponseEntity<GeoLocationDTO> getGeoLocation(@PathVariable String ip) {
        try {
            GeoLocationDTO response = geoLocationService.fetchGeoLocation(ip);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

package com.redue.newsflow.config;

import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class GeoLocationConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    public DatabaseReader getDatabaseReader() {
        try {
            log.info("GeoLocationConfig: Trying to load GeoLite2-Country database...");
            Resource resource = resourceLoader.getResource("classpath:maxmind/GeoLite2-City.mmdb");
            InputStream inputStream = resource.getInputStream();
            log.info("GeoLocationConfig: GeoLite2-Country database successfully loaded.");

            return new DatabaseReader
                    .Builder(inputStream)
                    .fileMode(Reader.FileMode.MEMORY)
                    .build();
        } catch (IOException | NullPointerException e) {
            log.error("Database reader could not be loaded.", e);
            return null;
        }
    }
}

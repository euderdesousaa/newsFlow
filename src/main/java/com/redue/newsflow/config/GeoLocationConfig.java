package com.redue.newsflow.config;

import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class GeoLocationConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    public synchronized DatabaseReader databaseReader() {
        try {
            log.info("GeoLocationConfig: Trying to load GeoLite2-City database...");

            // Use FileSystemResource para acessar diretamente o arquivo no sistema
            FileSystemResource resource = new FileSystemResource("src/main/resources/maxmind/GeoLite2-City.mmdb");

            if (!resource.exists()) {
                throw new FileNotFoundException("GeoLite2-City.mmdb could not be found at the specified path.");
            }

            log.info("GeoLocationConfig: GeoLite2-City database successfully loaded.");

            // Use FileMode.MEMORY_MAPPED com arquivos acessados diretamente
            return new DatabaseReader.Builder(resource.getFile())
                    .fileMode(Reader.FileMode.MEMORY_MAPPED)
                    .withCache(new CHMCache())
                    .build();
        } catch (IOException e) {
            log.error("Database reader could not be loaded.", e);
            return null;
        }
    }


}

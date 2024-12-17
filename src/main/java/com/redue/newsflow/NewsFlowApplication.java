package com.redue.newsflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;

@SpringBootApplication
public class NewsFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsFlowApplication.class, args);
	}


	@Bean
	public ResourceLoader resourceLoader() {
		return new FileSystemResourceLoader();
	}
}

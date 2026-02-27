package com.featureflag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FeatureFlagServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeatureFlagServiceApplication.class, args);
	}

}

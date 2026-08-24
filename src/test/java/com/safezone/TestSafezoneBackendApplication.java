package com.safezone;

import org.springframework.boot.SpringApplication;

public class TestSafezoneBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(SafezoneBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

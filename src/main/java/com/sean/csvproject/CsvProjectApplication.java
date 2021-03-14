package com.sean.csvproject;

import com.sean.csvproject.service.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CsvProjectApplication {
	private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

	public static void main(String[] args) {
		logger.info("CsvProjectApplication main method............");
		SpringApplication.run(CsvProjectApplication.class, args);
	}
}

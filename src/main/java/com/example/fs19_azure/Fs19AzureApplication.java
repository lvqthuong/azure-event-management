package com.example.fs19_azure;

import com.example.fs19_azure.service.EventsService;
import com.example.fs19_azure.service.azure.ServiceBusConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Fs19AzureApplication {
	@Autowired
	private ServiceBusConsumerService sbcs;

	private static final Logger logger = LoggerFactory.getLogger(Fs19AzureApplication.class);

	public static void main(String[] args) {
//		ApplicationInsights.attach();
		logger.info("Starting the application");
		SpringApplication.run(Fs19AzureApplication.class, args);
	}

//	@Bean
//	CommandLineRunner run(ServiceBusConsumerService consumerService) {
//		return args -> {
//			consumerService.startProcessing();
//		};
//	}
}

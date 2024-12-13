package com.example.fs19_azure;

import com.example.fs19_azure.service.azure.ServiceBusConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Fs19AzureApplication {
	@Autowired
	private ServiceBusConsumerService sbcs;

	public static void main(String[] args) {
		SpringApplication.run(Fs19AzureApplication.class, args);
	}

	@Bean
	CommandLineRunner run(ServiceBusConsumerService consumerService) {
		return args -> {
			consumerService.startProcessing();
		};
	}

}

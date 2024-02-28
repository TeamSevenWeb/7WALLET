package com.telerikacademy.web.virtualwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class VirtualWalletApplication {

	public static void main(String[] args) {
		// Create a RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// Specify the URL to the mock API
		String apiUrl = "https://65df74a2ff5e305f32a25197.mockapi.io/api/card/withdraw";

		// Make a GET request and retrieve the response as a String
		String response = restTemplate.getForObject(apiUrl, String.class);

		// Process the response as needed
		System.out.println("Response: " + response);
//		SpringApplication.run(VirtualWalletApplication.class, args);
	}

}

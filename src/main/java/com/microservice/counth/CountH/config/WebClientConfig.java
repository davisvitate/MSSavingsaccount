package com.microservice.counth.CountH.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

	@Bean
	@Qualifier("Movement")
	public WebClient registerWebClient() {
		return WebClient.create("http://localhost:8090/api2/movement/createM");
		
	}
	
	
	@Bean
	@Qualifier("Client")
	public WebClient registerWebClien() {
		return WebClient.create("http://localhost:8090/api3/client/createC");
		
	}
}

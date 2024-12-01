package com.eteration.simplebanking;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		 SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(AccountService accountService) {
		return (args) -> {
			// Create default account
			Account defaultAccount = new Account("Kerem Karaca", "669-7788");
			accountService.save(defaultAccount);
		};
	}

}

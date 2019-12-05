package de.gx.buyresell;

import de.gx.buyresell.io.TestRestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class BuyresellApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuyresellApplication.class, args);
	}

}

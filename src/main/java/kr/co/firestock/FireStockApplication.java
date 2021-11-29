package kr.co.firestock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FireStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(FireStockApplication.class, args);
	}

}

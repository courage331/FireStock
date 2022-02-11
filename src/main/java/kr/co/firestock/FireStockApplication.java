package kr.co.firestock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EntityScan(basePackages = {"kr.co.firestock.vo"})
@EnableJpaRepositories(basePackages = {"kr.co.firestock.repository"})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class FireStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(FireStockApplication.class, args);
	}

}

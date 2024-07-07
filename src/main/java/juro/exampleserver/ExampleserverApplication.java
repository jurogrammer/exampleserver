package juro.exampleserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
public class ExampleserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleserverApplication.class, args);
	}

}

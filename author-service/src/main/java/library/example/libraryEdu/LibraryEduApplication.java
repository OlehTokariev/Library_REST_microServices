package library.example.libraryEdu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "library.example.libraryEdu.client")
public class LibraryEduApplication {
	public static void main(String[] args) {
		SpringApplication.run(LibraryEduApplication.class, args);
	}
}
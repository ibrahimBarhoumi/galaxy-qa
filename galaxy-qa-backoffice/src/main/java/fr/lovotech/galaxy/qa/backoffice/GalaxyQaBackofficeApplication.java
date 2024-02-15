package fr.lovotech.galaxy.qa.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import fr.lovotech.galaxy.qa.backoffice.repository.utils.ResourceRepositoryImpl;

@SpringBootApplication
@EnableFeignClients
@EnableMongoRepositories(repositoryBaseClass = ResourceRepositoryImpl.class)
public class GalaxyQaBackofficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GalaxyQaBackofficeApplication.class, args);
    }

    @Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/*").allowedOrigins("*");
			}
		};
	}

}

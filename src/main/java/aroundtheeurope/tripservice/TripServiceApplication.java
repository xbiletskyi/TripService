package aroundtheeurope.tripservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The main entry point for the FindRoute Spring Boot application.
 */
@SpringBootApplication
//@EntityScan(basePackages = "aroundtheeurope.tripservice.model.entity")
//@EnableJpaRepositories(basePackages = "aroundtheeurope.tripservice.repository")
public class TripServiceApplication {
    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TripServiceApplication.class, args);
    }

}

package aroundtheeurope.tripservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the FindRoute Spring Boot application.
 */
@SpringBootApplication
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

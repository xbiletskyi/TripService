package aroundtheeurope.tripservice;

import aroundtheeurope.tripservice.SideFunctions.EnvPropertyLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the FindRoute Spring Boot application.
 */
@SpringBootApplication
public class TripServiceApplication {

    // Static block to load environment properties before the application starts
    static {
        EnvPropertyLoader.loadProperties();
    }

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TripServiceApplication.class, args);
    }

}

package aroundtheeurope.tripservice.SideFunctions;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Loads environment variables from a .env file and sets them as system properties
 * so that they can be used directly in Spring Boot configurations
 */
public class EnvPropertyLoader {

    /**
     * Loads properties from the .env file and sets them as system properties.
     */
    public static void loadProperties() {
        Dotenv dotenv = Dotenv.load();
        // Map .env variables to Spring Boot's property names and set them as system properties
        System.setProperty("spring.security.user.name", dotenv.get("SPRING_SECURITY_USER_NAME"));
        System.setProperty("spring.security.user.password", dotenv.get("SPRING_SECURITY_USER_PASSWORD"));
    }
}

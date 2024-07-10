package aroundtheeurope.findroute;

import aroundtheeurope.findroute.SideFunctions.EnvPropertyLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FindRouteApplication {
    static {
        EnvPropertyLoader.loadProperties();
    }
    public static void main(String[] args) {
        SpringApplication.run(FindRouteApplication.class, args);
    }

}

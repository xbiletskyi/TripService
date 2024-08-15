package aroundtheeurope.tripservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up security policies for the application.
 * This configuration ensures that only requests from localhost are allowed,
 * providing a layer of security for applications that should only be accessed locally.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     *
     * @param http the HttpSecurity object used to configure security settings.
     * @return a SecurityFilterChain object that defines the security rules.
     * @throws Exception if there is a problem configuring the security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable Cross-Site Request Forgery (CSRF) protection since this configuration
                // is intended for local access only. CSRF protection is not necessary for non-public APIs.
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Permits all requests originating from localhost (both IPv4 and IPv6 loopback addresses).
                                .requestMatchers(request -> {
                                    String remoteAddr = request.getRemoteAddr();
                                    return "127.0.0.1".equals(remoteAddr) || "::1".equals(remoteAddr);
                                }).permitAll()
                                // Denies all other requests that do not come from localhost.
                                .anyRequest().denyAll()
                );
        return http.build();
    }
}

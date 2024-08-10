package aroundtheeurope.tripservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(request -> {
                                    String remoteAddr = request.getRemoteAddr();
                                    return "127.0.0.1".equals(remoteAddr) || "::1".equals(remoteAddr);
                                }).permitAll()
                                .anyRequest().denyAll()
                );
        return http.build();
    }
}

package tran.example.weatherforecast.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * A class to help define beans which can be used during runtime such as in the Security
 * configuration class.
 */
@Configuration
@EnableJpaRepositories("tran.example.weatherforecast.repositories")
public class CommonBeanConfig {

    /**
     * @return A password encoder using BCrypt hashing.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncryptor() {
        return new BCryptPasswordEncoder();
    }
}

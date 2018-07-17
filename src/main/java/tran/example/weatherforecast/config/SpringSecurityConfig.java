package tran.example.weatherforecast.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tran.example.weatherforecast.controllers.IndexController;
import tran.example.weatherforecast.controllers.SearchController;

/**
 * A class to set up mappings which will require users to be authenticated and have the proper
 * role(s) to view a certain view. This class also creates the necessary dependencies for
 * authenticating a user via a login form.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Field to hold a specific type of authentication implementation.
     */
    private AuthenticationProvider authenticationProvider;

    @Autowired
    @Qualifier("daoAuthenticationProvider")
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * @param encoder An object using BCrypt hashing.
     * @return A password encoder using BCrypt hashing on encrypted string(s).
     */
    @Bean
    public PasswordEncoder passwordEncoder(BCryptPasswordEncoder encoder) {
        return encoder;
    }

    /**
     * @param passwordEncoder The password encoder for the authentication implementation.
     * @param userDetailsService An object to look up the user object and convert it as necessary.
     * @return An authentication implementation that checks entered credentials for a user
     * against a database.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                               UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    /**
     * Builds an authentication implementation object.
     * @param authenticationManagerBuilder An authentication request processor.
     */
    @Autowired
    public void configureAuthManager(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }

    /**
     * @param http The object to modify the security associated with different mappings.
     * @throws Exception if the configuration is invalid.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/h2-console").disable()
                .headers().frameOptions().sameOrigin()
                .and().authorizeRequests().antMatchers("/**/favicon.ico") .permitAll()
                .and().authorizeRequests().antMatchers("/product/**").permitAll()
                .and().authorizeRequests().antMatchers("/webjars/**").permitAll()
                .and().authorizeRequests().antMatchers("/static/css").permitAll()
                .and().authorizeRequests().antMatchers("/js").permitAll()
                .and().formLogin().loginPage(IndexController.URL_PATH_SEPARATOR + IndexController
                .LOGIN_PAGE_MAPPING).permitAll()
                .and().authorizeRequests().antMatchers(IndexController.URL_PATH_SEPARATOR).permitAll()
                .and().authorizeRequests().antMatchers(SearchController.BASE_URL +
                                        SearchController.PRIOR_USER_SEARCHES_MAPPING).hasAnyAuthority("User")
                .and().exceptionHandling().accessDeniedPage(IndexController.DENIED_PAGE_MAPPING);
    }
}

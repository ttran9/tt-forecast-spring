package tran.example.weatherforecast.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import tran.example.weatherforecast.controllers.ForecastController;
import tran.example.weatherforecast.controllers.IndexController;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/h2-console").disable()
                .headers().frameOptions().sameOrigin()
                .and().authorizeRequests().antMatchers("/**/favicon.ico") .permitAll()
                .and().authorizeRequests().antMatchers("/product/**").permitAll()
                .and().authorizeRequests().antMatchers("/webjars/**").permitAll()
                .and().authorizeRequests().antMatchers("/static/css").permitAll()
                .and().authorizeRequests().antMatchers("/js").permitAll()
                .and().formLogin().loginPage(IndexController.LOGIN_PAGE_MAPPING).permitAll()
                .and().authorizeRequests().antMatchers(IndexController.URL_PATH_SEPARATOR).permitAll()
                .and().authorizeRequests().antMatchers(ForecastController.BASE_URL +
                                        IndexController.URL_PATH_SEPARATOR +
                                        IndexController.PATH_MATCHER).hasAnyAuthority("User")
                .and().exceptionHandling().accessDeniedPage(IndexController.DENIED_PAGE_MAPPING);
    }
}

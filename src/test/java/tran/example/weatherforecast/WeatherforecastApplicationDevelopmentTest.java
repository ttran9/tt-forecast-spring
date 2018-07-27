package tran.example.weatherforecast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * The below test is to test if the Bootstrap file using a Postgres database is run instead of
 * using the Bootstrap file which is targeted for databses using a create-drop strategy.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"development"})
public class WeatherforecastApplicationDevelopmentTest {

    @Test
    public void contextLoads() {
    }

}
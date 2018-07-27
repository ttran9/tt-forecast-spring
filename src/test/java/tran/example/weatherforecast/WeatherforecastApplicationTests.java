package tran.example.weatherforecast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * The below test will verify that the SpringApplication.run method can be run and returns a
 * ConfigurableApplicationContext object that is not null.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherforecastApplicationTests {

    @Test
    public void contextLoads() {
        WeatherforecastApplication.main(new String[]{});
    }

}

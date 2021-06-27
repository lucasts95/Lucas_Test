package lucas.weather;


import lucas.weather.module.RequestInfo;
import lucas.weather.service.WeatherService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWeather {

    @Autowired
    WeatherService weatherService;


    @Test
    public void testGetWeather() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setCounty("苏州");
        requestInfo.setCity("苏州");
        requestInfo.setProvince("江苏");
        try {
            Optional<Integer> weather = weatherService.getWeather(requestInfo);
        } catch (Exception e) {
            System.out.println(11);
            e.printStackTrace();
        }
    }
}

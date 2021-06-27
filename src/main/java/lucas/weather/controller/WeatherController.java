
package lucas.weather.controller;

import lucas.weather.module.RequestInfo;
import lucas.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class WeatherController {

    @Autowired
    WeatherService weatherService;


    @GetMapping("/weather")
    public Optional<Integer> getTemperature(@RequestParam("province") String province,
                                            @RequestParam("city") String city, @RequestParam("county") String county) throws Exception {

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setProvince(province);
        requestInfo.setCity(city);
        requestInfo.setCounty(county);
        return weatherService.getWeather(requestInfo);

    }


}

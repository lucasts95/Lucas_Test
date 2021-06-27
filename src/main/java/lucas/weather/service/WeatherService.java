package lucas.weather.service;

import lucas.weather.module.RequestInfo;

import java.util.Optional;

public interface WeatherService {
    Optional<Integer> getWeather(RequestInfo requestInfo) throws Exception;
}

package lucas.weather.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lucas.weather.module.RequestInfo;
import lucas.weather.module.WeatherInfo;
import lucas.weather.service.RetryService;
import lucas.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Resource
    RetryService retryService;

    @Override
    public Optional<Integer> getWeather(RequestInfo requestInfo) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        ResponseEntity<String> provinceRes = restTemplate.exchange("http://www.weather.com" +
                ".cn/data/city3jdata/china" +
                ".html", HttpMethod.GET, new HttpEntity<String>(headers), String.class);

        if (logic(requestInfo.getProvince(), requestInfo, provinceRes, "pro")) return Optional.of(0);
        ResponseEntity<String> cityRes =
                restTemplate.exchange("http://www.weather.com.cn/data/city3jdata/provshi/" + requestInfo.getProvinceCode() +
                        ".html", HttpMethod.GET, null, String.class);
        if (logic(requestInfo.getCity(), requestInfo, cityRes, "city")) return Optional.of(0);
        ResponseEntity<String> countyRes =
                restTemplate.exchange("http://www.weather.com.cn/data/city3jdata/station/" +
                        requestInfo.getProvinceCode() + requestInfo.getCityCode() +
                        ".html", HttpMethod.GET, null, String.class);
        if (logic(requestInfo.getCounty(), requestInfo, countyRes, "")) return Optional.of(0);
        ResponseEntity<String> tempRes =
                restTemplate.exchange("http://www.weather.com.cn/data/sk/" +
                        requestInfo.getProvinceCode() + requestInfo.getCityCode() + requestInfo.getCountyCode() +
                        ".html", HttpMethod.GET, null, String.class);
        Map<String, Object> map = parseJSON2Map(tempRes.getBody());
        WeatherInfo weatherInfo = new WeatherInfo();
        if (map.containsKey("weatherinfo")) {
            JSONObject weatherJson = JSON.parseObject(map.get("weatherinfo").toString());
            weatherInfo.setTemp(weatherJson.getString("temp"));
        } else {
            return Optional.of(0);
        }
        Integer round = Math.toIntExact(Math.round(Double.parseDouble(weatherInfo.getTemp())));
        return Optional.of(round);
    }

    private boolean logic(String province, RequestInfo requestInfo,
                          ResponseEntity<String> provinceRes, String location) throws Exception {
        if (provinceRes.getStatusCodeValue() != 200) {
            retryService.retry(0);
        }
        Map<String, Object> map = parseJSON2Map(provinceRes.getBody());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (Objects.equals(province, new String(entry.getValue().toString().getBytes(StandardCharsets.ISO_8859_1)
                    , StandardCharsets.UTF_8))) {
                switch (location) {
                    case "pro":
                        requestInfo.setProvinceCode(entry.getKey());
                        return false;
                    case "city":
                        requestInfo.setCityCode(entry.getKey());
                        return false;
                    default:
                        requestInfo.setCountyCode(entry.getKey());
                        return false;
                }
            }
        }
        return true;
    }

    private Map<String, Object> parseJSON2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        JSONObject json = JSONObject.parseObject(jsonStr);
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<>();
                for (Object o : (JSONArray) v) {
                    JSONObject json2 = (JSONObject) o;
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

}

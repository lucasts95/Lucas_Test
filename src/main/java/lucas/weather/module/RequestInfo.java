package lucas.weather.module;

import lombok.Data;

@Data
public class RequestInfo {
    private String province;

    private String city;

    private String county;

    private String provinceCode;

    private String cityCode;

    private String countyCode;
}

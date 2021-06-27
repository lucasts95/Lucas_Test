package lucas.weather.module;

import lombok.Data;
import sun.awt.SunGraphicsCallback;

@Data
public class WeatherInfo {
    /**
     * {"weatherinfo":{"city":"苏州","cityid":"101190401","temp":"23.9","WD":"东北风","WS":"小于3级","SD":"79%","AP":"1004
     * .9hPa","njd":"暂无实况","WSE":"
     * <3","time":"18:00","sm":"1.5","isRadar":"0","Radar":""}}
     */
    private String city;
    private String cityid;
    private String temp;
    private String WD;
    private String WS;
    private String SD;
    private String AP;
    private String njd;
    private String WSE;
    private String time;
    private String sm;
    private String isRadar;
    private String Radar;
}

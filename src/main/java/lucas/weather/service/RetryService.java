package lucas.weather.service;

public interface RetryService {

    void retry(int code) throws Exception;
}

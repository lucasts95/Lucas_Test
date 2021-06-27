package lucas.weather.service.impl;

import lucas.weather.service.RetryService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service

public class RetryServiceImpl implements RetryService {

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void retry(int code) throws Exception {
        System.out.println("dignifiedTest被调用,时间：" + LocalTime.now());
        if (code == 0) {
            throw new Exception("重试");
        }
    }
}

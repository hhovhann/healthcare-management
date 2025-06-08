package am.hhovhann.healthcare_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretKeyConfiguration {

    @Value("${app.token.secret.key}")
    private String secretKey;

    @Value("${app.token.secret.expiration}")
    private Long secretExpiration;

    public String getSecretKey() {
        return secretKey;
    }

    public Long getSecretKeyExpiration() {
        return secretExpiration;
    }
}

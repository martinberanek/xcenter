package cz.martinberanek.xcenter.xcenter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "xcenter")
public class XCenterConfiguration {

    private String url;
    private String apiId;
    private String deviceId;

    private String username;
    private String password;

}

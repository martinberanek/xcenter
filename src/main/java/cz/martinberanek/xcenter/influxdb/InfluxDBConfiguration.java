package cz.martinberanek.xcenter.influxdb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "influxdb")
public class InfluxDBConfiguration {
    private String url;
    private String organization;
    private String bucket;
    private String token;
}

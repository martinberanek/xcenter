package cz.martinberanek.xcenter;

import com.influxdb.client.write.Point;
import cz.martinberanek.xcenter.influxdb.InfluxDBService;
import cz.martinberanek.xcenter.xcenter.XCenterClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricsJob {

    private final XCenterClient xCenterService;
    private final InfluxDBService influxDBService;
    private final MetricsConfigService metricsConfigService;

    @Scheduled(fixedRate = 30000)
    public void pump() {
        Map<String, Object> data = xCenterService.getData(metricsConfigService.getDatapointConfigIds());
        Map<String, Point> points = new HashMap<>();
        for (String key : data.keySet()) {
            MetricsConfig configuration = metricsConfigService.getConfiguration(key);
            Object value = data.get(key);
            points.computeIfAbsent(configuration.getMeasurement(), measurement -> Point.measurement(measurement))
                    .addFields(Map.of(configuration.getFiled(), configuration.getValueFunction().apply(value)));
        }
        influxDBService.write(points.values());
    }

}

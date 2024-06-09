package cz.martinberanek.xcenter.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;

@Service
public class InfluxDBService {

    private static final Logger log = LoggerFactory.getLogger(InfluxDBService.class);

    private final InfluxDBConfiguration configuration;
    private final InfluxDBClient client;

    public InfluxDBService(InfluxDBConfiguration configuration) {
        this.configuration = configuration;
        this.client = InfluxDBClientFactory.create(configuration.getUrl(), configuration.getToken().toCharArray());
    }

    public void write(Collection<Point> points) {
        Instant now = Instant.now();
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writePoints(configuration.getBucket(), configuration.getOrganization(), points.stream().map(p -> p.time(now, WritePrecision.S)).toList());
        points.forEach(p -> log.debug(p.toLineProtocol()));
    }

}

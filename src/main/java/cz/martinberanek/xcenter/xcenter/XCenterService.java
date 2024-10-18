package cz.martinberanek.xcenter.xcenter;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class XCenterService {

    private final XCenterConfiguration configuration;
    private final XCenterAuthenticationService authenticationService;

    private final RestClient restClient;

    public XCenterService(XCenterAuthenticationService authenticationService, XCenterConfiguration configuration) {
        this.configuration = configuration;
        this.authenticationService = authenticationService;
        restClient = RestClient.builder()
                .baseUrl(configuration.getUrl())
                .defaultStatusHandler((status) -> status.isError(), (req, res) -> {
                    authenticationService.wipe();
                    throw new RestClientException(res.getStatusCode().toString() + " " + res.getStatusText());
                })
                .build();
    }

    public Map<String, Object> getData(Collection<String> datapointConfigIds) {
        List<Map> list = (List<Map>) restClient.post()
                .uri("Datapoint/ReadValues/{uid}", configuration.getApiId())
                .header("Cookie", authenticationService.getCookies())
                .body(Map.of("DatapointValues", datapointConfigIds.stream().map(datapointConfigId -> Map.of("DeviceId", configuration.getDeviceId(), "DatapointConfigId", datapointConfigId)).toList()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class)
                .getOrDefault("ResponseData", Collections.emptyList());
        return list.stream().collect(Collectors.toMap(m -> (String) m.get("DatapointConfigId"), m -> m.get("Value")));
    }

    public boolean getSceneEnabled(String sceneId) {
        List<Map> list = (List<Map>) restClient.get()
                .uri("Scene/GetScenesOverview/{uid}", configuration.getApiId())
                .header("Cookie", authenticationService.getCookies())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class)
                .getOrDefault("ResponseData", Collections.emptyList());
        return list.stream().filter(i->sceneId.equals(i.get("SceneId"))).map(i->(Boolean) i.get("Enabled")).findFirst().orElse(false);
    }

    public void setSceneEnabled(String sceneId, boolean value) {
        restClient.post()
                .uri("Scene/UpdateSceneSettings/{uid}", configuration.getApiId())
                .header("Cookie", authenticationService.getCookies())
                .body(Map.of("Settings", List.of(Map.of("SceneId", sceneId, "Enabled", value))))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);
    }



}

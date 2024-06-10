package cz.martinberanek.xcenter;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class MetricsConfigService {

    private static final Function MIXER_FUNCTION = (value) ->
            switch ((Integer) value) {
                case 0:
                    yield 100;
                case 1:
                    yield 75;
                case 2:
                    yield 50;
                case 3:
                    yield 25;
                default:
                    yield 0;
            };

    private static final Function<Object, Object> PUMP_STATE_FUNCTION = (value) ->
            switch ((Integer) value) {
                case 1:
                    yield "heating";
                case 2:
                    yield "cooling";
                case 3, 4, 5, 6, 7:
                    yield "blocked";
                case 8:
                    yield "stop";
                case 9:
                    yield "maintenance";
                case 10:
                    yield "antifrozz";
                case 11:
                    yield "manual on";
                case 12:
                    yield "manual off";
                default:
                    yield "unknown";
            };

    private final Map<String, MetricsConfig> items = new HashMap<>();

    public MetricsConfigService() {
        register("777c1a8e-ec1c-4a15-9bcc-4ec5b8e0e4f4", "outdoor", "temp[°C]");
        register("c2d20aa6-8dd4-4513-a3fa-a45ba942b3ee", "electricity", "hdo");
        register("88696e86-50ce-4bb6-bb9d-8e6d4ae814f8", "primary_circuit", "temp_in[°C]");
        register("b7b3f408-c605-4209-b993-02e002503f10", "primary_circuit", "temp_out[°C]");
        register("6a7c2b23-341f-49f1-b3cb-49b99d51f266", "primary_circuit", "flow[l/min]");
        register("7b290829-d996-4fa4-b15c-88d712a79ef7", "primary_circuit", "temp_evap[°C]");
        register("6ca1372b-894d-4f27-add3-257fff9905c1", "heatpump", "temp_in[°C]");
        register("6576ccc5-048a-482e-ac0d-ef4dc0de16c4", "heatpump", "temp_out[°C]");
        register("01860ced-3dd2-4ab2-9cd8-da0e7cde597e", "heatpump", "flow[l/min]");
        register("244f5e1d-33ca-4e79-8360-414e69c16a7f", "circuit1", "pump_state", PUMP_STATE_FUNCTION);
        register("4e53d1c7-f461-4e00-ad71-2e0375be8e0c", "circuit1", "temp[°C]");
        register("25447684-1ff2-4d35-8c5f-919120d36c02", "circuit1", "mixer[%]", MIXER_FUNCTION);
        register("595cb934-dbc7-4692-9c77-f276dea68501", "circuit2", "pump_state", PUMP_STATE_FUNCTION);
        register("cf6fda09-6e9d-4477-b643-4839c4cc646f", "circuit2", "temp[°C]");
        register("b656e4da-6d0e-4204-b6db-67270eaa5e90", "circuit2", "mixer[%]", MIXER_FUNCTION);
        register("fc1c59db-33d8-41f4-afb9-0513d18e8095", "accumulation", "temp[°C]");
        register("c8797976-59ee-45f3-856a-5aec3693736c", "accumulation", "temp_req[°C]");
        register("83a34595-924a-421e-b9c1-44c2a49f97ad", "boiler", "temp[°C]");
        register("ca4dd370-2cd7-4a6b-b091-f9df74150265", "boiler", "temp_req[°C]");
        register("83049eb3-7f02-4032-98e4-7b39dfc9252d", "boiler", "temp_min[°C]");
        register("1e107669-d310-43f4-9840-22539ff1798d", "heating", "power_out[kWh]");
        register("dbf925c9-f24e-456c-ac49-f7702adeb9d1", "heating", "power_in[kWh]");
        register("bb3379db-ec1a-4b37-b423-f73f35df41a7", "heating", "time[min]");
        register("54dade80-c7cd-443c-8726-d5a6ba2b73c5", "dhw", "power_out[kWh]");
        register("b94586b8-1a4c-4c4f-b56c-07895cb71a89", "dhw", "power_in[kWh]");
        register("adcdaef6-b2a1-4031-a77c-355563819697", "dhw", "time[min]");
    }

    private void register(String datapointConfigId, String measurement, String field) {
        register(datapointConfigId, measurement, field, Function.identity());
    }

    private void register(String datapointConfigId, String measurement, String filed, Function<Object, Object> valueFunction) {
        if (this.items.containsKey(datapointConfigId)) {
            throw new IllegalArgumentException("Configuration for datapointConfigId " + datapointConfigId + " already exists");
        }
        this.items.put(datapointConfigId, new MetricsConfig(measurement, filed, valueFunction));
    }

    public Collection<String> getDatapointConfigIds() {
        return items.keySet();
    }

    public MetricsConfig getConfiguration(String datapointConfigId) {
        return items.get(datapointConfigId);
    }

}

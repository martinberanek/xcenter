package cz.martinberanek.xcenter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
@Getter
public class MetricsConfig
{
    private final String measurement;
    private final String filed;
    private final Function<Object, Object> valueFunction;
}

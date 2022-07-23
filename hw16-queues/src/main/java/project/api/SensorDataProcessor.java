package project.api;

import project.api.model.SensorData;

public interface SensorDataProcessor {
    void process(SensorData data);

    default void onProcessingEnd() {
    }
}

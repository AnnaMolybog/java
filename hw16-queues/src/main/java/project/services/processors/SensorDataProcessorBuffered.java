package project.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.api.SensorDataProcessor;
import project.api.model.SensorData;
import project.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);
    public static final int POLLING_TIMEOUT = 5;

    private final int bufferSize;

    private final SensorDataBufferedWriter writer;

    PriorityBlockingQueue<SensorData> dataBuffer;

    List<SensorData> bufferedData = new ArrayList<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        dataBuffer = new PriorityBlockingQueue<>(bufferSize, new Comparator<SensorData>() {
            @Override
            public int compare(SensorData o1, SensorData o2) {
                return o1.getMeasurementTime().compareTo(o2.getMeasurementTime());
            }
        });
    }

    @Override
    public void process(SensorData data) {
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
        dataBuffer.put(data);
    }

    public void flush() {
        try {
            while (!dataBuffer.isEmpty()) {
                dataBuffer.drainTo(bufferedData);
                writer.writeBufferedData(bufferedData);
                bufferedData = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}

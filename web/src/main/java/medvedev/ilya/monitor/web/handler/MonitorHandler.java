package medvedev.ilya.monitor.web.handler;

import medvedev.ilya.monitor.proto.Protobuf.SensorValue;
import medvedev.ilya.monitor.proto.Protobuf.SensorValue.Builder;
import medvedev.ilya.monitor.sensor.Sensor;
import medvedev.ilya.monitor.sensor.cpu.Cpu;
import medvedev.ilya.monitor.sensor.mem.Mem;
import medvedev.ilya.monitor.util.ExceptionHandler;
import medvedev.ilya.monitor.web.session.AsyncSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MonitorHandler extends AbstractWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorHandler.class);

    private final Sensor[] sensors;

    private final Map<WebSocketSession, AsyncSession> sessions = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Runnable runnable;
    private ScheduledFuture scheduledFuture;

    public MonitorHandler() {
        final Cpu cpu = Cpu.byFile();
        final Mem mem = Mem.byFile();

        sensors = new Sensor[] {cpu, mem};

        runnable = ExceptionHandler.runnableHandler(this::sendStats, LOGGER);
    }

    @Override
    public synchronized void afterConnectionEstablished(final WebSocketSession session) {
        if (sessions.isEmpty()) {
            scheduledFuture = executorService.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
        }

        final AsyncSession asyncSession = new AsyncSession(session);

        sessions.put(session, asyncSession);
    }

    private void sendStats() {
        final long time = System.currentTimeMillis();

        Arrays.stream(sensors)
                .parallel()
                .unordered()
                .flatMap(Sensor::sensorValue)
                .map(builder -> builder.setTime(time))
                .map(Builder::build)
                .map(SensorValue::toByteArray)
                .forEach(bytes -> sessions.values()
                        .parallelStream()
                        .unordered()
                        .forEach(session -> session.send(bytes)));
    }

    @Override
    public synchronized void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        sessions.remove(session);

        if (sessions.isEmpty()) {
            scheduledFuture.cancel(true);

            Arrays.stream(sensors)
                    .parallel()
                    .unordered()
                    .forEach(Sensor::clear);
        }
    }
}

package medvedev.ilya.monitor.web.config;

import medvedev.ilya.monitor.sensor.Sensor;
import medvedev.ilya.monitor.sensor.impl.cpu.Cpu;
import medvedev.ilya.monitor.sensor.impl.disk.Disk;
import medvedev.ilya.monitor.sensor.impl.mem.Mem;
import medvedev.ilya.monitor.sensor.impl.net.Net;
import medvedev.ilya.monitor.web.handler.Handler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;

@Configuration
public class HandlerConfig {
    @Bean
    public WebSocketHandler webSocketHandler(
            final Cpu cpu,
            final Mem mem,
            final Disk disk,
            final Net net,
            @Value("${monitor.period}") final byte period
    ) {
        final Sensor[] sensors = new Sensor[] {cpu, mem, disk, net};

        return new Handler(sensors, period);
    }
}

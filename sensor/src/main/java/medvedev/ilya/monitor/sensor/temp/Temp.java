package medvedev.ilya.monitor.sensor.temp;

import medvedev.ilya.monitor.proto.Protobuf.SensorValue.Builder;
import medvedev.ilya.monitor.sensor.Sensor;

import java.io.File;
import java.util.stream.Stream;

public class Temp implements Sensor {
    private final File file = new File("/sys/class/thermal/thermal_zone0/temp");

    @Override
    public Stream<Builder> sensorValue() {
        return null;
    }
}

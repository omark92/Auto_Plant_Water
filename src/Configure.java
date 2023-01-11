import org.firmata4j.I2CDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.Timer;

public class Configure {
    static final byte I2C0 = 0x3C;

    public static void main(String[] args) throws IOException, InterruptedException {

        //Connecting to the grove board and getting device started
        var myUSBport = "/dev/cu.usbserial-0001";
        var device = new FirmataDevice(myUSBport);
        device.start();
        device.ensureInitializationIsDone();

        //OLED
        I2CDevice i2cObject = device.getI2CDevice((byte) 0x3C);
        SSD1306 OledDisplay = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        OledDisplay.init();

        // Moisture sensor
        var Sensor = device.getPin(15);
        Sensor.setMode(Pin.Mode.ANALOG);

        //Pump
        var pump = device.getPin(2);
        Sensor.setMode(Pin.Mode.ANALOG);

        //TimerTask
        Timer timer = new Timer();
        var task = new moistureSensorTask(OledDisplay, Sensor, timer, pump, 15);
        timer.schedule(task, 0, 10000);

    }
}
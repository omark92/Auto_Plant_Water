import org.firmata4j.Pin;
import org.firmata4j.ssd1306.MonochromeCanvas;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class moistureSensorTask extends TimerTask {
    private int duration;
    private final SSD1306 OledDisplay;
    private final Pin Sensor;
    private final Timer myTimer;
    private final Pin pump;
    private String pumping;

    public moistureSensorTask(SSD1306 display,Pin pin,Timer timer, Pin pump, int duration) {
        this.OledDisplay = display;
        this.Sensor = pin;
        this.myTimer = timer;
        this.pump = pump;
    }
    //Threshold value is 650. If the voltage detected is more than that then the pump starts watering,
    //If less than pump stops watering.
    //Stuff will be displayed on OLED


    @Override
    public void run() {

        // This is for the variable pumping. Basically what this does is that when it water is pumping,
        // It will be displayed on the OLED, same with when it stops pumping.
        if (Sensor.getValue() > 650)
        {
            pumping = "Pumping; V = "+String.valueOf(Sensor.getValue());
        } else if (Sensor.getValue() <= 650 )
        {
            pumping = "Not pumping; V = "+String.valueOf(Sensor.getValue());
        }

        //These are for OLED. These lines display the voltage value on the OLED along with the horizontal line
        OledDisplay.getCanvas().clear();
        OledDisplay.getCanvas().drawString(0, 0, pumping);
        OledDisplay.getCanvas().drawHorizontalLine(0, 17, (int) ((Sensor.getValue()) / 8), MonochromeCanvas.Color.BRIGHT);

        // Conditions of whether or not the pump should water the plant
        if (Sensor.getValue() >= 650) {
            OledDisplay.getCanvas().setTextsize(2);
            OledDisplay.display();
            try {
                pump.setValue(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (Sensor.getValue() <= 650) {
            OledDisplay.getCanvas().setTextsize(2);
            OledDisplay.display();
            try {
                pump.setValue(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

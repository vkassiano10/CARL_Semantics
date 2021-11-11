package AuxiliaryPackage;

public class AuxiliaryPackageEnumerations {

    public enum WATTAGE_EVENTS_APPLIANCE {
        // TODO ADD MORE APPLIANCES WITH THRESHOLDS HERE
        OVEN      (20,   500,"Oven"),
        DRYER     (20,   600,"Dryer"),
        TELEVISION(10,   10, "TV"),
        COFFEE_MAKER(1,   2, "Coffee Maker"),
        SHAKER(1,   2, "Shaker"),
        MICROWAVE (1000, 60, "Microwave");

        int    powerThreshold;
        int    duration;
        String deviceName;

        WATTAGE_EVENTS_APPLIANCE(int powerThreshold, int duration, String deviceName) {
            this.powerThreshold = powerThreshold;
            this.duration = duration;
            this.deviceName = deviceName;
        }

        public int getPowerThreshold() { return powerThreshold; }

        public int getDuration() { return duration; }

        public String getDeviceName() { return deviceName; }

    }
}

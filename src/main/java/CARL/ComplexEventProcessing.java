package CARL;

import AuxiliaryPackage.AuxiliaryPackageEnumerations;
import CARL.Components.FibaroEventComponent;
import CARL.Components.FibaroWattageComponent;
import CARL.Components.FitbitHourlySleepMeasurementComponent;
import CARL.Components.FitbitMinuteHeartRateMeasurementComponent;
import CARL.entities.User;
import kotlin.Pair;
import kotlin.Triple;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static AuxiliaryPackage.AuxiliaryPackageConstants.*;

public class ComplexEventProcessing {


   public static List<FibaroEventComponent> calculateWattageEvents(List<FibaroWattageComponent> userFibaroWattageList){

        List<FibaroEventComponent> fibaroEventComponents = new ArrayList<>();

        // For each appliance that is declared in the WATTAGE EVENTS APPLIANCE enumeration
        for (AuxiliaryPackageEnumerations.WATTAGE_EVENTS_APPLIANCE appliance : AuxiliaryPackageEnumerations.WATTAGE_EVENTS_APPLIANCE.values()) {

            // Select only the Fibaro wattage components that have a device id equal to the device id of the appliance under examination (e.x. TELEVISION).
            List<FibaroWattageComponent> specificApplianceFibaroWattageList = userFibaroWattageList
                    .stream()
                    .filter(i->i.getDeviceName().equalsIgnoreCase(appliance.getDeviceName()))
                    .collect(Collectors.toList());

            System.out.println("Specific Appliance Fibaro wattage list for: " + appliance);
            System.out.println(specificApplianceFibaroWattageList);
            // Create the watt series
            List<Float> wattSeries = specificApplianceFibaroWattageList
                    .stream()
                    .map(FibaroWattageComponent::getWattageValue)
                    .collect(Collectors.toList());

            System.out.println("Watt series: ");
            System.out.println(wattSeries);
            // Create the timestamp series. This series contain the timestamps of each component, in order of appearance in the userFibaroWattageList
            List<Long> timestampSeries = specificApplianceFibaroWattageList.stream()
                    .map(k-> {
                        try {
                            return convertStringTimeStampToLongTimeStamp(k.getTimestamp());
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .collect(Collectors.toList());

            List<String> timestamps = specificApplianceFibaroWattageList.stream()
                    .map(k-> k.getTimestamp())
                    .collect(Collectors.toList());
            // Process the Wattage Series.
            if(wattSeries.size()>0){
                System.out.println("Info: ");
                System.out.println(appliance);
                System.out.println(wattSeries);
                System.out.println(timestampSeries);
                System.out.println(timestamps);
                System.out.println(specificApplianceFibaroWattageList.get(0).getDeviceId());
                System.out.println(specificApplianceFibaroWattageList.get(0).getUserId());
            }


            if(wattSeries.size()>1) {
                List<FibaroEventComponent> newFibaroEventComponents = new ArrayList<>();
                newFibaroEventComponents = processWattageSeries(wattSeries, timestampSeries, timestamps, appliance, specificApplianceFibaroWattageList);
                for (FibaroEventComponent newCmp: newFibaroEventComponents
                ) {
                    fibaroEventComponents.add(newCmp);
                }
            }

        }

        System.out.println("Prin:" + fibaroEventComponents);
        return fibaroEventComponents;
    }

    /**
     * This method is responsible for converting a timestamp from String to Long format, in order to better manipulate it.
     *
     * @param timestamp The timestamp to convert, as a String value.
     *
     * @return The timestamp to return, as a Long value.
     *
     * @throws java.text.ParseException In case the parsing process goes wrong.
     */
    static long convertStringTimeStampToLongTimeStamp(String timestamp) throws java.text.ParseException {

        String timestampAsString = timestamp.replaceAll("\"", "").replace("^^<http://www.w3.org/2001/XMLSchema#dateTime>", "");
        Timestamp timeStamp = new Timestamp(UTC_FORMAT.parse(timestampAsString).getTime());
        System.out.println(timeStamp);
        System.out.println("Ttimestamp: " + timeStamp.getTime());

        return timeStamp.getTime();
    }

    static List<FibaroEventComponent> processWattageSeries(List<Float> wattSeries, List<Long> timestampSeries, List<String> timestamps, AuxiliaryPackageEnumerations.WATTAGE_EVENTS_APPLIANCE appliance, List<FibaroWattageComponent> specificApplianceFibaroWattageList){

        List<FibaroEventComponent> fibaroEventComponents = new ArrayList<>();

        int  size                = wattSeries.size();
        long startOfEvent        = INITIAL_UNKNOWN_VALUE;
        long stopOfEvent         = INITIAL_UNKNOWN_VALUE;
        int  indexOfStartOfEvent = INITIAL_UNKNOWN_VALUE;
        int  indexOfStopOfEvent  = INITIAL_UNKNOWN_VALUE;
        String startTimestamp = BLANK_STRING;
        String endTimestamp = BLANK_STRING;

        // For each entry in the watt Series list.
        for (int i = 0; i < size; i++) {

            // If the power goes up from the appliance's threshold, and this is the first time
            if (startOfEvent == INITIAL_UNKNOWN_VALUE && wattSeries.get(i) > appliance.getPowerThreshold()) {

                // Lets note the start of the event as well as its index in the list.
                startOfEvent        = timestampSeries.get(i);
                startTimestamp = timestamps.get(i);
                indexOfStartOfEvent = i;
                continue;
            }

            // If the event has started and the power goes below, and this is the first time
            if (startOfEvent != INITIAL_UNKNOWN_VALUE && wattSeries.get(i) < appliance.getPowerThreshold()) {

                // Lets note the stop of the event as well as its index in the list.
                stopOfEvent        = timestampSeries.get(i);
                endTimestamp = timestamps.get(i);
                indexOfStopOfEvent = i;

                long durationOfEvent = (stopOfEvent - startOfEvent) / 1000;

                // If the duration of the event is below the minimum threshold of the appliance under examination,
                // disregard the event and continue
                if (durationOfEvent < appliance.getDuration()) {

                    // Reset the auxiliary variables.
                    startOfEvent        = INITIAL_UNKNOWN_VALUE;
                    indexOfStartOfEvent = INITIAL_UNKNOWN_VALUE;

                    continue;
                }

                List<Float> specificEventPowerWattageSeries = wattSeries.subList(indexOfStartOfEvent, indexOfStopOfEvent + 1);
                List<Long>  specificEventTimestampSeries    = timestampSeries.subList(indexOfStartOfEvent, indexOfStopOfEvent + 1);

                float powerSum = calculateWattageSum(specificEventPowerWattageSeries, specificEventTimestampSeries);

                // TODO Add more arguments in the constructor.
                System.out.println("ADDED COMPONENT: ");
                System.out.println(appliance + " " + durationOfEvent + " " + powerSum + " " + startTimestamp + " " + endTimestamp);
                FibaroEventComponent newEvent = new FibaroEventComponent(appliance, (int) durationOfEvent, powerSum, startTimestamp, endTimestamp, specificApplianceFibaroWattageList.get(0).getUserId(), specificApplianceFibaroWattageList.get(0).getDeviceId(), specificApplianceFibaroWattageList.get(0).getRoomName(),specificApplianceFibaroWattageList.get(0).getRoomId());
                fibaroEventComponents.add(newEvent);

                // Reset the auxiliary variables.
                startOfEvent        = INITIAL_UNKNOWN_VALUE;
                indexOfStartOfEvent = INITIAL_UNKNOWN_VALUE;
            }
        }
        System.out.println("added components: " + fibaroEventComponents);
        return fibaroEventComponents;
    }

    /**
     * This method calculates the sum of the power consumption
     */
    static float calculateWattageSum(List<Float> wattSeries, List<Long> timestampSeries) {

        float powerSum = 0;

        for (int i = 0; i < wattSeries.size() - 1; i++) {

            long concurentFibaroWattageComponentsDuration = (timestampSeries.get(i+1) - timestampSeries.get(i)) / 1000;

            powerSum += concurentFibaroWattageComponentsDuration * wattSeries.get(i);
        }

        return powerSum;
    }


    public static Map<String, Integer> calculateNumberOfAsleepInNaps(List<FitbitHourlySleepMeasurementComponent> userHourlySleepArray) {

        Map<String, Integer> numberOfAsleepInNapsMap = new HashMap<>();

        int sizeOfHourlySleepArray = userHourlySleepArray.size();

        if (sizeOfHourlySleepArray == 0)
            return numberOfAsleepInNapsMap;

        String currentDateTime;

        int i = 0;

        while(i < sizeOfHourlySleepArray){
            FitbitHourlySleepMeasurementComponent currentSleepComponent = userHourlySleepArray.get(i);

            currentDateTime = currentSleepComponent.getDatetime();

            if (numberOfAsleepInNapsMap.containsKey(currentDateTime)) {
                i++;
                continue;
            }

            String finalCurrentDateTime = currentDateTime;
            int numberOfAsleepInNapsAuxiliary = (int)
                    userHourlySleepArray.stream()
                            .filter(k->k.getDatetime().equals(finalCurrentDateTime))
                            .filter(k->k.getType().equals("classic"))
                            .count();
            numberOfAsleepInNapsMap.put(currentDateTime, numberOfAsleepInNapsAuxiliary);
            i++;
        }

        return numberOfAsleepInNapsMap;
    }

    public static Map<String, Triple> calculateSleepCounts(List<FitbitHourlySleepMeasurementComponent> userHourlySleepArray){

        Map<String, Triple> userSleepCountsMap = new HashMap<>();

        int sizeOfHourlySleepArray = userHourlySleepArray.size();

        if (sizeOfHourlySleepArray == 0)
            return userSleepCountsMap;

        String timestamp;
        int i = 0;

        int countAsleep = 0, countWake = 0, countRestless = 0;
        while(i < sizeOfHourlySleepArray) {
            FitbitHourlySleepMeasurementComponent currentSleepComponent = userHourlySleepArray.get(i);

            timestamp = currentSleepComponent.getDatetime();

            if (userSleepCountsMap.containsKey(timestamp)) {
                i++;
                continue;
            }
            String finalCurrentDateTime = timestamp;
            countAsleep = (int)
                    userHourlySleepArray.stream()
                            .filter(k -> k.getDatetime().equals(finalCurrentDateTime))
                            .filter(k -> k.getLevel().equals("asleep"))
                            .count();

            countWake = (int)
                    userHourlySleepArray.stream()
                            .filter(k -> k.getDatetime().equals(finalCurrentDateTime))
                            .filter(k -> k.getLevel().equals("wake"))
                            .count();

            countRestless = (int)
                    userHourlySleepArray.stream()
                            .filter(k -> k.getDatetime().equals(finalCurrentDateTime))
                            .filter(k -> k.getLevel().equals("restless"))
                            .count();
            //NOT SURE IF THIS SHOULD BE INSIDE WHILE
            Triple counts = new Triple(countAsleep, countWake, countRestless);
            userSleepCountsMap.put(timestamp, counts);
            i++;

        }
        //populateCountsToGraphdb(userSleepCountsMap);
        return userSleepCountsMap;
    }

    public static Map<String, Boolean> calculateNapEndTimeCloseToSleepStarTime(List<FitbitHourlySleepMeasurementComponent> userHourlySleepArray) throws ParseException {

        Map<String, Boolean> napEndTimeCloseToSleepStartTime = new HashMap<>();

        int sizeOfHourlySleepArray = userHourlySleepArray.size();

        if (sizeOfHourlySleepArray == 0)
            return napEndTimeCloseToSleepStartTime;

        String timestamp;
        int i = 0;

        while(i < sizeOfHourlySleepArray) {

            FitbitHourlySleepMeasurementComponent currentSleepComponent = userHourlySleepArray.get(i);

            timestamp = currentSleepComponent.getDatetime();

            if (napEndTimeCloseToSleepStartTime.containsKey(timestamp)) {
                i++;
                continue;
            }
            // Insert false as default.
            napEndTimeCloseToSleepStartTime.put(timestamp, false);

            String finalCurrentDateTime = timestamp;
            List<FitbitHourlySleepMeasurementComponent> sameDayHourlySleepComponents =
                    userHourlySleepArray.stream()
                            .filter(k -> k.getDatetime().equals(finalCurrentDateTime))
                            .collect(Collectors.toList());

            List<FitbitHourlySleepMeasurementComponent> sameDayNaps =
                    sameDayHourlySleepComponents
                            .stream()
                            .filter(k->k.getType().equals("classic"))
                            .collect(Collectors.toList());

            List<FitbitHourlySleepMeasurementComponent> sameDaySleeps =
                    sameDayHourlySleepComponents
                            .stream()
                            .filter(k->k.getType().equals("stages"))
                            .collect(Collectors.toList());

            // For each nap in the same day naps
            for (FitbitHourlySleepMeasurementComponent nap : sameDayNaps) {

                // For each sleep in the same day sleeps
                for (FitbitHourlySleepMeasurementComponent sleep : sameDaySleeps) {

                    // If the nap's end time is close (below 7200 seconds) to a sleep's start time
                    if (convertStringTimeStampToLongTimeStamp(sleep.getStartTime()) - convertStringTimeStampToLongTimeStamp(nap.getEndTime()) < 7200) {

                        // We found it. Set the proper variable to TRUE for the specific date.
                        napEndTimeCloseToSleepStartTime.put(timestamp, true);

                        // Break the loop, no more searching is required for the particular day
                        break;
                    }
                }

                // If the condition was previously met, break
                if (napEndTimeCloseToSleepStartTime.get(timestamp))
                    break;
            }

            i++;
        }

        return napEndTimeCloseToSleepStartTime;
    }

    public static List<Triple<String, String, Integer>> processMinuteHeartRate(List<FitbitMinuteHeartRateMeasurementComponent> userMinuteHeartRateArray,
                                                                     Map<String, Pair<Integer, Integer>> userHeartRateZones){


        List<Integer> heartRateSeriesList = userMinuteHeartRateArray
                .stream()
                .map(i->Integer.valueOf(i.getResting_hr()))
                .collect(Collectors.toList());

        List<String> zonesList = heartRateSeriesList.stream().map(i-> findZone(i, userHeartRateZones)).collect(Collectors.toList());
        List<Triple<String, String, Integer>> zonesDurationList = new ArrayList<>();

        int i=0, j=0;
        for(i=0; i<zonesList.size()-1; i++){
            for(j=i+1; j<zonesList.size(); j++){
                if(zonesList.get(i).equals(zonesList.get(j))){
                    continue;
                }
                int duration = j - i;
                zonesDurationList.add(new Triple(userMinuteHeartRateArray.get(i).getDatetime(), zonesList.get(i), duration));
                break;
            }
            i=j;
        }

        i = 0;

        // This variable is going to be returned from the method, after it has been populated properly.
        List<Triple<String, String, Integer>> hrZonesDurationPerDay = new ArrayList<>();

        while(i < userMinuteHeartRateArray.size()) {

            FitbitMinuteHeartRateMeasurementComponent currentComponent = userMinuteHeartRateArray.get(i);

            hrZonesDurationPerDay.addAll(aggregateHeartRateZonesForSpecificDay(currentComponent.getDatetime(), zonesDurationList));

            i++;
        }

        return zonesDurationList;

    }

    static String findZone(int heartRate, Map<String, Pair<Integer, Integer>> userHeartRateZones){

        if(heartRate < userHeartRateZones.get("Out_Of_Range").getSecond()){
            return "Out_of_Range";
        }
        else if(heartRate < userHeartRateZones.get("Fat_Burn").getSecond()){
            return "Fat_Burn";
        }
        else if(heartRate < userHeartRateZones.get("Cardio").getSecond()){
            return "Cardio";
        }
        else
            return "Peak";
    }

    static List<Triple<String, String, Integer>>
    aggregateHeartRateZonesForSpecificDay(String dateTime, List<Triple<String, String, Integer>> zonesDurationList) {

        List<Triple<String, String, Integer>> finalZonesDurationListPerDay = new ArrayList<>();

        String currentDateTime = extractDayFormat(dateTime);

        for (String hrZone : List.of("Out_of_Range", "Fat_Burn", "Cardio", "Peak")) {

            int specificDayAndHrZoneDuration = zonesDurationList.stream()
                    .filter(k -> (extractDayFormat(k.getFirst())).equalsIgnoreCase(currentDateTime))
                    .filter(k->k.getSecond().equals(hrZone))
                    .mapToInt(Triple::getThird).sum();

            finalZonesDurationListPerDay.add(new Triple(currentDateTime, hrZone, specificDayAndHrZoneDuration));

        }

        return finalZonesDurationListPerDay;
    }

    static String extractDayFormat(String input) {

        // TODO Make sure that this format is the correct one
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");

        LocalDateTime dateTime = LocalDateTime.parse(input, formatter);

        return dateTime + "^^<http://www.w3.org/2001/XMLSchema#dateTime>";

    }
}

package CARL.GraphDB;

import CARL.Components.*;
import CARL.Vocabulary;
import CARL.entities.SleepSet;
import CARL.entities.User;
import kotlin.Triple;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static AuxiliaryPackage.AuxiliaryPackageConstants.*;

public class GraphDBPopulation {

    RepositoryConnection repositoryConnection = null;

    public GraphDBPopulation(RepositoryConnection connection, List<User> usersList) throws IOException {

        // Set the class variable repository connection.
        setRepositoryConnection(connection);

        populateDataToGraphDB(usersList);

    }

    void setRepositoryConnection(RepositoryConnection repositoryConnection) { this.repositoryConnection = repositoryConnection; }

    void populateDataToGraphDB(List<User> usersList) throws IOException {

        for (int i = 0; i < usersList.size(); i++) {

            // Auxiliary variable to increase readability of the code.
            User userUnderExamination = usersList.get(i);

            // Various methods for population of the graphDB with the proper data.
            createUserInGraphDB(userUnderExamination);
            populateFibaroComponent(userUnderExamination.getUserFibaroList());
            populateFibaroEventComponent(userUnderExamination.getFibaroEventComponents());
            populateFitbitHourlySleepMeasurementComponent(userUnderExamination.getUserHourlySleepArray());
            populateFitbitHourlyStepsMeasurementComponent(userUnderExamination.getUserHourlyStepsArray());
            populateFitbitHourlyHeartRateMeasurementComponent(userUnderExamination.getUserHourlyHeartRateArray());

            populateFitbitDailyStepsMeasurementComponent(userUnderExamination);

            // TODO Uncomment the following lines, when the proper code for the population of sleep and heart has been written.
            populateFitbitDailySleepMeasurementComponent(userUnderExamination);
            populateFitbitDailyHeartRateMeasurementComponent(userUnderExamination);

            populateSleepCountsToGraphdb(userUnderExamination);
            populateDeviceThresholdsComponent(userUnderExamination);
            // Create the object that is responsible for executing the rules.
            //GraphDBRulesExecution GraphDBRulesExecution = new GraphDBRulesExecution(repositoryConnection, usersList);


        }

    }

    void populateFibaroComponent(List<FibaroComponent> fibaroComponents) throws IOException {

        String strInsert = BLANK_STRING;
        for (FibaroComponent fibaroComponent : fibaroComponents) {

            // EDW PREPEI NA FTIAXNEI QUERY GIA NA BAZEI TA STATEMENTS STI BASI, GIA KATHE fibaro Component.
            // NA FTIAXOYME MIA SINARTISI STATIC POY NA PAIRNEI SAN ORISMA TO fibaroComponent, KAI NA EKTELEI TO SOSTO QUERY
            //"<" + detectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Detection" + ">
         //   System.out.println("THE UNIQUE ID IS : " + fibaroComponent.getUniqueId());

            String fibaroSensorMeasurementURI = Vocabulary.NAMESPACE_CARL + "fibaroSensorMeasurement_" + fibaroComponent.getUniqueId();
            String roomURI = Vocabulary.NAMESPACE_CARL + "Room_" + fibaroComponent.getRoomId();
            String deviceURI = Vocabulary.NAMESPACE_CARL + "Device_" + fibaroComponent.getDeviceId();
            String sectionURI = Vocabulary.NAMESPACE_CARL + "Section_" + fibaroComponent.getSectionId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + fibaroComponent.getUser();

            strInsert =     "<" + fibaroSensorMeasurementURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "fibaroSensorMeasurement" + "> .\n"
                            + "<" + roomURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "Room" + "> .\n"
                            + "<" + deviceURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "Device" + "> .\n"
                            + "<" + sectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "Section" + "> .\n"
                            + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "Person" + "> .\n";

            strInsert = strInsert

                    +" <" + fibaroSensorMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "fibaroSensorMeasurementId" + "> " + "\"" + fibaroComponent.getId() + "\"" + "^^xsd:int" + "."
                    +" <" + fibaroSensorMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "timestamp" + "> " + "\"" + fibaroComponent.getTimestamp() + "\"" + "^^xsd:timestamp" + "."
                    +" <" + fibaroSensorMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "old_value" + "> " + "\"" + fibaroComponent.getOldValue() + "\"" + "^^xsd:float" + "."
                    +" <" + fibaroSensorMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "new_value" + "> " + "\"" + fibaroComponent.getNewValue() + "\"" + "^^xsd:float" + "."
                    +" <" + roomURI + "> <" + Vocabulary.NAMESPACE_CARL + "roomId" + "> " + "\"" + fibaroComponent.getRoomId() + "\"" + "^^xsd:int" + "."
                    +" <" + roomURI + "> <" + Vocabulary.NAMESPACE_CARL + "roomName" + "> " + "\"" + fibaroComponent.getRoomName() + "\"" + "^^xsd:string" + "."
                    +" <" + sectionURI + "> <" + Vocabulary.NAMESPACE_CARL + "sectionId" + "> " + "\"" + fibaroComponent.getSectionId() + "\"" + "^^xsd:int" + "."
                    +" <" + sectionURI + "> <" + Vocabulary.NAMESPACE_CARL + "sectionName" + "> " + "\"" + fibaroComponent.getSectionName() + "\"" + "^^xsd:string" + "."
                    +" <" + deviceURI + "> <" + Vocabulary.NAMESPACE_CARL + "deviceId" + "> " + "\"" + fibaroComponent.getDeviceId() + "\"" + "^^xsd:int" + "."
                    +" <" + deviceURI + "> <" + Vocabulary.NAMESPACE_CARL + "deviceName" + "> " + "\"" + fibaroComponent.getDeviceName() + "\"" + "^^xsd:string" + "."
                    +" <" + fibaroSensorMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "hasUser" + "> <" +  personURI + "> ."
                    +" <" + fibaroSensorMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "containsDevice" + "> <" +  deviceURI + "> ."
                    +" <" + fibaroSensorMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "containsSection" + "> <" +  sectionURI + "> ."
                    +" <" + fibaroSensorMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "containsRoom" + "> <" +  roomURI + "> ."
                    // TODO maybe change personId to UserId
                    +" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fibaroComponent.getUser() + "\"" + "^^xsd:int" + ".";

            strInsert = Vocabulary.PREFIXES_ALL +
                    "INSERT DATA {"
                    + strInsert
                    + " }";

            //System.out.println(strInsert);

            //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {

            repositoryConnection.begin();
            Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
            updateOperation.execute();

            try {
                repositoryConnection.commit();
            } catch (Exception e) {
                if (repositoryConnection.isActive())
                    repositoryConnection.rollback();
            }
        }

		//System.out.println("QUERY CREATED: \n" + strInsert);
    }

    void populateFibaroEventComponent(List<FibaroEventComponent> fibaroEventComponents) throws IOException {


        String strInsert = BLANK_STRING;
        for (FibaroEventComponent fibaroEventComponent : fibaroEventComponents) {

            // EDW PREPEI NA FTIAXNEI QUERY GIA NA BAZEI TA STATEMENTS STI BASI, GIA KATHE fibaro Component.
            // NA FTIAXOYME MIA SINARTISI STATIC POY NA PAIRNEI SAN ORISMA TO fibaroComponent, KAI NA EKTELEI TO SOSTO QUERY
            //"<" + detectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Detection" + ">
        //    System.out.println("THE UNIQUE ID IS : " + fibaroEventComponent.getUniqueId());

            String fibaroEventURI = Vocabulary.NAMESPACE_CARL + "FibaroEvent_" + fibaroEventComponent.getUniqueId();
            String roomURI = Vocabulary.NAMESPACE_CARL + "Room_" + fibaroEventComponent.getRoomName() + fibaroEventComponent.getRoomId();
            String deviceURI = Vocabulary.NAMESPACE_CARL + "Device_" + fibaroEventComponent.getDeviceName() + fibaroEventComponent.getDeviceId();
            //String sectionURI = Vocabulary.NAMESPACE_CARL + "Section_" + fibaroEventComponent.getSectionId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + fibaroEventComponent.getUserId();

            strInsert =     "<" + fibaroEventURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Event" + "> .\n"
                    + "<" + roomURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Room" + "> .\n"
                    + "<" + deviceURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Device" + "> .\n"
                    //+ "<" + sectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "Section" + "> .\n"
                    + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Person" + "> .\n";

            strInsert = strInsert

                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "fibaroSensorMeasurementId" + "> " + "\"" + fibaroEventComponent.getUniqueId() + "\"" + "^^xsd:int" + "."
                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventStartTime" + "> " + "\"" + fibaroEventComponent.getStartTimestamp() + "\"" + "^^xsd:timestamp" + "."
                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventEndTime" + "> " + "\"" + fibaroEventComponent.getEndTimestamp() + "\"" + "^^xsd:timestamp" + "."
                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventDuration" + "> " + "\"" + fibaroEventComponent.getDuration() + "\"" + "^^xsd:int" + "."
                    //+" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventName" + "> " + "\"" + fibaroEventComponent.getEventName() + "\"" + "^^xsd:string" + "."
                    +" <" + roomURI + "> <" + Vocabulary.NAMESPACE_CARL + "roomId" + "> " + "\"" + fibaroEventComponent.getRoomId() + "\"" + "^^xsd:int" + "."
                    +" <" + roomURI + "> <" + Vocabulary.NAMESPACE_CARL + "roomName" + "> " + "\"" + fibaroEventComponent.getRoomName() + "\"" + "^^xsd:string" + "."
                    //+" <" + sectionURI + "> <" + Vocabulary.NAMESPACE_CARL + "sectionId" + "> " + "\"" + fibaroEventComponent.getSectionId() + "\"" + "^^xsd:int" + "."
                    //+" <" + sectionURI + "> <" + Vocabulary.NAMESPACE_CARL + "sectionName" + "> " + "\"" + fibaroEventComponent.getSectionName() + "\"" + "^^xsd:string" + "."
                    +" <" + deviceURI + "> <" + Vocabulary.NAMESPACE_CARL + "deviceId" + "> " + "\"" + fibaroEventComponent.getDeviceId() + "\"" + "^^xsd:int" + "."
                    +" <" + deviceURI + "> <" + Vocabulary.NAMESPACE_CARL + "deviceName" + "> " + "\"" + fibaroEventComponent.getDeviceName() + "\"" + "^^xsd:string" + "."
                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventRefersToPerson" + "> <" +  personURI + "> ."
                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventContainsDevice" + "> <" +  deviceURI + "> ."
                    //+" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventContainsSection" + "> <" +  sectionURI + "> ."
                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventContainsRoom" + "> <" +  roomURI + "> .";
                    // TODO maybe change personId to UserId
                    //+" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fibaroEventComponent.getUserId() + "\"" + "^^xsd:int" + ".";

            strInsert = Vocabulary.PREFIXES_ALL +
                    "INSERT DATA{"
                    + strInsert
                    + " }";

            //System.out.println(strInsert);

            //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {
            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
            operation.execute();

//            repositoryConnection.begin();
//            Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
//            updateOperation.execute();
//
//            try {
//                repositoryConnection.commit();
//                System.out.println("Commited");
//            } catch (Exception e) {
//                if (repositoryConnection.isActive())
//                    repositoryConnection.rollback();
//            }
        }

        //System.out.println("QUERY CREATED: \n" + strInsert);
    }

    void populateFitbitHourlySleepMeasurementComponent(List<FitbitHourlySleepMeasurementComponent> fitbitHourlySleepMeasurementComponents) throws IOException {

        String strInsert = BLANK_STRING;
        for (FitbitHourlySleepMeasurementComponent fitbitHourlySleepMeasurementComponent : fitbitHourlySleepMeasurementComponents) {

            String fitbitHourlySleepURI = Vocabulary.NAMESPACE_CARL + "FitbitHourlySleep_" + fitbitHourlySleepMeasurementComponent.getId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + fitbitHourlySleepMeasurementComponent.getUser_id();

            strInsert =     "<" + fitbitHourlySleepURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "FitbitHourlySleepSensorMeasurement" + "> .\n"
                    + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "Person" + "> .\n";

            strInsert = strInsert

                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "FitbitHourlySleepId" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getId() + "\"" + "^^xsd:int" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "timestamp" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getDatetime() + "\"" + "^^xsd:timestamp" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "type" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getType() + "\"" + "^^xsd:string" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "duration" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getDuration() + "\"" + "^^xsd:int" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "startTime" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getStartTime() + "\"" + "^^xsd:timestamp" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "endTime" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getEndTime() + "\"" + "^^xsd:timestamp" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "efficiency" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getEfficiency() + "\"" + "^^xsd:int" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "minAfterWakeup" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getMinAfterWakeup() + "\"" + "^^xsd:int" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "minAsleep" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getMinAsleep() + "\"" + "^^xsd:int" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "minAwake" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getMinAwake() + "\"" + "^^xsd:int" + "."
                    +" <" + fitbitHourlySleepURI + "> <" + Vocabulary.NAMESPACE_CARL + "minToAsleep" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getMintoAsleep() + "\"" + "^^xsd:int" + ".";
                    //TODO create for to loop through sleep sessions
                    for(int i=0;i<fitbitHourlySleepMeasurementComponent.getSleepSetArrayList().size();i++){
                        List<SleepSet> sleepSetList = fitbitHourlySleepMeasurementComponent.getSleepSetArrayList();
                        String fitbitHourlySleepSetURI = Vocabulary.NAMESPACE_CARL + "FitbitHourlySleepStage_" + sleepSetList.get(i).getDate() + i;
                        strInsert = strInsert
                                + "<" + fitbitHourlySleepSetURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "FitbitHourlySleepStage" + "> .\n"
                                +" <" + fitbitHourlySleepSetURI + "> <" + Vocabulary.NAMESPACE_CARL + "date" + "> " + "\"" + sleepSetList.get(i).getDate() + "\"" + "^^xsd:timestamp" + "."
                                +" <" + fitbitHourlySleepSetURI + "> <" + Vocabulary.NAMESPACE_CARL + "duration" + "> " + "\"" + sleepSetList.get(i).getDurationInSecs() + "\"" + "^^xsd:int" + "."
                                +" <" + fitbitHourlySleepSetURI + "> <" + Vocabulary.NAMESPACE_CARL + "level" + "> " + "\"" + sleepSetList.get(i).getLevel() + "\"" + "^^xsd:string" + "."
                                +" <" + fitbitHourlySleepSetURI + "> <" + Vocabulary.NAMESPACE_CARL + "refersToHourlySleepMeasurement" + "> <" + fitbitHourlySleepURI + "> .";
                    }
                    // TODO maybe change personId to UserId
                    //+" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getUser() + "\"" + "^^xsd:int" + ".";

            strInsert = Vocabulary.PREFIXES_ALL +
                    "INSERT DATA {"
                    + strInsert
                    + " }";

            //System.out.println(strInsert);

            //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {

            repositoryConnection.begin();
            Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
            updateOperation.execute();

            try {
                repositoryConnection.commit();
            } catch (Exception e) {
                if (repositoryConnection.isActive())
                    repositoryConnection.rollback();
            }
        }

        //System.out.println("QUERY CREATED: \n" + strInsert);
    }

    public void populateFitbitHourlyStepsMeasurementComponent(List<FitbitHourlyStepsMeasurementComponent> fitbitHourlyStepsMeasurementComponents) {

        String strInsert = BLANK_STRING;
        for (FitbitHourlyStepsMeasurementComponent fitbitHourlyStepsMeasurementComponent : fitbitHourlyStepsMeasurementComponents) {

            fitbitHourlyStepsMeasurementComponent.setId(FITBIT_HOURLY_STEPS_COMPONENT_COUNTER++);

            // EDW PREPEI NA FTIAXNEI QUERY GIA NA BAZEI TA STATEMENTS STI BASI, GIA KATHE fibaro Component.
            // NA FTIAXOYME MIA SINARTISI STATIC POY NA PAIRNEI SAN ORISMA TO fibaroComponent, KAI NA EKTELEI TO SOSTO QUERY
            //"<" + detectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Detection" + ">
          //  System.out.println("THE UNIQUE ID IS : " + fitbitHourlyStepsMeasurementComponent.getId());

            String fitbitHourlyStepsURI = Vocabulary.NAMESPACE_CARL + "FitbitHourlySteps_" + fitbitHourlyStepsMeasurementComponent.getId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + fitbitHourlyStepsMeasurementComponent.getUser_id();

            strInsert =     "<" + fitbitHourlyStepsURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "FitbitHourlyStepsSensorMeasurement" + "> .\n"
                    + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Person" + "> .\n";

            strInsert = strInsert

                    +" <" + fitbitHourlyStepsURI + "> <" + Vocabulary.NAMESPACE_CARL + "FitbitHourlyStepsId" + "> " + "\"" + fitbitHourlyStepsMeasurementComponent.getId() + "\"" + "^^xsd:int" + "."
                    +" <" + fitbitHourlyStepsURI + "> <" + Vocabulary.NAMESPACE_CARL + "timestamp" + "> " + "\"" + fitbitHourlyStepsMeasurementComponent.getDateTime() + "\"" + "^^xsd:timestamp" + "."
                    +" <" + fitbitHourlyStepsURI + "> <" + Vocabulary.NAMESPACE_CARL + "rate" + "> " + "\"" + fitbitHourlyStepsMeasurementComponent.getRate() + "\"" + "^^xsd:string" + ".";

            }
            // TODO maybe change personId to UserId
            //+" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getUser() + "\"" + "^^xsd:int" + ".";

            strInsert = Vocabulary.PREFIXES_ALL +
                    "INSERT DATA {"
                    + strInsert
                    + " }";

            //System.out.println(strInsert);

            //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {

            repositoryConnection.begin();
            Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
            updateOperation.execute();

            try {
                repositoryConnection.commit();
            } catch (Exception e) {
                if (repositoryConnection.isActive())
                    repositoryConnection.rollback();
            }


        //System.out.println("QUERY CREATED: \n" + strInsert);
    }



    public void populateFitbitHourlyHeartRateMeasurementComponent(List<FitbitHourlyHeartRateMeasurementComponent> fitbitHourlyHeartRateMeasurementComponents) {

        String strInsert = BLANK_STRING;
        for (FitbitHourlyHeartRateMeasurementComponent fitbitHourlyHeartRateMeasurementComponent : fitbitHourlyHeartRateMeasurementComponents) {

            fitbitHourlyHeartRateMeasurementComponent.setId(FITBIT_HOURLY_HEART_RATE_COMPONENT_COUNTER++);

            // EDW PREPEI NA FTIAXNEI QUERY GIA NA BAZEI TA STATEMENTS STI BASI, GIA KATHE fibaro Component.
            // NA FTIAXOYME MIA SINARTISI STATIC POY NA PAIRNEI SAN ORISMA TO fibaroComponent, KAI NA EKTELEI TO SOSTO QUERY
            //"<" + detectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Detection" + ">
         //   System.out.println("THE UNIQUE ID IS : " + fitbitHourlyHeartRateMeasurementComponent.getId());

            String fitbitHourlyHeartRateURI = Vocabulary.NAMESPACE_CARL + "FitbitHourlyHeartRate_" + fitbitHourlyHeartRateMeasurementComponent.getId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + fitbitHourlyHeartRateMeasurementComponent.getUser_id();

            strInsert =     "<" + fitbitHourlyHeartRateURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "FitbitHourlyHeartRateSensorMeasurement" + "> .\n"
                    + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "Person" + "> .\n";

            strInsert = strInsert

                    +" <" + fitbitHourlyHeartRateURI + "> <" + Vocabulary.NAMESPACE_CARL + "FitbitHourlyHeartRateId" + "> " + "\"" + fitbitHourlyHeartRateMeasurementComponent.getId() + "\"" + "^^xsd:int" + "."
                    +" <" + fitbitHourlyHeartRateURI + "> <" + Vocabulary.NAMESPACE_CARL + "timestamp" + "> " + "\"" + fitbitHourlyHeartRateMeasurementComponent.getDateTime() + "\"" + "^^xsd:timestamp" + "."
                    +" <" + fitbitHourlyHeartRateURI + "> <" + Vocabulary.NAMESPACE_CARL + "resting_hr" + "> " + "\"" + fitbitHourlyHeartRateMeasurementComponent.getResting_hr() + "\"" + "^^xsd:string" + ".";

        }
        // TODO maybe change personId to UserId
        //+" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getUser() + "\"" + "^^xsd:int" + ".";

        strInsert = Vocabulary.PREFIXES_ALL +
                "INSERT DATA {"
                + strInsert
                + " }";

       // System.out.println(strInsert);

        //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {

        repositoryConnection.begin();
        Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
        updateOperation.execute();

        try {
            repositoryConnection.commit();
        } catch (Exception e) {
            if (repositoryConnection.isActive())
                repositoryConnection.rollback();
        }


     //   System.out.println("QUERY CREATED: \n" + strInsert);
    }

    public void createUserInGraphDB(User user) throws IOException {

        String insertUserQuery = "PREFIX CARL:<http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        insertUserQuery += "INSERT Data{\n";
        insertUserQuery += "CARL:Person_" + user.getId();
        insertUserQuery += " rdf:type CARL:Person .\n";
        insertUserQuery += "CARL:Person_" + user.getId();
        insertUserQuery += " CARL:username ";
        insertUserQuery += "\"" + user.getUsername() + "\"^^xsd:string . ";
        insertUserQuery += ".\n";
        insertUserQuery += "CARL:Person_" + user.getId();
        insertUserQuery += " CARL:token ";
        insertUserQuery += "\"" + user.getToken() + "\"^^xsd:string . ";
        insertUserQuery += "CARL:Person_" + user.getId();
        insertUserQuery += " CARL:personId ";
        insertUserQuery += "\"" + user.getId() + "\"^^xsd:int . ";
        insertUserQuery += ".";
        insertUserQuery += "}";

        Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, insertUserQuery);
        operation.execute();
    }

    void populateSleepCountsToGraphdb(User user){

        String uniqueID = " ";
        for (Map.Entry<String, Triple> pair : user.getUserSleepCountsMap().entrySet()) {
         //   System.out.println(String.format("Key (name) is: %s, Value (age) is : %s", pair.getKey(), pair.getValue()));

            uniqueID = user.getId() + "_" + pair.getKey() ;
            String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "INSERT Data{\n";
            queryString2 += "CARL:SleepCount_" + uniqueID;
            queryString2 += " rdf:type CARL:SleepCount .\n";
            queryString2 += "CARL:SleepCount_" + uniqueID;
            queryString2 += " CARL:sleepCountTimestamp ";
            queryString2 += "\"" + pair.getKey() + "\"^^xsd:dateTimestamp ";
            queryString2 += ".\n";
            queryString2 += "CARL:SleepCount_" + uniqueID;
            queryString2 += " CARL:sleepCountAwake ";
            queryString2 += "\"" + pair.getValue().getFirst()+ "\"^^xsd:int .";
            queryString2 += "CARL:SleepCount_" + uniqueID;
            queryString2 += " CARL:sleepCountRestless ";
            queryString2 += "\"" + pair.getValue().getSecond()+ "\"^^xsd:int .";
            queryString2 += "CARL:SleepCount_" + uniqueID;
            queryString2 += " CARL:sleepCountWake ";
            queryString2 += "\"" + pair.getValue().getThird()+ "\"^^xsd:int .";
            queryString2 += "CARL:SleepCount_" + uniqueID;
            queryString2 += " CARL:sleepCountRefersToPerson CARL:Person_" + user.getId();
            queryString2 += ".\n";
            queryString2 += "}";

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString2);
            operation.execute();
        }

    }

    void populateZonesToGraphdb(User user){

        String uniqueID = " ";
        for (Triple triple : user.getZonesDurationList()) { // iterating on list of 'Pair's
            uniqueID = String.valueOf(user.getId()) + triple.getFirst() + triple.getSecond();
            String queryString2 = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "INSERT Data{\n";
            queryString2 += ":SleepZone_" + uniqueID;
            queryString2 += " rdf:type :SleepZone .\n";
            queryString2 += ":SleepZone_" + uniqueID;
            queryString2 += " CARL:sleepZoneTimestamp ";
            queryString2 += "\"" + triple.getFirst() + "\"^^xsd:dateTimestamp ";
            queryString2 += ".\n";
            queryString2 += ":SleepZone_" + uniqueID;
            queryString2 += " :sleepZoneName ";
            queryString2 += "\"" + triple.getSecond() + "\"^^xsd:string .";
            queryString2 += ":SleepZone_" + uniqueID;
            queryString2 += " :sleepZoneDuration ";
            queryString2 += "\"" + triple.getThird() + "\"^^xsd:int .";
            queryString2 += ":SleepZone_" + uniqueID;
            queryString2 += " :sleepZoneRefersToPerson :Person_" + user.getId();
            queryString2 += ".\n";
            queryString2 += "}";

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString2);
            operation.execute();
        }

    }


    void populateNumberOfAsleepInNapsMap(User user) {

        String uniqueID = " ";
        for (Map.Entry<String, Integer> entry : user.getNumberOfAsleepInNapsMap().entrySet()) {
          //  System.out.println(String.format("Key (name) is: %s, Value (age) is : %s", entry.getKey(), entry.getValue()));

            uniqueID = user.getId() + entry.getKey() ;
            String queryString2 = "PREFIX CARL:<http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "INSERT Data{\n";
            queryString2 += "CARL:NumberOfAsleepInNapsCount_" + uniqueID;
            queryString2 += " rdf:type CARL:NumberOfAsleepInNapsCount .\n";
            queryString2 += "CARL:NumberOfAsleepInNapsCount_" + uniqueID;
            queryString2 += " CARL:numberOfAsleepInNapsDateTime ";
            queryString2 += "\"" + entry.getKey() + "\"^^xsd:dateTimestamp ";
            queryString2 += ".\n";
            queryString2 += "CARL:NumberOfAsleepInNapsCount_" + uniqueID;
            queryString2 += " CARL:numberOfAsleepInNaps ";
            queryString2 += "\"" + entry.getValue()+ "\"^^xsd:int .";
            queryString2 += "CARL:NumberOfAsleepInNapsCount_" + uniqueID;
            queryString2 += " CARL:numberOfAsleepInNapsCountRefersToPerson CARL:Person_" + user.getId();
            queryString2 += ".\n";
            queryString2 += "}";

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString2);
            operation.execute();
        }
    }


    void populateNapEndTimeCloseToSleepStartTime(User user) {

        String uniqueID = " ";
        for (Map.Entry<String, Boolean> entry : user.getNapEndTimeCloseToSleepStartTime().entrySet()) {
         //   System.out.println(String.format("Key (name) is: %s, Value (age) is : %s", entry.getKey(), entry.getValue()));

            uniqueID = user.getId() + entry.getKey() ;
            String queryString2 = "PREFIX CARL:<http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "INSERT Data{\n";
            queryString2 += "CARL:NapEndTimeCloseToSleepStartTimeObject_" + uniqueID;
            queryString2 += " rdf:type CARL:NapEndTimeCloseToSleepStartTimeObject .\n";
            queryString2 += "CARL:NapEndTimeCloseToSleepStartTimeObject_" + uniqueID;
            queryString2 += " CARL:napEndTimeCloseToSleepStartTimeDateTime ";
            queryString2 += "\"" + entry.getKey() + "\"^^xsd:dateTimestamp ";
            queryString2 += ".\n";
            queryString2 += "CARL:NapEndTimeCloseToSleepStartTimeObject_" + uniqueID;
            queryString2 += " CARL:napEndTimeCloseToSleepStartTime ";
            queryString2 += "\"" + entry.getValue()+ "\"^^xsd:boolean .";
            queryString2 += "CARL:NapEndTimeCloseToSleepStartTimeObject_" + uniqueID;
            queryString2 += " CARL:napEndTimeCloseToSleepStartTimeObjectRefersToPerson CARL:Person_" + user.getId();
            queryString2 += ".\n";
            queryString2 += "}";

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString2);
            operation.execute();
        }
    }

    public void populateFitbitDailyStepsMeasurementComponent(User user) throws IOException {

        String strInsert = BLANK_STRING;

        for (FitbitDailyStepsMeasurementComponent fitbitDailyStepsMeasurementComponent : user.getUserDailyStepsArray()) {

            fitbitDailyStepsMeasurementComponent.setId(FITBIT_DAILY_HEART_RATE_COMPONENT_COUNTER++);

            // EDW PREPEI NA FTIAXNEI QUERY GIA NA BAZEI TA STATEMENTS STI BASI, GIA KATHE fibaro Component.
            // NA FTIAXOYME MIA SINARTISI STATIC POY NA PAIRNEI SAN ORISMA TO fibaroComponent, KAI NA EKTELEI TO SOSTO QUERY
            //"<" + detectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Detection" + ">
          //  System.out.println("THE UNIQUE ID IS : " + fitbitDailyStepsMeasurementComponent.getId());

            String fitbitDailyStepsURI = Vocabulary.NAMESPACE_CARL + "FitbitDailySteps_" + fitbitDailyStepsMeasurementComponent.getId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + fitbitDailyStepsMeasurementComponent.getUser_id();

            strInsert =     "<" + fitbitDailyStepsURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "DailyStepsMeasurement" + "> .\n"
                    + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Person" + "> .\n";

            strInsert = strInsert +
                    "<" + fitbitDailyStepsURI +"> <" +  Vocabulary.NAMESPACE_CARL + "dailyStepsReferToPerson" + "> <" + personURI + "> . \n";

            strInsert = strInsert

                    +" <" + fitbitDailyStepsURI + "> <" + Vocabulary.NAMESPACE_CARL + "dailyStepsDatetime" + "> " + "\"" + fitbitDailyStepsMeasurementComponent.getDatetime() + "\"" + "^^xsd:dateTimeStamp" + "."
                    +" <" + fitbitDailyStepsURI + "> <" + Vocabulary.NAMESPACE_CARL + "rate" + "> " + "\"" + fitbitDailyStepsMeasurementComponent.getRate() + "\"" + "^^xsd:double" + ".";


            // TODO maybe change personId to UserId
            //+" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getUser() + "\"" + "^^xsd:int" + ".";

            strInsert = Vocabulary.PREFIXES_ALL +
                    "INSERT DATA {"
                    + strInsert
                    + " }";

         //   System.out.println(strInsert);

            //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {

            repositoryConnection.begin();
            Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
            updateOperation.execute();

            try {
                repositoryConnection.commit();
            } catch (Exception e) {
                if (repositoryConnection.isActive())
                    repositoryConnection.rollback();
            }
        }

      //  System.out.println("QUERY CREATED: \n" + strInsert);
    }


    void populateFitbitDailySleepMeasurementComponent(User user) {

        String strInsert = BLANK_STRING;

        for (FitbitDailySleepMeasurementComponent fitbitDailySleepMeasurementComponent : user.getUserDailySleepArray()) {

            fitbitDailySleepMeasurementComponent.setId(FITBIT_DAILY_SLEEP_COMPONENT_COUNTER++);

            // EDW PREPEI NA FTIAXNEI QUERY GIA NA BAZEI TA STATEMENTS STI BASI, GIA KATHE fibaro Component.
            // NA FTIAXOYME MIA SINARTISI STATIC POY NA PAIRNEI SAN ORISMA TO fibaroComponent, KAI NA EKTELEI TO SOSTO QUERY
            //"<" + detectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Detection" + ">
         //   System.out.println("THE UNIQUE ID IS : " + fitbitDailySleepMeasurementComponent.getId());

            String dailySleepMeasurementURI = Vocabulary.NAMESPACE_CARL + "DailySleepMeasurement_" + fitbitDailySleepMeasurementComponent.getId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + fitbitDailySleepMeasurementComponent.getUser_id();

            strInsert =     "<" + dailySleepMeasurementURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "DailySleepMeasurement" + "> .\n"
                    + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Person" + "> .\n";

            strInsert = strInsert +
                    "<" + dailySleepMeasurementURI +"> <" +  Vocabulary.NAMESPACE_CARL + "dailySleepRefersToPerson" + "> <" + personURI + "> . \n";

            strInsert = strInsert

                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "FitbitDailySleepId" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getId() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "light_minutes" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getLight_minutes() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "rem_minutes" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getLight_minutes() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "deep_minutes" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getDeep_minutes() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "minutes_a_sleep" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getMinutes_asleep() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "efficiency" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getEfficiency() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "wake_minutes" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getWake_minutes() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "asleep_minutes" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getAsleep_minutes() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "restless_minutes" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getRestless_minutes() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "awake_minutes" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getAwake_minutes() + "\"" + "^^xsd:int" + "."

                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "minutes_awake" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getMinutes_awake() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "minutes_asleep" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getMinutes_asleep() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "wake_count" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getWake_count() + "\"" + "^^xsd:int" + "."

                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "light_count" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getLight_count() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "deep_count" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getDeep_count() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "rem_count" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getRem_count() + "\"" + "^^xsd:int" + "."
                    +" <" + dailySleepMeasurementURI + "> <" + Vocabulary.NAMESPACE_CARL + "datetimeDailySleep" + "> " + "\"" + fitbitDailySleepMeasurementComponent.getDate() + "\"" + "^^xsd:dateTimeStamp" + ".";


            // TODO maybe change personId to UserId
            //+" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getUser() + "\"" + "^^xsd:int" + ".";

            strInsert = Vocabulary.PREFIXES_ALL +
                    "INSERT DATA {"
                    + strInsert
                    + " }";

         //  System.out.println(strInsert);

            //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {

            repositoryConnection.begin();
            Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
            updateOperation.execute();

            try {
                repositoryConnection.commit();
            } catch (Exception e) {
                if (repositoryConnection.isActive())
                    repositoryConnection.rollback();
            }
        }

      //  System.out.println("QUERY CREATED: \n" + strInsert);
    }


    void populateFitbitDailyHeartRateMeasurementComponent(User user) {

        String strInsert = BLANK_STRING;

        for (FitbitDailyHeartRateMeasurementComponent fitbitDailyHeartRateMeasurementComponent : user.getUserDailyHeartRateArray()) {

            fitbitDailyHeartRateMeasurementComponent.setId(FITBIT_DAILY_HEART_RATE_COMPONENT_COUNTER++);

            // EDW PREPEI NA FTIAXNEI QUERY GIA NA BAZEI TA STATEMENTS STI BASI, GIA KATHE fibaro Component.
            // NA FTIAXOYME MIA SINARTISI STATIC POY NA PAIRNEI SAN ORISMA TO fibaroComponent, KAI NA EKTELEI TO SOSTO QUERY
            //"<" + detectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Detection" + ">
           // System.out.println("THE UNIQUE ID IS : " + fitbitDailyHeartRateMeasurementComponent.getId());

            String fitbitDailyHeartRateURI = Vocabulary.NAMESPACE_CARL + "FitbitDailyHeartRate_" + fitbitDailyHeartRateMeasurementComponent.getId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + fitbitDailyHeartRateMeasurementComponent.getUser_id();

            strInsert =     "<" + fitbitDailyHeartRateURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "DailyHeartRateMeasurement" + "> .\n"
                    + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Person" + "> .\n";

            strInsert = strInsert +
                    "<" + fitbitDailyHeartRateURI +"> <" +  Vocabulary.NAMESPACE_CARL + "dailyHeartRateRefersToPerson" + "> <" + personURI + "> . \n";

            strInsert = strInsert

                    +" <" + fitbitDailyHeartRateURI + "> <" + Vocabulary.NAMESPACE_CARL + "restingHeartRate" + "> " + "\"" + fitbitDailyHeartRateMeasurementComponent.getResting_hr() + "\"" + "^^xsd:double" + "."
                    +" <" + fitbitDailyHeartRateURI + "> <" + Vocabulary.NAMESPACE_CARL + "dailyHeartRateDatetime" + "> " + "\"" + fitbitDailyHeartRateMeasurementComponent.getDateTime() + "\"" + "^^xsd:dateTimeStamp" + ".";


            // TODO maybe change personId to UserId
            //+" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fitbitHourlySleepMeasurementComponent.getUser() + "\"" + "^^xsd:int" + ".";

            strInsert = Vocabulary.PREFIXES_ALL +
                    "INSERT DATA {"
                    + strInsert
                    + " }";

        //    System.out.println(strInsert);

            //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {

            repositoryConnection.begin();
            Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
            updateOperation.execute();

            try {
                repositoryConnection.commit();
            } catch (Exception e) {
                if (repositoryConnection.isActive())
                    repositoryConnection.rollback();
            }
        }

       // System.out.println("QUERY CREATED: \n" + strInsert);
    }

    void populateDeviceThresholdsComponent(User user) throws IOException {


        String strInsert = BLANK_STRING;
        for (DeviceThresholds DeviceThresholdsComponent : user.getUserDeviceThresholdsList()) {

            String fibaroEventURI = Vocabulary.NAMESPACE_CARL + "FibaroEvent_" + DeviceThresholdsComponent.getDeviceId();
            //String roomURI = Vocabulary.NAMESPACE_CARL + "Room_" + DeviceThresholdsComponent.getRoomName() + DeviceThresholdsComponent.getRoomId();
            String deviceURI = Vocabulary.NAMESPACE_CARL + "Device_" + DeviceThresholdsComponent.getDeviceName() + DeviceThresholdsComponent.getDeviceId();
            //String sectionURI = Vocabulary.NAMESPACE_CARL + "Section_" + fibaroEventComponent.getSectionId();
            // TODO maybe change the Person_ to User_
            String personURI = Vocabulary.NAMESPACE_CARL + "Person_" + DeviceThresholdsComponent.getUserId();

            strInsert =     "<" + fibaroEventURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Event" + "> .\n"
                    //+ "<" + roomURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Room" + "> .\n"
                    + "<" + deviceURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Device" + "> .\n"
                    //+ "<" + sectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.PREFIX + "Section" + "> .\n"
                    + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_CARL + "Person" + "> .\n";

//            strInsert = strInsert
//
//                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "fibaroSensorMeasurementId" + "> " + "\"" + fibaroEventComponent.getUniqueId() + "\"" + "^^xsd:int" + "."
//                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventStartTime" + "> " + "\"" + fibaroEventComponent.getStartTimestamp() + "\"" + "^^xsd:timestamp" + "."
//                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventEndTime" + "> " + "\"" + fibaroEventComponent.getEndTimestamp() + "\"" + "^^xsd:timestamp" + "."
//                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventDuration" + "> " + "\"" + fibaroEventComponent.getDuration() + "\"" + "^^xsd:int" + "."
//                    //+" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventName" + "> " + "\"" + fibaroEventComponent.getEventName() + "\"" + "^^xsd:string" + "."
//                    +" <" + roomURI + "> <" + Vocabulary.NAMESPACE_CARL + "roomId" + "> " + "\"" + fibaroEventComponent.getRoomId() + "\"" + "^^xsd:int" + "."
//                    +" <" + roomURI + "> <" + Vocabulary.NAMESPACE_CARL + "roomName" + "> " + "\"" + fibaroEventComponent.getRoomName() + "\"" + "^^xsd:string" + "."
//                    //+" <" + sectionURI + "> <" + Vocabulary.NAMESPACE_CARL + "sectionId" + "> " + "\"" + fibaroEventComponent.getSectionId() + "\"" + "^^xsd:int" + "."
//                    //+" <" + sectionURI + "> <" + Vocabulary.NAMESPACE_CARL + "sectionName" + "> " + "\"" + fibaroEventComponent.getSectionName() + "\"" + "^^xsd:string" + "."
//                    +" <" + deviceURI + "> <" + Vocabulary.NAMESPACE_CARL + "deviceId" + "> " + "\"" + fibaroEventComponent.getDeviceId() + "\"" + "^^xsd:int" + "."
//                    +" <" + deviceURI + "> <" + Vocabulary.NAMESPACE_CARL + "deviceName" + "> " + "\"" + fibaroEventComponent.getDeviceName() + "\"" + "^^xsd:string" + "."
//                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventRefersToPerson" + "> <" +  personURI + "> ."
//                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventContainsDevice" + "> <" +  deviceURI + "> ."
//                    //+" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventContainsSection" + "> <" +  sectionURI + "> ."
//                    +" <" + fibaroEventURI + "> <" + Vocabulary.NAMESPACE_CARL + "eventContainsRoom" + "> <" +  roomURI + "> .";
            // TODO maybe change personId to UserId
            //+" <" + personURI + "> <" + Vocabulary.NAMESPACE_CARL + "personId" + "> " + "\"" + fibaroEventComponent.getUserId() + "\"" + "^^xsd:int" + ".";

            strInsert = Vocabulary.PREFIXES_ALL +
                    "INSERT DATA{"
                    + strInsert
                    + " }";

            //System.out.println(strInsert);

            //try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {
            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
            operation.execute();

//            repositoryConnection.begin();
//            Update updateOperation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
//            updateOperation.execute();
//
//            try {
//                repositoryConnection.commit();
//                System.out.println("Commited");
//            } catch (Exception e) {
//                if (repositoryConnection.isActive())
//                    repositoryConnection.rollback();
//            }
        }

        //System.out.println("QUERY CREATED: \n" + strInsert);
    }


}

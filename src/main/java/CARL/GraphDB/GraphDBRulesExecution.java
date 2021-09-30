package CARL.GraphDB;

import CARL.entities.User;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import java.io.IOException;
import java.util.*;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;

import java.util.ArrayList;
import java.util.UUID;

public class GraphDBRulesExecution {

    private RepositoryConnection repositoryConnection = null;

    private List<User> users = null;

    public void setRepositoryConnection(RepositoryConnection repositoryConnection) { this.repositoryConnection = repositoryConnection; }
    public void setUsers(List<User> users) { this.users = users; }

    public GraphDBRulesExecution(RepositoryConnection repositoryConnection, List<User> users) throws IOException {

        // Set the private class variabes, repository connection and users.
        setRepositoryConnection(repositoryConnection);
        setUsers(users);

        // Execute the rules.
        executeRules();
    }

    void executeRules() throws IOException {

        LackofMovementRule(repositoryConnection);
        // BadQualityofSleep(repositoryConnection);
        LackofSleep(repositoryConnection);
        // TooMuchSleep(repositoryConnection);
        LowHR(repositoryConnection);
        // StressOrPain(repositoryConnection);
        // Insomnia(repositoryConnection);
        // restlessnessEfficiencyRule(repositoryConnection);

    }
    //SPIN Rule 1:steps<100 && steps>0 -->Lack of Movement Problem
    public void LackofMovementRule(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedSt = new ArrayList<String>();
        //select stepsRefersToPatient
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?s \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?p a CARL:Person . \n";
        queryString2 += "?s a CARL:DailyStepsMeasurement.  \n";
        queryString2 += "?s CARL:rate ?r1. \n";
        queryString2 += "?s CARL:dailyStepsReferToPerson ?p. \n";
        queryString2 += "FILTER(?r1<2000.00) \n";
        queryString2 += "}";

        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();

        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            detectedSt.add(str1.substring(index + 1));
            //System.out.println("?s = " + solution.getValue("s"));

        }

        System.out.println("LACK OF MOVEMENT PROBLEMS GENERATED : " + detectedSt.size());

        String uniqueID = UUID.randomUUID().toString();
        for (int i = 0; i < detectedSt.size(); i++) {
            String queryString = "";
            uniqueID = detectedSt.get(i);
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "INSERT{ \n";
            queryString += "CARL:LackOfMovementProblem" + uniqueID + " rdf:type CARL:LackOfMovement. \n";
            queryString += "CARL:LackOfMovementProblem" + uniqueID + " CARL:lackOfMovementDatetime ?dt1.\n";
            queryString += "CARL:LackOfMovementProblem" + uniqueID + " CARL:lackOfMovementRate ?r.\n";
            queryString += "?p CARL:hasMovementProblem CARL:LackOfMovementProblem" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Person . \n";
            queryString += "CARL:" + detectedSt.get(i) + " CARL:dailyStepsDatetime ?d1. \n";
            queryString += "CARL:" + detectedSt.get(i) + " CARL:dailyStepsReferToPerson ?p.\n";
            queryString += "CARL:" + detectedSt.get(i) + " CARL:rate ?r1.\n";
            queryString += "BIND(?d1 AS ?dt1) \n";
            queryString += "BIND(?r1 AS ?r) \n";
            queryString += "}";

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }

    }

    public void restlessnessEfficiencyRule(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedRestlessness = new ArrayList<String>();
        //select stepsRefersToPatient
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?s \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?p a CARL:Patient . \n";
        queryString2 += "?s a CARL:Sleep.  \n";
        queryString2 += "?s CARL:efficiency ?e1. \n";
        queryString2 += "?s CARL:sleepRefersToPatient ?p. \n";
        queryString2 += "FILTER(?e1<85) \n";
        queryString2 += "}";

        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();

        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            detectedRestlessness.add(str1.substring(index + 1));
            System.out.println("?s = " + solution.getValue("s"));

        }

        System.out.println("RESTLESSNESS/EFFICIENCY PROBLEMS GENERATED: " + detectedRestlessness.size());

        String uniqueID = UUID.randomUUID().toString();
        for (int i = 0; i < detectedRestlessness.size(); i++) {
            String queryString = "";
            uniqueID = detectedRestlessness.get(i);
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "PREFIX : <http://localhost/resources/experimental_db#> ";
            queryString += "INSERT{ \n";
            queryString += "CARL:SleepProblem" + uniqueID + " rdf:type CARL:Restlessness. \n";
            queryString += "CARL:SleepProblem" + uniqueID + " CARL:dateTime ?dt1.\n";
            //queryString += ":SleepProblem" + uniqueID + " :efficiency ?e1.\n";
            queryString += "?p CARL:hasSleepProblem CARL:SleepProblem" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Patient . \n";
            queryString += "?s a CARL:Sleep . \n";
            queryString += "?s CARL:sleepRefersToPatient ?p. \n";
            queryString += "?s CARL:sleepDateTime ?d1 \n";
            //queryString += "CARL:" + detectedRestlessness.get(i) + " CARL:dateTime ?d1. \n";
            //queryString += ":" + detectedRestlessness.get(i) + " :efficiency ?e. \n";
            //queryString += "CARL:" + detectedRestlessness.get(i) + " CARL:sleepRefersToPatient ?p.\n";
            //queryString += "BIND(?e AS ?e1) \n";
            queryString += "BIND(?d1 AS ?dt1) \n";
            queryString += "}";

            System.out.println(detectedRestlessness.get(i));
            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }

    }
    public void restlessnessRule(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedRestlessness = new ArrayList<String>();
        //select stepsRefersToPatient
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?s \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?p a CARL:Patient . \n";
        queryString2 += "?s a CARL:DailySleep.  \n";
        queryString2 += "?s CARL:wakeCount ?wc. \n";
        queryString2 += "?s CARL:dailySleepRefersToPatient ?p. \n";
        queryString2 += "FILTER(?wc>10) \n";
        queryString2 += "}";

        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();

        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            detectedRestlessness.add(str1.substring(index + 1));
            System.out.println("?s = " + solution.getValue("s"));

        }

        System.out.println("RESTLESSNESS PROBLEMS GENERATED: " + detectedRestlessness.size());

        String uniqueID = UUID.randomUUID().toString();
        for (int i = 0; i < detectedRestlessness.size(); i++) {
            String queryString = "";
            uniqueID = detectedRestlessness.get(i);
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "PREFIX : <http://localhost/resources/experimental_db#> ";
            queryString += "INSERT{ \n";
            queryString += "CARL:DailySleepProblem" + uniqueID + " rdf:type CARL:Restlessness. \n";
            queryString += "CARL:DailySleepProblem" + uniqueID + " CARL:dateTime ?dt1.\n";
            //queryString += ":SleepProblem" + uniqueID + " :efficiency ?e1.\n";
            queryString += "?p CARL:hasSleepProblem CARL:SleepProblem" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Patient . \n";
            queryString += "?s a CARL:DailySleep . \n";
            queryString += "?s CARL:dailySleepRefersToPatient ?p. \n";
            queryString += "?s CARL:sleepDateTime ?d1 \n";
            //queryString += "CARL:" + detectedRestlessness.get(i) + " CARL:dateTime ?d1. \n";
            //queryString += ":" + detectedRestlessness.get(i) + " :efficiency ?e. \n";
            //queryString += "CARL:" + detectedRestlessness.get(i) + " CARL:sleepRefersToPatient ?p.\n";
            //queryString += "BIND(?e AS ?e1) \n";
            queryString += "BIND(?d1 AS ?dt1) \n";
            queryString += "}";

            System.out.println(detectedRestlessness.get(i));
            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }

    }

    public void tooManySleepInterruptionsRule(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedTopManySleepInterruptions = new ArrayList<String>();
        //select stepsRefersToPatient
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?sc \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?sc a CARL:SleepCount . \n";
        queryString2 += "?p a CARL:Patient . \n";
        queryString2 += "?sc CARL:sleepCountRefersToPerson ?p. \n";
        queryString2 += "?sc CARL:sleepCountWake ?scw. \n";
        queryString2 += "?sc CARL:sleepRestless ?scr. \n";
        queryString2 += "?sc CARL:sleepCountAwake ?sca. \n";
        queryString2 += "FILTER(?sca + ?scw + ?scr>10) \n";
        queryString2 += "}";

        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();

        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("sc").toString();
            int index = str1.indexOf("#");
            detectedTopManySleepInterruptions.add(str1.substring(index + 1));
            System.out.println("?sc = " + solution.getValue("sc"));

        }

        System.out.println("TOO MANY SLEEP INTERRUPTIONS PROBLEMS GENERATED: " + detectedTopManySleepInterruptions.size());

        String uniqueID = UUID.randomUUID().toString();
        for (int i = 0; i < detectedTopManySleepInterruptions.size(); i++) {
            String queryString = "";
            uniqueID = detectedTopManySleepInterruptions.get(i);
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "PREFIX : <http://localhost/resources/experimental_db#> ";
            queryString += "INSERT{ \n";
            queryString += "CARL:TooManySleepInterruptionsProblem" + uniqueID + " rdf:type CARL:TooManySleepInterruptions. \n";
            queryString += "CARL:TooManySleepInterruptionsProblem" + uniqueID + " CARL:dateTime ?dt1.\n";
            //queryString += ":SleepProblem" + uniqueID + " :efficiency ?e1.\n";
            queryString += "?p CARL:hasSleepProblem CARL:TooManySleepInterruptionsProblem" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Patient . \n";
            queryString += "?sc a CARL:SleepCount . \n";
            queryString += "?sc CARL:SleepCountRefersToPerson ?p. \n";
            queryString += "?sc CARL:sleepDateTime ?d1 \n";
            queryString += "BIND(?d1 AS ?dt1) \n";
            queryString += "}";

            System.out.println(detectedTopManySleepInterruptions.get(i));
            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }

    }

    public void stressOrPainRule(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedStressOrPain = new ArrayList<String>();
        //select stepsRefersToPatient
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?sc \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?sz a CARL:SleepZone . \n";
        queryString2 += "?p a CARL:Patient . \n";
        queryString2 += "?st a CARL:Step . \n";
        queryString2 += "?sz CARL:sleepZoneRefersToPerson ?p. \n";
        queryString2 += "?st CARL:StepRefersToPerson ?p. \n";
        queryString2 += "?sz CARL:sleepZoneName ?szn. \n";
        queryString2 += "?sz CARL:sleepZoneDuration ?szd. \n";
        queryString2 += "?sz CARL:sleepZoneTimestamp ?szt. \n";
        queryString2 += "?st CARL:stepCount ?stc. \n";
        queryString2 += "?st CARL:stepTimestamp ?stt. \n";
        queryString2 += "FILTER(?stc < LOW && stt == szt && szn == FatBurnZone && szd > 5) \n";
        queryString2 += "}";

        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();

        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("sop").toString();
            int index = str1.indexOf("#");
            detectedStressOrPain.add(str1.substring(index + 1));
            System.out.println("?sop = " + solution.getValue("sop"));

        }

        System.out.println("STRESS OR PAIN PROBLEMS GENERATED: " + detectedStressOrPain.size());

        String uniqueID = UUID.randomUUID().toString();
        for (int i = 0; i < detectedStressOrPain.size(); i++) {
            String queryString = "";
            uniqueID = detectedStressOrPain.get(i);
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "PREFIX : <http://localhost/resources/experimental_db#> ";
            queryString += "INSERT{ \n";
            queryString += "CARL:StressOrPainProblem" + uniqueID + " rdf:type CARL:StressOrPain. \n";
            queryString += "CARL:StressOrPainProblem" + uniqueID + " CARL:dateTime ?dt1.\n";
            //queryString += ":SleepProblem" + uniqueID + " :efficiency ?e1.\n";
            queryString += "?p CARL:hasMultiProblem CARL:StressOrPainProblem" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Patient . \n";
            queryString += "?sz a CARL:SleepZone . \n";
            queryString += "?sz CARL:SleepZoneRefersToPerson ?p. \n";
            queryString += "?sz CARL:sleepDateTime ?d1 \n";
            queryString += "BIND(?d1 AS ?dt1) \n";
            queryString += "}";

            System.out.println(detectedStressOrPain.get(i));
            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }

    }


//    //SPIN Rule 2:efficiency<90 && efficiency>0 -->Bad Quality of Sleep
//    public void BadQualityofSleep(RepositoryConnection repositoryConnection) throws IOException {
//        ArrayList<String> detectedEf = new ArrayList<String>();
//        String queryString2 = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
//        queryString2 += "SELECT ?s \n";
//        queryString2 += "WHERE { \n";
//        queryString2 += "?p a :Patient . \n";
//        queryString2 += "?s a :Sleep.  \n";
//        queryString2 += "?s :efficiency ?e1. \n";
//        queryString2 += "?s :sleepRefersToPatient ?p. \n";
//        queryString2 += "FILTER(?e1<90 && ?e1>0) \n";
//        queryString2 += "}";
//
//        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
//        TupleQueryResult result = query.evaluate();
//
//        while (result.hasNext()) {
//            BindingSet solution = result.next();
//            // ... and print out the value of the variable binding for ?s and ?n
//            String str1 = solution.getValue("s").toString();
//            int index = str1.indexOf("#");
//            detectedEf.add(str1.substring(index + 1));
//            //System.out.println("?s = " + solution.getValue("s"));
//        }
//
//        System.out.println("BAD QUALITY OF SLEEP PROBLEMS GENERATED : " + detectedEf.size());
//
//        String uniqueID = UUID.randomUUID().toString();
//        for (int i = 0; i < detectedEf.size(); i++) {
//            String queryString = "";
//            uniqueID = detectedEf.get(i);
//            queryString = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
//            queryString += "INSERT{ \n";
//            queryString += ":SleepProblem" + uniqueID + " rdf:type :BadQualityofSleep. \n";
//            queryString += ":SleepProblem" + uniqueID + " :dateTime ?dt2.\n";
//            queryString += ":SleepProblem" + uniqueID + " :efficiency ?ef1.\n";
//            queryString += "?p :hasSleepProblem :SleepProblem" + uniqueID + ".\n";
//            queryString += "}\n";
//            queryString += "WHERE { \n";
//            queryString += "?p a :Patient . \n";
//            queryString += ":" + detectedEf.get(i) + " :dateTime ?s2. \n";
//            queryString += ":" + detectedEf.get(i) + " :efficiency ?ef. \n";
//            queryString += ":" + detectedEf.get(i) + " :sleepRefersToPatient ?p.\n";
//            queryString += "BIND(?s2 AS ?dt2) \n";
//            queryString += "BIND(?ef AS ?ef1) \n";
//            queryString += "}";
//
//            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
//            operation.execute();
//        }
//    }
//SPIN Rule 2:minutes_asleep<90 && minutes_asleep>0 -->Lack of Sleep

    public void TooMuchSleep(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedTMS = new ArrayList<String>();
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?s \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?p a CARL:Patient . \n";
        queryString2 += "?s a CARL:Sleep.  \n";
        queryString2 += "?s CARL:minutes_a_sleep ?m1. \n";
        queryString2 += "?s CARL:sleepRefersToPatient ?p. \n";
        queryString2 += "FILTER(?m1>480) \n";
        queryString2 += "}";
        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();
        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            detectedTMS.add(str1.substring(index + 1));
            //System.out.println("?s = " + solution.getValue("s"));
        }

        System.out.println("TOO MUCH SLEEP PROBLEMS GENERATED : " + detectedTMS.size());

        String uniqueID = "";
        for (int i = 0; i < detectedTMS.size(); i++) {
            uniqueID = detectedTMS.get(i);;
            String queryString = "";
            queryString = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "INSERT{ \n";
            queryString += "CARL:TooMuchSleepProblem" + uniqueID + " rdf:type CARL:TooMuchSleep. \n";
            queryString += "CARL:TooMuchSleepProblem" + uniqueID + " CARL:dateTime ?dt2.\n";
            queryString += "CARL:TooMuchSleepProblem" + uniqueID + " CARL:minutes_a_sleep ?m1.\n";
            queryString += "?p CARL:hasSleepProblem CARL:TooMuchSleepProblem" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a :Patient . \n";
            queryString += "CARL:" + detectedTMS.get(i) + " CARL:dateTime ?s2. \n";
            queryString += "CARL:" + detectedTMS.get(i) + " CARL:minutes_a_sleep ?m. \n";
            queryString += "CARL:" + detectedTMS.get(i) + " CARL:sleepRefersToPatient ?p.\n";
            queryString += "BIND(?s2 AS ?dt2) \n";
            queryString += "BIND(?m AS ?m1) \n";
            queryString += "}";

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }}

    public void IncreasedNapping(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedIncreasedNapping = new ArrayList<String>();

        //select stepsRefersToPatient
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?sc \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?sc a CARL:NumberOfAsleepInNapsCount . \n";
        queryString2 += "?p a CARL:Patient . \n";
        queryString2 += "?sc CARL:numberOfAsleepInNapsCountRefersToPerson ?p. \n";
        queryString2 += "?sc CARL:numberOfAsleepInNaps ?napNumber. \n";
        queryString2 += "FILTER(?napNumber>5) \n";
        queryString2 += "}";

        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();

        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("sc").toString();
            int index = str1.indexOf("#");
            detectedIncreasedNapping.add(str1.substring(index + 1));
            System.out.println("?sc = " + solution.getValue("sc"));

        }

        System.out.println("INCREASED NAPPING PROBLEMS GENERATED: " + detectedIncreasedNapping.size());

        String uniqueID = UUID.randomUUID().toString();
        for (int i = 0; i < detectedIncreasedNapping.size(); i++) {
            String queryString = "";
            uniqueID = detectedIncreasedNapping.get(i);
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "PREFIX : <http://localhost/resources/experimental_db#> ";
            queryString += "INSERT{ \n";
            queryString += "CARL:IncreasedNapping_" + uniqueID + " rdf:type CARL:SleepProblem. \n";
            queryString += "CARL:IncreasedNapping_" + uniqueID + " CARL:dateTime ?dt1.\n";
            //queryString += ":SleepProblem" + uniqueID + " :efficiency ?e1.\n";
            queryString += "?p CARL:hasSleepProblem CARL:IncreasedNapping_" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Patient . \n";
            queryString += "?sc a CARL:NumberOfAsleepInNapsCount . \n";
            queryString += "?sc CARL:numberOfAsleepInNapsCountRefersToPerson ?p. \n";
            queryString += "?sc CARL:numberOfAsleepInNapsDateTime ?d1 \n";
            queryString += "BIND(?d1 AS ?dt1) \n";
            queryString += "}";

            System.out.println(detectedIncreasedNapping.get(i));
            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }

    }

    public void napCloseToBedtime(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedNapCloseToBedtime = new ArrayList<String>();
        //select stepsRefersToPatient
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?sc \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?sc a CARL:NapEndTimeCloseToSleepStartTimeObject . \n";
        queryString2 += "?p a CARL:Patient . \n";
        queryString2 += "?sc CARL:napEndTimeCloseToSleepStartTimeObjectRefersToPerson ?p. \n";
        queryString2 += "?sc CARL:napEndTimeCloseToSleepStartTime ?bv. \n";
        queryString2 += "FILTER(?bv=\"true\"^^xsd:boolean) \n";
        queryString2 += "}";

        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();

        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("sc").toString();
            int index = str1.indexOf("#");
            detectedNapCloseToBedtime.add(str1.substring(index + 1));
            System.out.println("?sc = " + solution.getValue("sc"));

        }

        System.out.println("NAP CLOSE TO BEDTIME PROBLEMS GENERATED: " + detectedNapCloseToBedtime.size());

        String uniqueID = UUID.randomUUID().toString();
        for (int i = 0; i < detectedNapCloseToBedtime.size(); i++) {
            String queryString = "";
            uniqueID = detectedNapCloseToBedtime.get(i);
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "PREFIX : <http://localhost/resources/experimental_db#> ";
            queryString += "INSERT{ \n";
            queryString += "CARL:NapEndTimeCloseToSleepStartTimeProblem_" + uniqueID + " rdf:type CARL:SleepProblem. \n";
            queryString += "CARL:NapEndTimeCloseToSleepStartTimeProblem_" + uniqueID + " CARL:dateTime ?dt1.\n";
            //queryString += ":SleepProblem" + uniqueID + " :efficiency ?e1.\n";
            queryString += "?p CARL:hasSleepProblem CARL:NapEndTimeCloseToSleepStartTimeProblem_" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Patient . \n";
            queryString += "?sc a CARL:NapEndTimeCloseToSleepStartTimeObject . \n";
            queryString += "?sc CARL:napEndTimeCloseToSleepStartTimeObjectRefersToPerson ?p. \n";
            queryString += "?sc CARL:napEndTimeCloseToSleepStartTimeDateTime ?d1 \n";
            queryString += "BIND(?d1 AS ?dt1) \n";
            queryString += "}";

            System.out.println(detectedNapCloseToBedtime.get(i));
            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }

    }

    public void LackofSleep(RepositoryConnection repositoryConnection) throws IOException {

        ArrayList<String> detectedLoS = new ArrayList<String>();
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?s \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?p a CARL:Person . \n";
        queryString2 += "?s a CARL:FitbitDailySleepSensorMeasurement.  \n";
        queryString2 += "?s CARL:minutes_a_sleep ?m1. \n";
        queryString2 += "?s CARL:dailySleepRefersToPerson ?p. \n";
        queryString2 += "FILTER(?m1<300 && ?m1>0) \n";
        queryString2 += "}";

        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();
        while (result.hasNext()) {
            BindingSet solution = result.next();
           // System.out.println("koita dw");
            //System.out.println(solution);
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            detectedLoS.add(str1.substring(index + 1));
            //System.out.println("?s = " + solution.getValue("s"));
        }

        System.out.println("LACK OF SLEEP PROBLEMS GENERATED : " + detectedLoS.size());
        detectedLoS.forEach(System.out::println);
        String uniqueID = "";
        for (int i = 0; i < detectedLoS.size(); i++) {
            uniqueID = detectedLoS.get(i);

            String queryString = "";
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "INSERT { \n";
            queryString += "CARL:LackOfSleep" + uniqueID + " rdf:type CARL:LackOfSleep. \n";
            queryString += "CARL:LackOfSleep" + uniqueID + " CARL:lackOfSleep_DateTime ?dt2.\n";
            queryString += "CARL:LackOfSleep" + uniqueID + " CARL:lackOfSleep_minutes_a_sleep ?m1.\n";
            queryString += "?p CARL:hasSleepProblem CARL:LackOfSleep"+ uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Person . \n";
            queryString += "CARL:" + detectedLoS.get(i) + " CARL:datetimeDailySleep ?s2. \n";
            queryString += "CARL:" + detectedLoS.get(i) + " CARL:minutes_a_sleep ?m. \n";
            queryString += "CARL:" + detectedLoS.get(i) + " CARL:dailySleepRefersToPerson ?p.\n";
            queryString += "BIND(?s2 AS ?dt2) \n";
            queryString += "BIND(?m AS ?m1) \n";
            queryString += "}";

            System.out.println(queryString);

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();

        }
    }

        //SPIN Rule 3:minutes_asleep>480 -->Too much Sleep

    //SPIN Rule 4:HR<60 -->Low HR

    public void LowHR(RepositoryConnection repositoryConnection) throws IOException {
        ArrayList<String> detectedLHR = new ArrayList<String>();
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?s \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?p a CARL:Person . \n";
        queryString2 += "?s a CARL:DailyHeartRateMeasurement.  \n";
        queryString2 += "?s CARL:restingHeartRate ?h1. \n";
        queryString2 += "?s CARL:dailyHeartRateRefersToPerson ?p. \n";
        queryString2 += "FILTER(?h1<75) \n";
        queryString2 += "}";
        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result = query.evaluate();
        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            detectedLHR.add(str1.substring(index + 1));
            //System.out.println("?s = " + solution.getValue("s"));
        }

        System.out.println("LOW HEARTRATE PROBLEMS GENERATED : " + detectedLHR.size());

        String uniqueID = "";
        for (int i = 0; i < detectedLHR.size(); i++) {
            uniqueID = detectedLHR.get(i);;
            String queryString = "";
            queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString += "INSERT{ \n";
            queryString += "CARL:LowHeartRateProblem" + uniqueID + " rdf:type CARL:LowHeartRateProblem. \n";
            queryString += "CARL:LowHeartRateProblem" + uniqueID + " CARL:lowHeartRateProblemDatetime ?dt2.\n";
            queryString += "CARL:LowHeartRateProblem" + uniqueID + " CARL:lowHeartRateValue ?h1.\n";
            queryString += "?p CARL:hasHeartRateProblem CARL:LowHeartRateProblem" + uniqueID + ".\n";
            queryString += "}\n";
            queryString += "WHERE { \n";
            queryString += "?p a CARL:Person . \n";
            queryString += "CARL:" + detectedLHR.get(i) + " CARL:dailyHeartRateDatetime ?d2. \n";
            queryString += "CARL:" + detectedLHR.get(i) + " CARL:restingHeartRate ?h. \n";
            queryString += "CARL:" + detectedLHR.get(i) + " CARL:dailyHeartRateRefersToPerson ?p.\n";
            queryString += "BIND(?d2 AS ?dt2) \n";
            queryString += "BIND(?h AS ?h1) \n";
            queryString += "}";

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
            operation.execute();
        }
    }

    public void StressOrPain(RepositoryConnection repositoryConnection) throws IOException {

        for (User user : users) {
            String fatBurnZoneThreshold = String.valueOf(user.getUserThresholds().getFatBurnZone());
            String stepsLowThreshold = String.valueOf(user.getUserThresholds().getSteps_Low());
            String stressOrPainDurationThreshold = String.valueOf(user.getUserThresholds().getStressOrPainDuration());

            ArrayList < String > detectedSoP = new ArrayList<String>();
            String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "PREFIX : <http://localhost/resources/experimental_db#> ";
            queryString2 += "SELECT ?s \n";
            queryString2 += "WHERE { \n";
            queryString2 += "?p a CARL:Patient . \n";
            queryString2 += "?s a CARL:Step.  \n";
            queryString2 += "?s CARL:rate ?r1. \n";
            queryString2 += "?s CARL:minutes ?duration. \n";
            queryString2 += "?s CAR;:stepsRefersToPatient ?p. \n";
            queryString2 += "?s2 a CARL:Sleep.  \n";
            queryString2 += "?s2 CARL:heartRateValue ?h1. \n";
            queryString2 += "?s2 CARL:sleepRefersToPatient ?p. \n";

            queryString2 += "FILTER(?h1>" + fatBurnZoneThreshold + ") . \n";
            queryString2 += "FILTER(?r1<" + stepsLowThreshold + ") .\n";
            queryString2 += "FILTER(?duration>" + stressOrPainDurationThreshold + ") \n";
            queryString2 += "}";
            TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
            TupleQueryResult result = query.evaluate();
            while (result.hasNext()) {
                BindingSet solution = result.next();
                // ... and print out the value of the variable binding for ?s and ?n
                String str1 = solution.getValue("s").toString();
                int index = str1.indexOf("#");
                detectedSoP.add(str1.substring(index + 1));
                //System.out.println("?s = " + solution.getValue("s"));
            }

            System.out.println("STRESS OR PAIN PROBLEMS GENERATED : " + detectedSoP.size());

            String uniqueID = "";
            for (int i = 0; i < detectedSoP.size(); i++) {
                uniqueID = detectedSoP.get(i);
                ;
                String queryString = "";
                queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
                queryString += "PREFIX : <http://localhost/resources/experimental_db#> ";
                queryString += "INSERT{ \n";
                queryString += "CARL:StressOrPainProblem" + uniqueID + " rdf:type CARL:StressOrPain. \n";
                queryString += "CARL:StressOrPainProblem" + uniqueID + " CARL:dateTime ?dt2.\n";
                //queryString += ":StressOrPainProblem" + uniqueID + " :steps ?s2.\n";
                //queryString += ":StressOrPainProblem" + uniqueID + " :heartRateValue ?h2.\n";
                queryString += "?p CARL:hasStressOrPainProblem CARL:stressOrPainProblem" + uniqueID + ".\n";
                queryString += "}\n";
                queryString += "WHERE { \n";
                queryString += "?p a CARL:Patient . \n";
                queryString += ":" + detectedSoP.get(i) + " CARL:dateTime ?dt1. \n";
                //queryString += ":" + detectedSoP.get(i) + " :heartRateValue ?h1. \n";
                //queryString += ":" + detectedSoP.get(i) + " :steps ?s1. \n";
                queryString += ":" + detectedSoP.get(i) + " CARL:stepsRefersToPatient ?p.\n";

                queryString += "BIND(?dt1 AS ?dt2) .\n";
//                queryString += "BIND(?h1 AS ?h2) .\n";
//                queryString += "BIND(?s1 AS ?s2) \n";

                queryString += "}";

                Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
                operation.execute();
            }
        }
    }


    public void Restlessness(RepositoryConnection repositoryConnection) throws IOException {

        for (User user : users) {
            String noOfInterruptionsInADayThreshold = String.valueOf(user.getUserThresholds().getRestlessness_NumberOfInterruptionsInADay());

            ArrayList < String > detectedRestless = new ArrayList<String>();
            String queryString2 = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "SELECT ?s \n";
            queryString2 += "WHERE { \n";
            queryString2 += "?p a :Patient . \n";
            queryString2 += "?s a :Sleep.  \n";
            queryString2 += "?s :restlessCount ?r1. \n";
            queryString2 += "?s :stepsRefersToPatient ?p. \n";

            queryString2 += "FILTER(?r1>" + noOfInterruptionsInADayThreshold + ")  \n";
            queryString2 += "}";
            TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
            TupleQueryResult result = query.evaluate();
            while (result.hasNext()) {
                BindingSet solution = result.next();
                // ... and print out the value of the variable binding for ?s and ?n
                String str1 = solution.getValue("s").toString();
                int index = str1.indexOf("#");
                detectedRestless.add(str1.substring(index + 1));
                //System.out.println("?s = " + solution.getValue("s"));
            }

            System.out.println("RESTLESSNESS PROBLEMS GENERATED : " + detectedRestless.size());

            String uniqueID = "";
            for (int i = 0; i < detectedRestless.size(); i++) {
                uniqueID = detectedRestless.get(i);
                ;
                String queryString = "";
                queryString = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
                queryString += "INSERT{ \n";
                queryString += ":RestlessnessProblem" + uniqueID + " rdf:type :Restlessness. \n";
                queryString += ":RestlessnessProblem" + uniqueID + " :dateTime ?dt2.\n";
                queryString += ":RestlessnessProblem" + uniqueID + " :restlessCount ?s2.\n";
                queryString += "?p :hasRestlessnessProblem :restlessnessProblem" + uniqueID + ".\n";
                queryString += "}\n";
                queryString += "WHERE { \n";
                queryString += "?p a :Patient . \n";
                queryString += ":" + detectedRestless.get(i) + " :dateTime ?dt1. \n";
                queryString += ":" + detectedRestless.get(i) + " :restlessCount ?s1. \n";
                queryString += ":" + detectedRestless.get(i) + " :sleepRefersToPatient ?p.\n";

                queryString += "BIND(?dt1 AS ?dt2) .\n";
                queryString += "BIND(?s1 AS ?s2) \n";

                queryString += "}";

                Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
                operation.execute();
            }
        }
    }

    public void Inactivity(RepositoryConnection repositoryConnection) throws IOException {

        for (User user : users) {
            String stepsMediumThreshold = String.valueOf(user.getUserThresholds().getSteps_Medium());
            String fatBurnZoneThreshold = String.valueOf(user.getUserThresholds().getFatBurnZone());
            String inactivityDurationThreshold = String.valueOf(user.getUserThresholds().getInactivity_Duration());

            ArrayList < String > detectedInactivity = new ArrayList<String>();
            String queryString2 = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "SELECT ?s \n";
            queryString2 += "WHERE { \n";
            queryString2 += "?p a :Patient . \n";
            queryString2 += "?s a :Step.  \n";
            queryString2 += "?s :rate ?r1. \n";
            queryString2 += "?s :minutes ?duration. \n";
            queryString2 += "?s :stepsRefersToPatient ?p. \n";
            queryString2 += "?s2 a :Sleep.  \n";
            queryString2 += "?s2 :heartRateValue ?h1. \n";
            queryString2 += "?s2 :sleepRefersToPatient ?p. \n";

            queryString2 += "FILTER(?h1>" + fatBurnZoneThreshold + ") . \n";
            queryString2 += "FILTER(?duration>" + inactivityDurationThreshold + ") \n";
            queryString2 += "}";
            TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
            TupleQueryResult result = query.evaluate();
            while (result.hasNext()) {
                BindingSet solution = result.next();
                // ... and print out the value of the variable binding for ?s and ?n
                String str1 = solution.getValue("s").toString();
                int index = str1.indexOf("#");
                detectedInactivity.add(str1.substring(index + 1));
                //System.out.println("?s = " + solution.getValue("s"));
            }

            System.out.println("INACTIVITY PROBLEMS GENERATED : " + detectedInactivity.size());

            String uniqueID = "";
            for (int i = 0; i < detectedInactivity.size(); i++) {
                uniqueID = detectedInactivity.get(i);
                ;
                String queryString = "";
                queryString = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
                queryString += "INSERT{ \n";
                queryString += ":InactivityProblem" + uniqueID + " rdf:type :Inactivity. \n";
                queryString += ":InactivityProblem" + uniqueID + " :dateTime ?dt2.\n";
                queryString += ":InactivityProblem" + uniqueID + " :steps ?s2.\n";
                queryString += ":InactivityProblem" + uniqueID + " :heartRateValue ?h2.\n";
                queryString += "?p :hasInactivityProblem :inactivityProblem" + uniqueID + ".\n";
                queryString += "}\n";
                queryString += "WHERE { \n";
                queryString += "?p a :Patient . \n";
                queryString += ":" + detectedInactivity.get(i) + " :dateTime ?dt1. \n";
                queryString += ":" + detectedInactivity.get(i) + " :heartRateValue ?h1. \n";
                queryString += ":" + detectedInactivity.get(i) + " :steps ?s1. \n";
                queryString += ":" + detectedInactivity.get(i) + " :stepsRefersToPatient ?p.\n";

                queryString += "BIND(?dt1 AS ?dt2) .\n";
                queryString += "BIND(?h1 AS ?h2) .\n";
                queryString += "BIND(?s1 AS ?s2) \n";

                queryString += "}";

                Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
                operation.execute();
            }
        }
    }

    public void Insomnia(RepositoryConnection repositoryConnection) throws IOException {

        for (User user : users) {
            String timeToFallAsleepThreshold = String.valueOf(user.getUserThresholds().getInsomnia_TimeToFallAsleep());

            ArrayList<String> detectedInsomnia = new ArrayList<String>();
            //select stepsRefersToPatient
            String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "PREFIX : <http://localhost/resources/experimental_db#> ";
            queryString2 += "WHERE { \n";
            queryString2 += "?p a CARL:Patient . \n";
            queryString2 += "?s a CARL:Sleep.  \n";
            queryString2 += "?s CARL:minutesToFallAsleep ?t1. \n";
            queryString2 += "?s CARL:sleepRefersToPatient ?p. \n";
            queryString2 += "FILTER(?t1<"+timeToFallAsleepThreshold+"&& ?t1>0) \n";
            queryString2 += "}";

            TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
            TupleQueryResult result = query.evaluate();

            while (result.hasNext()) {
                BindingSet solution = result.next();
                // ... and print out the value of the variable binding for ?s and ?n
                String str1 = solution.getValue("s").toString();
                int index = str1.indexOf("#");
                detectedInsomnia.add(str1.substring(index + 1));
                //System.out.println("?s = " + solution.getValue("s"));

            }

            System.out.println("INSOMNIA PROBLEMS GENERATED : " + detectedInsomnia.size());

            String uniqueID = UUID.randomUUID().toString();
            for (int i = 0; i < detectedInsomnia.size(); i++) {
                String queryString = "";
                uniqueID = detectedInsomnia.get(i);
                queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
                queryString += "PREFIX : <http://localhost/resources/experimental_db#> ";
                queryString += "INSERT{ \n";
                queryString += "CARL:SleepProblem" + uniqueID + " rdf:type CARL:Insomnia. \n";
                queryString += "CARL:SleepProblem" + uniqueID + " CARL:dateTime ?dt1.\n";
                queryString += "?p CARL:hasSleepProblem CARL:SleepProblem" + uniqueID + ".\n";
                queryString += "}\n";
                queryString += "WHERE { \n";
                queryString += "?p a CARL:Patient . \n";
                queryString += ":" + detectedInsomnia.get(i) + " CARL:dateTime ?s1. \n";
                queryString += ":" + detectedInsomnia.get(i) + " CARL:sleepRefersToPatient ?p.\n";
                queryString += "BIND(?s1 AS ?dt1) \n";
                queryString += "}";

                Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
                operation.execute();
            }

        }
    }

    public void LackOfExercise(RepositoryConnection repositoryConnection) throws IOException {

        for (User user : users) {
            String stepsMediumThreshold = String.valueOf(user.getUserThresholds().getSteps_Medium());
            String fatBurnZoneThreshold = String.valueOf(user.getUserThresholds().getFatBurnZone());
            String inactivityDurationThreshold = String.valueOf(user.getUserThresholds().getLackOfExercise_Duration());

            ArrayList < String > detectedLackOfExercise = new ArrayList<String>();
            String queryString2 = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryString2 += "SELECT ?s \n";
            queryString2 += "WHERE { \n";
            queryString2 += "?p a :Patient . \n";
            queryString2 += "?s a :Step.  \n";
            queryString2 += "?s :rate ?r1. \n";
            queryString2 += "?s :minutes ?duration. \n";
            queryString2 += "?s :stepsRefersToPatient ?p. \n";
            queryString2 += "?s2 a :Sleep.  \n";
            queryString2 += "?s2 :heartRateValue ?h1. \n";
            queryString2 += "?s2 :sleepRefersToPatient ?p. \n";

            queryString2 += "FILTER(?h1<" + fatBurnZoneThreshold + ") . \n";
            queryString2 += "FILTER(?r1>" + stepsMediumThreshold + ") .\n";
            queryString2 += "FILTER(?duration>" + inactivityDurationThreshold + ") \n";
            queryString2 += "}";
            TupleQuery query = repositoryConnection.prepareTupleQuery(queryString2);
            TupleQueryResult result = query.evaluate();
            while (result.hasNext()) {
                BindingSet solution = result.next();
                // ... and print out the value of the variable binding for ?s and ?n
                String str1 = solution.getValue("s").toString();
                int index = str1.indexOf("#");
                detectedLackOfExercise.add(str1.substring(index + 1));
                //System.out.println("?s = " + solution.getValue("s"));
            }

            System.out.println("LACK OF EXERCISE PROBLEMS GENERATED : " + detectedLackOfExercise.size());

            String uniqueID = "";
            for (int i = 0; i < detectedLackOfExercise.size(); i++) {
                uniqueID = detectedLackOfExercise.get(i);
                ;
                String queryString = "";
                queryString = "PREFIX : <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
                queryString += "INSERT{ \n";
                queryString += ":LackOfExerciseProblem" + uniqueID + " rdf:type :LackOfExercise. \n";
                queryString += ":LackOfExerciseProblemyProblem" + uniqueID + " :dateTime ?dt2.\n";
                queryString += ":LackOfExerciseProblem" + uniqueID + " :steps ?s2.\n";
                queryString += ":LackOfExerciseProblem" + uniqueID + " :heartRateValue ?h2.\n";
                queryString += "?p :hasLackOfExerciseProblem :lackOfExerciseProblem" + uniqueID + ".\n";
                queryString += "}\n";
                queryString += "WHERE { \n";
                queryString += "?p a :Patient . \n";
                queryString += ":" + detectedLackOfExercise.get(i) + " :dateTime ?dt1. \n";
                queryString += ":" + detectedLackOfExercise.get(i) + " :heartRateValue ?h1. \n";
                queryString += ":" + detectedLackOfExercise.get(i) + " :steps ?s1. \n";
                queryString += ":" + detectedLackOfExercise.get(i) + " :stepsRefersToPatient ?p.\n";

                queryString += "BIND(?dt1 AS ?dt2) .\n";
                queryString += "BIND(?h1 AS ?h2) .\n";
                queryString += "BIND(?s1 AS ?s2) \n";

                queryString += "}";

                Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryString);
                operation.execute();
            }
        }
    }

    }
//
//    public void populateYpostirizoDataWithSPARQL(VAComponentResults vaResults){
//
//        /* detection */
//        //String detectionURI = Vocabulary.NAMESPACE_TBOX + "detection_" + UUID.randomUUID().toString();
//        String detectionURI = Vocabulary.NAMESPACE_TBOX + "detection_" + counter++;
//
//        String strInsert =  "<" + detectionURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Detection" + "> ." //detection_ABC rdf:type sot:Detection
//                + "<" + detectionURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasTimestamp" 	+ "> \"" + vaResults.getTimestamp() + "\"^^xsd:dateTime" + " ." //detection sot:hasTimestamp timestamp
//                + "<" + detectionURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasImageName" 	+ "> \"" + vaResults.getImageName() + "\"^^xsd:string" + " ."
//                + "<" + detectionURI + "> <" + Vocabulary.NAMESPACE_TBOX + "providedBy" 	+ "> <" + Vocabulary.NAMESPACE_TBOX + "camera" + "> ." //detection sot:providedBy camera
//                + "<" + Vocabulary.NAMESPACE_TBOX + "camera" + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Camera" + "> ." //camera is a sensor of Camera type
//                ;
//
//        /* semantic space */
//        if (!vaResults.getSceneType().trim().isEmpty()){
//
//            String semanticSpaceURI = Vocabulary.NAMESPACE_TBOX + vaResults.getSceneType();
//
//            strInsert = strInsert
//                    + "<" + detectionURI + "> <" + Vocabulary.NAMESPACE_TBOX + "detectsSemanticSpace" + "> <" + semanticSpaceURI + "> ." //detection sot:detectsSemanticSpace spaceXY
//                    + "<" + semanticSpaceURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "SemanticSpace" + "> ." //semantic space rdf:type sot:SemanticSpace
//                    + "<" + semanticSpaceURI + "> <" + RDFS.LABEL + "> \"" + Vocabulary.getLabelOfSemanticSpaceConcept(vaResults.getSceneType()) + "\"^^xsd:string" + " ." //semantic space rdfs:label label
//                    + "<" + detectionURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasConfidence" + "> \"" + (new Float(vaResults.getSceneScore().toString())).toString() + "\"^^xsd:float" + " ." //detection sot:hasConfidence 0.77
//            ;
//        }
//
//        /* grid space */
//        String detectionGridSpaceURI = Vocabulary.NAMESPACE_TBOX + "detection_grid_space_" + UUID.randomUUID().toString();
//        strInsert = strInsert
//                + "<" + detectionURI + "> <" + Vocabulary.NAMESPACE_TBOX + "locatedInGridSpace" + "> <" + detectionGridSpaceURI + "> ." //detection locatedInGridSpace detectionSpaceXY
//                + "<" + detectionGridSpaceURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "GridSpace" + "> ." //detectionSpaceXY rdf:type sot:GridSpace
//                + "<" + detectionGridSpaceURI + "> <" + Vocabulary.NAMESPACE_TBOX + "width" + "> \"" + (new Integer(vaResults.getImageWidth().toString())).toString() + "\"^^xsd:integer" + " ." //detectionSpaceXY sot:width 800
//                + "<" + detectionGridSpaceURI + "> <" + Vocabulary.NAMESPACE_TBOX + "height" + "> \"" + (new Integer(vaResults.getImageHeight().toString())).toString() + "\"^^xsd:integer" + " ." //detectionSpaceXY sot:height 600
//        ;
//
//        /* detected entities */
//        ArrayList<VAEntity> entitiesFound = vaResults.getEntitiesFound();
//
//        for (VAEntity entity : entitiesFound) {
//            if (entity.getType().contains("person")) {
//                /* detected persons */
//                String personURI = "";
//                // known/unknown person
//                if (entity.getType().equals("person_unknown"))
//                    personURI = Vocabulary.NAMESPACE_TBOX + "person_" + UUID.randomUUID().toString();
//                else //person is known (person_xyz)
////					personURI = Vocabulary.NAMESPACE_TBOX + entity.getType().toLowerCase(); //TODO: altered because the other relations were attached into one single instance and queries did not operate properly
//                    personURI = Vocabulary.NAMESPACE_TBOX + entity.getType().toLowerCase() + "_" + UUID.randomUUID().toString();
//
//
//                strInsert = strInsert
//                        + "<" + detectionURI + "> <" + Vocabulary.NAMESPACE_TBOX + "detectsPerson" + "> <" + personURI + "> ." //detection detectsPerson person
//                ;
//
//                // known/unknown person
//                if (entity.getType().equals("person_unknown"))
//                    strInsert = strInsert
//                            + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "UnknownPerson" + "> ."
//                            + "<" + personURI + "> <" + RDFS.LABEL + "> \"" + entity.getType().replaceFirst("person_unknown", "unknown_person") + "\"^^xsd:string" + " ." // add name as label
//                            ;
//                else if (entity.getType().contains("person_")){
//                    //System.out.println("Mphka edw");
//                    strInsert = strInsert
//                            + "<" + personURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "KnownPerson" + "> ."
//                            + "<" + personURI + "> <" + RDFS.LABEL + "> \"" + entity.getType().replaceFirst("person_", "") + "\"^^xsd:string" + " ." // add name as label
//                    ;
//                }
//                //System.out.println(strInsert);
//                //distance from person
//                String spatialContextURI = Vocabulary.NAMESPACE_TBOX + "spatialContext_" + UUID.randomUUID().toString();
//                strInsert = strInsert
//                        //+ "<" + personURI + "> <" + Vocabulary.NAMESPACE_TBOX + "facemask" + "> <" + "> \"" + entity.getFacemask() + "\"^^xsd:string" + " ."
//                        + "<" + personURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasSpatialContext" + "> <" + spatialContextURI + "> ." //person hasSpatialContext spatialContext
//                        + "<" + spatialContextURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "AbsoluteDistanceSpatialContext" + "> ."
//                        + "<" + spatialContextURI + "> <" + Vocabulary.NAMESPACE_TBOX + "definesAbsoluteDistance" + "> \"" + (new Float(entity.getDistance())).toString() + "\"^^xsd:float" + " ."
//                ;
//                //hasOccupiedArea
//                String entityOccupiedAreaURI = Vocabulary.NAMESPACE_TBOX + "occupiedArea_" + UUID.randomUUID().toString();
//                strInsert = strInsert
//                        + "<" + personURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasOccupiedArea" + "> <" + entityOccupiedAreaURI + "> ." //person hasOccupiedArea occupiedAreaXYZ
//                        + "<" + entityOccupiedAreaURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "OccupiedArea" + "> ."
//                        + "<" + entityOccupiedAreaURI + "> <" + Vocabulary.NAMESPACE_TBOX + "xPosition" + "> \"" + (new Integer(entity.getLeft().toString())).toString() + "\"^^xsd:integer" + " ." //occupiedAreaXYZ sot:width 200
//                        + "<" + entityOccupiedAreaURI + "> <" + Vocabulary.NAMESPACE_TBOX + "yPosition" + "> \"" + (new Integer(entity.getTop().toString())).toString() + "\"^^xsd:integer" + " ." //occupiedAreaXYZ sot:height 10
//                        + "<" + entityOccupiedAreaURI + "> <" + Vocabulary.NAMESPACE_TBOX + "width" + "> \"" + (new Integer(entity.getWidth().toString())).toString() + "\"^^xsd:integer" + " ." //occupiedAreaXYZ sot:width 123
//                        + "<" + entityOccupiedAreaURI + "> <" + Vocabulary.NAMESPACE_TBOX + "height" + "> \"" + (new Integer(entity.getHeight().toString())).toString() + "\"^^xsd:integer" + " ." //occupiedAreaXYZ sot:height 456
//                ;
//                //hasXGridSpace
//                String entityXGridSpaceURI = Vocabulary.NAMESPACE_TBOX + "xGridSpace_" + UUID.randomUUID().toString();
//                strInsert = strInsert
//                        + "<" + personURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasXGridSpace" + "> <" + entityXGridSpaceURI + "> ." //person hasXGridSpace xGridSpaceXY
//                        + "<" + entityXGridSpaceURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "XGridSpace" + "> ."
//                        + "<" + entityXGridSpaceURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasOffset" + "> \"" + entity.getOffset() + "\"^^xsd:integer" + " ."
//                        + "<" + entityXGridSpaceURI + "> <" + Vocabulary.NAMESPACE_TBOX + "positionedInXGrid" + "> \"" + entity.getPositionInXGridSpace() + "\"^^xsd:string" + " ."//xGridSpaceXY sot:positionedInXGrid "middle"
//                ;
//            }
//            else{
//                /* detected objects */
//                String objectURI = Vocabulary.NAMESPACE_TBOX + entity.getType() + "_" + UUID.randomUUID().toString();
////				String objectURI = Vocabulary.NAMESPACE_TBOX + entity.getType(); // without random URI. Counter will define how many of them exist
//
//                strInsert = strInsert
//                        + "<" + detectionURI + "> <" + Vocabulary.NAMESPACE_TBOX + "detectsObject" + "> <" + objectURI + "> ."
//                        + "<" + objectURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "Object" + "> ."
////						+ "<" + objectURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasTotalNo" + "> \"" + entity.getTotalNo().toString() + "\"^^xsd:integer" + " ." // number of objects of that type detected
//                ;
//
//                //distance from object
//                String spatialContextURI = Vocabulary.NAMESPACE_TBOX + "spatialContext_" + UUID.randomUUID().toString();
//                strInsert = strInsert
//                        + "<" + objectURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasSpatialContext" + "> <" + spatialContextURI + "> ." //object hasSpatialContext spatialContext
//                        + "<" + spatialContextURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "AbsoluteDistanceSpatialContext" + "> ."
//                        + "<" + spatialContextURI + "> <" + Vocabulary.NAMESPACE_TBOX + "definesAbsoluteDistance" + "> \"" + (new Float(entity.getDistance())).toString() + "\"^^xsd:float" + " ."
//                ;
//
//                // if object contained in HashMap then getClassURI else use only objectClass
//                if (Vocabulary.isObjectContainedInHashMap(entity.getType()))
//
//                    strInsert = strInsert
//                            + "<" + objectURI + "> <" + RDF.TYPE + "> <" + Vocabulary.getClassURIOfObjectType(entity.getType()) + "> ."
//                            + "<" + objectURI + "> <" + RDFS.LABEL + "> \"" + Vocabulary.getLabelOfSemanticEntityConcept(entity.getType().toLowerCase()) + "\"^^xsd:string" + " .";
//                ;
//                //System.out.println(entity.getType());
//                //hasOccupiedArea
//                String entityOccupiedAreaURI = Vocabulary.NAMESPACE_TBOX + "occupiedArea_" + UUID.randomUUID().toString();
//                strInsert = strInsert
//                        + "<" + objectURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasOccupiedArea" + "> <" + entityOccupiedAreaURI + "> ." //object hasOccupiedArea occupiedAreaXYZ
//                        + "<" + entityOccupiedAreaURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "OccupiedArea" + "> ."
//                        + "<" + entityOccupiedAreaURI + "> <" + Vocabulary.NAMESPACE_TBOX + "xPosition" + "> \"" + (new Integer(entity.getLeft().toString())).toString() + "\"^^xsd:integer" + " ." //occupiedAreaXYZ sot:width 200
//                        + "<" + entityOccupiedAreaURI + "> <" + Vocabulary.NAMESPACE_TBOX + "yPosition" + "> \"" + (new Integer(entity.getTop().toString())).toString() + "\"^^xsd:integer" + " ." //occupiedAreaXYZ sot:height 10
//                        + "<" + entityOccupiedAreaURI + "> <" + Vocabulary.NAMESPACE_TBOX + "width" + "> \"" + (new Integer(entity.getWidth().toString())).toString() + "\"^^xsd:integer" + " ." //occupiedAreaXYZ sot:width 123
//                        + "<" + entityOccupiedAreaURI + "> <" + Vocabulary.NAMESPACE_TBOX + "height" + "> \"" + (new Integer(entity.getHeight().toString())).toString() + "\"^^xsd:integer" + " ." //occupiedAreaXYZ sot:height 456
//                ;
//
//                //hasXGridSpace
//                String entityXGridSpaceURI = Vocabulary.NAMESPACE_TBOX + "xGridSpace_" + UUID.randomUUID().toString();
//                strInsert = strInsert
//                        + "<" + objectURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasXGridSpace" + "> <" + entityXGridSpaceURI + "> ." //person hasXGridSpace xGridSpaceXY
//                        + "<" + entityXGridSpaceURI + "> <" + RDF.TYPE + "> <" + Vocabulary.NAMESPACE_TBOX + "XGridSpace" + "> ."
//                        + "<" + entityXGridSpaceURI + "> <" + Vocabulary.NAMESPACE_TBOX + "hasOffset" + "> \"" + entity.getOffset() + "\"^^xsd:integer" + " ."
//                        + "<" + entityXGridSpaceURI + "> <" + Vocabulary.NAMESPACE_TBOX + "positionedInXGrid" + "> \"" + entity.getPositionInXGridSpace() + "\"^^xsd:string" + " ." //xGridSpaceXY sot:positionedInXGrid "middle"
//                ;
//
//            }
//        }
//
//        strInsert = Vocabulary.PREFIXES_ALL +
//                "INSERT DATA {"
//                + strInsert
//                + " }";
//
////		System.out.println("QUERY CREATED: \n" + strInsert);
//
//        try (RepositoryConnection connection = this.getUpdateRepo().getConnection()) {
//
//            connection.begin();
//            Update updateOperation = connection.prepareUpdate(QueryLanguage.SPARQL, strInsert);
//            updateOperation.execute();
//
//            try {
//                connection.commit();
//            } catch (Exception e) {
//                if (connection.isActive())
//                    connection.rollback();
//            }
//        }
//    }

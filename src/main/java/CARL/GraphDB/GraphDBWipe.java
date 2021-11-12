package CARL.GraphDB;

import CARL.Vocabulary;
import CARL.entities.User;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static AuxiliaryPackage.AuxiliaryPackageUtils.addUsers;

/**
 * This class represents a Graph DB Wipe process. The purpose of this process is to delete all
 * the semantic problems from the database.
 *
 * @author Kassiano Vasileios
 * @version Q3_2021
 * @since Q3_2021
 */

public class GraphDBWipe {

    private RepositoryConnection repositoryConnection = null;

    private List<User> users = null;

    public void setRepositoryConnection(RepositoryConnection repositoryConnection) {
        this.repositoryConnection = repositoryConnection;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public GraphDBWipe(RepositoryConnection repositoryConnection, List<User> users) throws IOException {

        // Set the private class variabes, repository connection and users.
        setRepositoryConnection(repositoryConnection);
        setUsers(users);
    }

    public void wipeSpecificUserProblems(RepositoryConnection repositoryConnection, User user) throws IOException {

        wipeSpecificUserHeartRateData(repositoryConnection, user);
        wipeSpecificUserMovementData(repositoryConnection, user);
        wipeSpecificUserSleepData(repositoryConnection, user);

    }

    public void wipeAllUserProblems(RepositoryConnection repositoryConnection) throws IOException {

        for (User user : users)
            wipeSpecificUserProblems(repositoryConnection, user);
    }

    public void wipeSpecificUserSleepData(RepositoryConnection repositoryConnection, User user){

        String userId = String.valueOf(user.getId());

        ArrayList<String> sleepProblems = new ArrayList<String>();
        String queryString3 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString3 += "SELECT ?s \n";
        queryString3 += "WHERE { \n";
        queryString3 += "?p a CARL:Person . \n";
        queryString3 += "?p CARL:personId ?id . \n ";
        queryString3 += "?s a CARL:LackOfSleep .  \n";
        queryString3 += "?p CARL:hasSleepProblem ?s. \n";
        queryString3 += "FILTER(?id =" + userId + " ) \n";
        queryString3 += "}";
        TupleQuery query3 = repositoryConnection.prepareTupleQuery(queryString3);
        TupleQueryResult result3 = query3.evaluate();
        while (result3.hasNext()) {
            BindingSet solution = result3.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            sleepProblems.add(str1.substring(index + 1));
            //System.out.println("?s = " + solution.getValue("s"));
        }

        for (String sleepProblem : sleepProblems) {
            String queryDeleteSleepProblem= "";
            queryDeleteSleepProblem = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryDeleteSleepProblem += "DELETE{ \n";
            queryDeleteSleepProblem += Vocabulary.PREFIX + sleepProblem + " ?p ?o . \n";
            queryDeleteSleepProblem += "?o1 ?p1 " + Vocabulary.PREFIX + sleepProblem + " . \n";
            queryDeleteSleepProblem += "}\n";
            queryDeleteSleepProblem += "WHERE { \n";
            queryDeleteSleepProblem += Vocabulary.PREFIX + sleepProblem + " ?p ?o \n";
            queryDeleteSleepProblem += "OPTIONAL {\n" + " ?o1 ?p1 " + Vocabulary.PREFIX + sleepProblem + "\n" + "}";
            queryDeleteSleepProblem += "}";

            System.out.println(queryDeleteSleepProblem);

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryDeleteSleepProblem);
            operation.execute();
        }
    }

    public void wipeSpecificUserHeartRateData(RepositoryConnection repositoryConnection, User user){
        String userId = String.valueOf(user.getId());

        ArrayList<String> hrProblems = new ArrayList<String>();
        String queryString = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString += "SELECT ?s \n";
        queryString += "WHERE { \n";
        queryString += "?p a CARL:Person . \n";
        queryString += "?p CARL:personId ?id . \n ";
        queryString += "?s a CARL:LowHeartRateProblem .  \n";
        queryString += "?p CARL:hasHeartRateProblem ?s . \n";
        queryString += "FILTER(?id =" + userId + " ) \n";
        queryString += "}";
        TupleQuery query = repositoryConnection.prepareTupleQuery(queryString);
        TupleQueryResult result = query.evaluate();
        while (result.hasNext()) {
            BindingSet solution = result.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            hrProblems.add(str1.substring(index + 1));
            //System.out.println("?s = " + solution.getValue("s"));
        }

        for (String hrProblem : hrProblems) {
            String queryDeleteHrProblem= "";
            queryDeleteHrProblem = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryDeleteHrProblem += "DELETE{ \n";
            queryDeleteHrProblem += Vocabulary.PREFIX + hrProblem + " ?p ?o . \n";
            queryDeleteHrProblem += "?o1 ?p1 " + Vocabulary.PREFIX + hrProblem + " . \n";
            queryDeleteHrProblem += "}\n";
            queryDeleteHrProblem += "WHERE { \n";
            queryDeleteHrProblem += Vocabulary.PREFIX + hrProblem + " ?p ?o \n";
            queryDeleteHrProblem += "OPTIONAL {\n" + " ?o1 ?p1 " + Vocabulary.PREFIX + hrProblem + "\n" + "}";
            queryDeleteHrProblem += "}";

            System.out.println(queryDeleteHrProblem);

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryDeleteHrProblem);
            operation.execute();
        }
    }

    public void wipeSpecificUserMovementData(RepositoryConnection repositoryConnection, User user){

        String userId = String.valueOf(user.getId());


        ArrayList<String> movProblems = new ArrayList<String>();
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?s \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?p a CARL:Person . \n";
        queryString2 += "?p CARL:personId ?id . \n ";
        queryString2 += "?s a CARL:LackOfMovement .  \n";
        queryString2 += "?p CARL:hasMovementProblem ?s. \n";
        queryString2 += "FILTER(?id =" + userId + " ) \n";
        queryString2 += "}";
        TupleQuery query2 = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result2 = query2.evaluate();
        while (result2.hasNext()) {
            BindingSet solution = result2.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            movProblems.add(str1.substring(index + 1));
            //System.out.println("?s = " + solution.getValue("s"));
        }

        for (String movProblem : movProblems) {
            String queryDeleteMovProblem= "";
            queryDeleteMovProblem = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryDeleteMovProblem += "DELETE{ \n";
            queryDeleteMovProblem += Vocabulary.PREFIX + movProblem + " ?p ?o . \n";
            queryDeleteMovProblem += "?o1 ?p1 " + Vocabulary.PREFIX + movProblem + " . \n";
            queryDeleteMovProblem += "}\n";
            queryDeleteMovProblem += "WHERE { \n";
            queryDeleteMovProblem += Vocabulary.PREFIX + movProblem + " ?p ?o \n";
            queryDeleteMovProblem += "OPTIONAL {\n" + " ?o1 ?p1 " + Vocabulary.PREFIX + movProblem + "\n" + "}";
            queryDeleteMovProblem += "}";

            System.out.println(queryDeleteMovProblem);

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryDeleteMovProblem);
            operation.execute();
        }

    }
    public void wipeAllUserSleepData(RepositoryConnection repositoryConnection){

        for (User user : users) {
            wipeSpecificUserSleepData(repositoryConnection, user);

        }
    }
    public void wipeAllUserHeartRateData(RepositoryConnection repositoryConnection){

        for (User user : users) {
            wipeSpecificUserHeartRateData(repositoryConnection, user);

        }
    }
    public void wipeAllUserMovementpData(RepositoryConnection repositoryConnection){

        for (User user : users) {
            wipeSpecificUserMovementData(repositoryConnection, user);

        }
    }

    public void wipeSpecificUserFibaroData(RepositoryConnection repositoryConnection, User user){

        String userId = String.valueOf(user.getId());


        ArrayList<String> fibaroData = new ArrayList<String>();
        String queryString2 = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
        queryString2 += "SELECT ?s \n";
        queryString2 += "WHERE { \n";
        queryString2 += "?p a CARL:Person . \n";
        queryString2 += "?p CARL:personId ?id . \n ";
        queryString2 += "?s a CARL:FibaroData .  \n";
        queryString2 += "?p CARL:hasDetecteData ?s. \n";
        queryString2 += "FILTER(?id =" + userId + " ) \n";
        queryString2 += "}";
        TupleQuery query2 = repositoryConnection.prepareTupleQuery(queryString2);
        TupleQueryResult result2 = query2.evaluate();
        while (result2.hasNext()) {
            BindingSet solution = result2.next();
            // ... and print out the value of the variable binding for ?s and ?n
            String str1 = solution.getValue("s").toString();
            int index = str1.indexOf("#");
            fibaroData.add(str1.substring(index + 1));
            //System.out.println("?s = " + solution.getValue("s"));
        }

        for (String movProblem : fibaroData) {
            String queryDeleteFibaroData= "";
            queryDeleteFibaroData = "PREFIX CARL: <http://www.semanticweb.org/ITI/ontologies/2021/2/CARL#> \n";
            queryDeleteFibaroData += "DELETE{ \n";
            queryDeleteFibaroData += Vocabulary.PREFIX + movProblem + " ?p ?o . \n";
            queryDeleteFibaroData += "?o1 ?p1 " + Vocabulary.PREFIX + movProblem + " . \n";
            queryDeleteFibaroData += "}\n";
            queryDeleteFibaroData += "WHERE { \n";
            queryDeleteFibaroData += Vocabulary.PREFIX + movProblem + " ?p ?o \n";
            queryDeleteFibaroData += "OPTIONAL {\n" + " ?o1 ?p1 " + Vocabulary.PREFIX + movProblem + "\n" + "}";
            queryDeleteFibaroData += "}";

            System.out.println(queryDeleteFibaroData);

            Update operation = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL, queryDeleteFibaroData);
            operation.execute();
        }

    }

    public void wipeAllFibaroData(RepositoryConnection repositoryConnection){

        for (User user : users) {
            wipeSpecificUserFibaroData(repositoryConnection, user);

        }
    }


    public static void main(String[] args) throws IOException {
        // The following connection credentials should be inserted in the grapdh db connection constructor in order to connect to the remote graph db.
        String username      = "vasilis";
        String password      = "k5ksxp10kk!";
        String graphDBServer = "http://160.40.49.192:89/";
        String repositoryId  = "mklab-ypostirizo_db";

        // Retrieve the users from the proper file.
        List<User> users = addUsers();
        List<User> finalUsers = users;
        users = new ArrayList<>(){{add(finalUsers.get(38)); add(finalUsers.get(39));}};



        // Create a connection to the remote graph db connection. Also upload the ontology while doing that.
        GraphDBConnection connector = new GraphDBConnection(graphDBServer, repositoryId, username, password);


        // Create a repository connection object from the previously established remote graph db connector.
        RepositoryConnection connection = connector.getRepositoryConnection();


        GraphDBWipe wipe = new GraphDBWipe(connection, users);

        users.forEach(i-> {
            try {
                wipe.wipeSpecificUserProblems(connection, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



}

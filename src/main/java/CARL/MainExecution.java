package CARL;

import AuxiliaryPackage.AuxiliaryPackageUtils;
import CARL.Connectors.FibaroAPIConnector;
import CARL.Connectors.FibaroWattAPIConnector;
import CARL.Connectors.FitbitAPIConnector;
import CARL.GraphDB.GraphDBConnection;
import CARL.GraphDB.GraphDBPopulation;
import CARL.GraphDB.GraphDBRulesExecution;
import CARL.GraphDB.GraphDBWipe;
import CARL.entities.User;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static AuxiliaryPackage.AuxiliaryPackageUtils.addUsers;
import static java.lang.System.exit;

public class MainExecution {

    private static final ch.qos.logback.classic.Logger logger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);


    public static void main(String[] args) throws IOException, ParseException {

        // The following connection credentials should be inserted in the grapdh db connection constructor in order to connect to the remote graph db.
        String username      = "vasilis";
        String password      = "k5ksxp10kk!";
        String graphDBServer = "http://160.40.49.192:89/";
        String repositoryId  = "mklab-ypostirizo_db";

        //logger.error("This is an ERROR example in Backlog framework");
        logger.setLevel(Level.ERROR);
        // Retrieve the users from the proper file.
        List<User> users = addUsers();
        List<User> finalUsers = users;
        users = new ArrayList<>(){{add(finalUsers.get(50));
            add(finalUsers.get(4));
        }};
        finalUsers.get(50).status();
        // If we want to execute the fibaro API connection and retrieve data from there, uncomment the following line.
        //FibaroAPIConnector fibaroApiConnector = new FibaroAPIConnector(users);

        // If we want to execute the fibaro API connection and retrieve Watt data from there, uncomment the following line.
        FibaroWattAPIConnector fibaroWattApiConnector = new FibaroWattAPIConnector(users);

        // If we want to execute the Fibaro thresholds API connection and retrieve data from there, uncomment the following line
        //FibaroThresholdsConnector  fibaroThresholdsConnector = new FibaroThresholdsConnector(users);

        // If we want to execute the fitbit API connection and retrieve data from there, uncomment the following line
        //FitbitAPIConnector apiConnector = new FitbitAPIConnector(users);

        // exit(1);
        //System.out.println("edw==");
//        for (User user : users) {
//            user.status();
//        }


        // Create a connection to the remote graph db connection. Also upload the ontology while doing that.
        GraphDBConnection connector = new GraphDBConnection(graphDBServer, repositoryId, username, password);

        // In case we do not wish to upload data to graph db, comment out the next line.
        connector.uploadDataToRemoteRepository();

        // Create a repository connection object from the previously established remote graph db connector.
        RepositoryConnection connection = connector.getRepositoryConnection();


        // Populate the graph DB with the fitbit and Fibaro data of all users.
        GraphDBPopulation graphDBPopulation = new GraphDBPopulation(connection, users);

        // Execute the rules
        GraphDBRulesExecution rulesExecution = new GraphDBRulesExecution(connection, users);

        //GraphDBWipe gdbw = new GraphDBWipe(connection, users);
        //gdbw.wipeAllFibaroData(connection);

    }



}

package CARL.GraphDB;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static AuxiliaryPackage.AuxiliaryPackageConstants.*;
import static java.util.Objects.isNull;

/**
 * This class represents a Graph DB Connection.
 *
 * @author  Vasileiadis Angelos
 * @version Q3_2021
 * @since   Q3_2021
 */
public class GraphDBConnection {

    /**
     * This is the repository connection object that is produced during the connection to the graph db.
     */
    RepositoryConnection repositoryConnection = null;

    /**
     * This is the username of the user's credentials that we want to connect with, in the graph db.
     */
    String username = BLANK_STRING;

    /**
     * This is the password of the user's credentials that we want to connect with, in the graph db.
     */
    String password = BLANK_STRING;

    /**
     * This is the void constructor of the class.
     */
    GraphDBConnection() {}

    /**
     * This is the constructor of the class.
     *
     * @param  graphDBServer The graph DB Server to be used, as a String value.
     * @param  repositoryId  The repository id of the aforementioned graph db server, as a String value.
     * @param  username      The username of the user in the DB, as a String value.
     * @param  password      The password of the user in the DB, as a String value.
     * @throws IOException   In case an i/o exception occurs.
     */
    public GraphDBConnection(String graphDBServer, String repositoryId, String username, String password) throws IOException {

        // If the input graphDb server is null or empty, use the default value of the Graph DB server
        if (isNull(graphDBServer) || graphDBServer.isEmpty()) {
            setGraphdbServerConstant(GRAPHDB_SERVER);
            setRepositoryIdConstant(REPOSITORY_ID);
        }
        else {
            setGraphdbServerConstant(graphDBServer);
            setRepositoryIdConstant(repositoryId);
        }

        // Set the username and the password class variables.
        setUsername(username);
        setPassword(password);

        // Initialize the connector.
        initializeGraphDBConnector();
    }

    /**
     * This is the getter method of the repository connection class variable.
     *
     * @return The repository connection class variable, as a Repository Connection object.
     */
    public RepositoryConnection getRepositoryConnection() { return repositoryConnection; }

    /**
     * This is the getter method of the username class variable.
     *
     * @return The username class variable, as a String value.
     */
    String getUsername() { return username; }

    /**
     * This is the getter method of the password class variable.
     *
     * @return The password class variable, as a String value.
     */
    String getPassword() { return password; }

    /**
     * This is the setter of the repository connection class variable.
     *
     * @param repositoryConnection The repository connection class variable, as a Repository Connection object.
     */
    void setRepositoryConnection(RepositoryConnection repositoryConnection) {

        // Sanity checking
        if (isNull(repositoryConnection))
            return;

        // Set the class variable
        this.repositoryConnection = repositoryConnection;
    }

    /**
     * This is the setter method of the username class variable.
     *
     * @param username The username class variable, as a String value.
     */
    void setUsername(String username) {

        // Sanity checking
        if (isNull(username) || username.isEmpty())
            return;

        // Set the class variable
        this.username = username;
    }

    /**
     * This is the setter method of the password class variable.
     *
     * @param password The password class variable, as a String value.
     */
    void setPassword(String password) {

        // Sanity checking
        if (isNull(password) || password.isEmpty())
            return;

        // Set the class variable
        this.password = password;
    }

    /**
     * This method initializes all the appropriate actions that need to take place in order to successfully establish a
     * graph db connection.
     *
     * @throws IOException In case an i/o exception occurs.
     */
    void initializeGraphDBConnector() throws IOException {

        // Instantiate a remote repository manager and initialize it
        RemoteRepositoryManager repositoryManager;

        // In case the username or the password is blank
        if (username.isBlank() || password.isBlank())

            // Get the instance of the graph db server without username or password
            repositoryManager = RemoteRepositoryManager.getInstance(GRAPHDB_SERVER);
        else

            // Get the instance of the corresponding user (username & password) of the dedicated graph db server.
            repositoryManager = RemoteRepositoryManager.getInstance(GRAPHDB_SERVER, username, password);

        //  repositoryManager.initialize();
        repositoryManager.getAllRepositories();

        // Instantiate a repository graph model
        TreeModel graph = new TreeModel();

        Path path = Paths.get(".").toAbsolutePath().normalize();
        String strRepositoryConfig = path.toFile().getAbsolutePath() + "\\src\\main\\resources\\CARL\\ypostirizo_db-config.ttl";
        System.out.println(strRepositoryConfig);
//        Path path = Paths.get(".").toAbsolutePath().normalize();
//        String newpath = path + "\\src\\main\\resources\\ipostirizo\\"+"ypostirizo_db_config.ttl";
//        System.out.println("NEW PATH IS : ");
//        System.out.println(newpath);

        // Read repository configuration file
//        InputStream config = GraphDBConnection.class.getResourceAsStream(strRepositoryConfig);
        InputStream config = new FileInputStream(strRepositoryConfig);
        RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
        rdfParser.setRDFHandler(new StatementCollector(graph));
        rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
        config.close();

        // Retrieve the repository node as a resource
        Resource repositoryNode = Models.subject(graph.filter(null, RDF.TYPE, RepositoryConfigSchema.REPOSITORY)).orElseThrow(() -> new RuntimeException
                ("Oops, no <http://www.openrdf.org/config/repository#> subject found!"));

        // Create a repository configuration object and add it to the repositoryManager
        RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);

        //Change repository ID with respect to the value of the repositoryID variable
        repositoryConfig.setID("mklab-yposthrizo_db");
        repositoryManager.addRepositoryConfig(repositoryConfig);

        // Get the repository from repository manager, note the repository id set in configuration .ttl file
//		Repository repository = repositoryManager.getRepository("mklab-suitceyes-kb-" + repositoryID);
        Repository repository = repositoryManager.getRepository(repositoryConfig.getID());

        // Open a connection to this repository
        RepositoryConnection repositoryConnection = repository.getConnection();

        setRepositoryConnection(repositoryConnection);

        // Shutdown connection, repository and manager
//        repositoryConnection.close();
//        repository.shutDown();
//        repositoryManager.shutDown();

//        uploadDataToRemoteRepository();
    }

    public void uploadDataToRemoteRepository() throws IOException{
        System.out.println("Upload to GraphDB");
        // curl command /
		/*
		String curl_command = "curl -X POST -H \"Content-Type:application/x-turtle\" "
		+ "-T \"C:\\Users\\mriga\\TBCFreeWorkspace\\SUITCEYES\\suitceyes_tbox.ttl\" "
		+ "http://160.40.49.112:7200/repositories/mklab-suitceyes-kb-DEFAULT/statements\"";
		*/


        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("", "");
        credentialsProvider.setCredentials(AuthScope.ANY, creds);
//	    CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpClient client =
                HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();


        HttpPost httpPost = new HttpPost("http://160.40.49.192:89/repositories/mklab-yposthrizo_db/statements");

        String user = "vasilis";
        String pwd = "k5ksxp10kk!";
        String encoding = Base64.getEncoder().encodeToString((user + ":" + pwd).getBytes());
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        System.out.println("Executing Request " + httpPost.getRequestLine());

        Path path = Paths.get(".").toAbsolutePath().normalize();
        // String strOntology = path.toFile().getAbsolutePath() + "\\src\\main\\resources\\CARL\\new_ontology_ipostirizo.ttl";
        // String strOntology = path.toFile().getAbsolutePath() + "\\src\\main\\resources\\CARL\\experimental_db.ttl";
        String strOntology = path.toFile().getAbsolutePath() + "\\src\\main\\resources\\CARL\\ontology_with_hourly.owl";

        String data = new String(Files.readAllBytes(Paths.get(strOntology)));
        //System.out.println("DATA IS : " + data);
        HttpEntity entity = new StringEntity(data);
        httpPost.setEntity(entity);

        httpPost.setHeader("Accept", "*/*");
        httpPost.setHeader("Content-type", "application/x-turtle");

        System.out.println("Connecting to GraphDB");
        CloseableHttpResponse response = client.execute(httpPost);
        StatusLine sl = response.getStatusLine();
        System.out.println("Request ended with code " + sl.getStatusCode());
        //client.close();

    }

    void close() {

        // Shutdown connection, repository and manager
        repositoryConnection.close();
//        repository.shutDown();
//        repositoryManager.shutDown();
    }
}

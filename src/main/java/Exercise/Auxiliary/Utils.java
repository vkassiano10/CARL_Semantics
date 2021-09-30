package Exercise.Auxiliary;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

/**
 * This class contains the auxiliary methods that are used through out the Semantics Exercise.
 *
 * @author  Vasileiadis Angelos
 * @since   Q2_2021
 * @version Q2_2021
 */
public class Utils {

    /**
     * This method is used to create a connection to the repository of interest in the graph db server of interest.
     *
     * @param graphDbSever The address of the graphDbServer, as a String value.
     * @param repositoryId The repository id of interest, as a String value.
     *
     * @return The repository, as a Repository object.
     */
    public static Repository retrieveRepository(String graphDbSever, String repositoryId) {

        // Create a new HTTP Repository object.
        Repository repository = new HTTPRepository(graphDbSever, repositoryId);
        repository.init();

        // Get the connection and return it.
        return repository;
    }
}

package Exercise.ForthTask;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryImplConfig;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.sail.config.SailRepositoryConfig;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.sail.config.SailImplConfig;
import org.eclipse.rdf4j.sail.inferencer.fc.config.DedupingInferencerConfig;
import org.eclipse.rdf4j.sail.inferencer.fc.config.SchemaCachingRDFSInferencerConfig;
import org.eclipse.rdf4j.sail.memory.config.MemoryStoreConfig;
import org.eclipse.rdf4j.sail.spin.config.SpinSailConfig;
import org.eclipse.rdf4j.sparqlbuilder.core.Prefix;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.ModifyQuery;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.TriplePattern;

import static AuxiliaryPackage.AuxiliaryPackageConstants.GRAPHDB_SERVER;
import static AuxiliaryPackage.AuxiliaryPackageConstants.REPOSITORY_ID;
import static Exercise.Auxiliary.Constants.*;
import static org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf.iri;

/**
 * This class represents the execution of the forth task of the Semantics Exercise.
 *
 * @author  Vasileiadis Angelos
 * @since   Q2_2021
 * @version Q2_2021
 */
public class ForthTaskExecution {

    public static void main(String[] args) {

        // Create a repository manager
        RepositoryManager manager = new RemoteRepositoryManager(GRAPHDB_SERVER);

        // Create the configuration for the sail stack
        SailImplConfig spinSailConfig = new SpinSailConfig(
                new SchemaCachingRDFSInferencerConfig(
                        new DedupingInferencerConfig(new MemoryStoreConfig()))
        );

        // Sail Repository Config
        RepositoryImplConfig repositoryTypeSpec = new SailRepositoryConfig(spinSailConfig);

        // Create the configuration for the actual repository
        RepositoryConfig repConfig = new RepositoryConfig(REPOSITORY_ID, repositoryTypeSpec);
        manager.addRepositoryConfig(repConfig);

        // Get the Repository from the manager
        Repository repository = manager.getRepository(REPOSITORY_ID);

        // Create the construct query string
        String queryString = "PREFIX se: <https://www.semanticsExercise.com#> " +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +

                "INSERT{" +
                "    ?act2 se:shouldStartAt ?stopTime1 ." +
                "}" +
                "WHERE {" +
                "    ?act1 se:hasActivityStartTime ?startTime1 ." +
                "    ?act1 se:hasActivityStopTime ?stopTime1 ." +
                "    ?act2 se:hasActivityStartTime ?startTime2 ." +
                "    ?act2 se:hasActivityStopTime ?stopTime2 ." +
                "    FILTER (?startTime1 < ?startTime2) ." +
                "    FILTER (?stopTime1 > ?startTime2) ." +
                "    }";

        // Retrieve the model that holds the statements that correspond to the construct query.
        Model retrievedModel = Repositories.graphQuery(repository, queryString, QueryResults::asModel);

        // Declaring auxiliary variables, in order to create the appropriate query string for each statement of the retrieved model.
        ModifyQuery   modifyQuery                                                  = Queries.MODIFY();
        Prefix        se                                                           = SparqlBuilder.prefix("se", iri(NAMESPACE_STRING));
        Variable      oldStartTime                                                 = SparqlBuilder.var("oldStartTime");
        TriplePattern deleteTriplePattern, insertTriplePattern, whereTriplePattern;
        String        updateQuery;

        // For each statement in the retrieved model, create an update query
        for (Statement st : retrievedModel) {

            // Create the appropriate graph patterns to be used in the modify query construction.
            deleteTriplePattern = GraphPatterns.tp(st.getSubject(), se.iri("hasActivityStartTime"), oldStartTime);
            insertTriplePattern = GraphPatterns.tp(st.getSubject(), se.iri("hasActivityStartTime"), st.getObject());
            whereTriplePattern  = GraphPatterns.tp(st.getSubject(), se.iri("hasActivityStartTime"), oldStartTime);

            // Create the update query.
            updateQuery = modifyQuery.prefix(se)
                    .delete(deleteTriplePattern)
                    .insert(insertTriplePattern)
                    .where(whereTriplePattern)
                    .getQueryString();

            // Prepare an update operation.
            Update updateOperation = repository.getConnection().prepareUpdate(updateQuery);

            // Execute the update operation.
            updateOperation.execute();
        }

        // Close the connection
        repository.getConnection().close();
    }
}

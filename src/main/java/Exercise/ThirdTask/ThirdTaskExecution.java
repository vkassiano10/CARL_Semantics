package Exercise.ThirdTask;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.sparqlbuilder.core.Prefix;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.core.query.SelectQuery;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.TriplePattern;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static AuxiliaryPackage.AuxiliaryPackageConstants.GRAPHDB_SERVER;
import static AuxiliaryPackage.AuxiliaryPackageConstants.REPOSITORY_ID;
import static Exercise.Auxiliary.Constants.*;
import static Exercise.Auxiliary.Utils.retrieveRepository;
import static org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf.iri;

/**
 * This class represents the execution of the third task of the Semantics Exercise.
 *
 * @author  Vasileiadis Angelos
 * @since   Q2_2021
 * @version Q2_2021
 */
public class ThirdTaskExecution {

    public static void main(String[] args) {

        // Initialize a repository connection, in the appropriate graph db server and repository id.
        Repository repository = retrieveRepository(GRAPHDB_SERVER, REPOSITORY_ID);

        //<Statement> rst = repository.getConnection().getStatements(null, null, null, true);
        SelectQuery selectQuery = Queries.SELECT();

        // =============================== FIRST QUERY ==================================//

        // Declaring auxiliary variables in order to build the query String properly.
        Prefix se = SparqlBuilder.prefix("se", iri(NAMESPACE_STRING));
        Variable person    = SparqlBuilder.var("person");
        Variable startTime = SparqlBuilder.var("startTime");
        Variable stopTime  = SparqlBuilder.var("stopTime");
        Variable content   = SparqlBuilder.var("content");

        // Create the triple patterns necessary for the sparql builder.
        TriplePattern hasActivityStartTime = GraphPatterns.tp(person, se.iri("hasActivityStartTime"), startTime);
        TriplePattern hasActivityStopTime  = GraphPatterns.tp(person, se.iri("hasActivityStopTime"),  stopTime);
        TriplePattern hasActivityContent   = GraphPatterns.tp(person, se.iri("hasActivityContent"),   content);

        // Create the first query String.
        String queryString = selectQuery.prefix(se)
                .select(startTime).where(hasActivityStartTime)
                .select(stopTime).where(hasActivityStopTime)
                .select(content).where(hasActivityContent)
                .getQueryString();

        System.out.println("Query String is : ");
        System.out.println(queryString);
        System.out.println();

        // Retrieve the resulting activities from the database.
        List<BindingSet> resultsActivities = Repositories.tupleQuery(repository, queryString, r -> QueryResults.asList(r));

        System.out.println("-----ACTIVITIES THAT EXIST IN DATABASE----");
        resultsActivities.forEach(System.out::println);

        // =============================== SECOND QUERY ================================== //
        Variable observationType = SparqlBuilder.var("observationType");

        TriplePattern hasObservationContent = GraphPatterns.tp(person, se.iri("hasObservationContent"), observationType);
        queryString = Queries.SELECT().prefix(se).select(observationType).where(hasObservationContent).getQueryString();

        List<BindingSet> resultsObservationTypes = Repositories.tupleQuery(repository, queryString, r -> QueryResults.asList(r));

        // Discard the observation types that are double or triple in the database
        Set<BindingSet> resultsObservationTypesAsSet = new LinkedHashSet<>(resultsObservationTypes);

        System.out.println("Query String is : ");
        System.out.println(queryString);
        System.out.println();

        System.out.println("-----OBSERVATION TYPES THAT EXIST IN DATABASE----");
        resultsObservationTypesAsSet.forEach(System.out::println);

        // =============================== THIRD QUERY ===================================== //

        queryString = "PREFIX se: <https://www.semanticsExercise.com#> \n" +
                "\n" +
                "SELECT ?content\n" +
                "WHERE { \n" +
                "    ?obs se:hasObservationStartTime ?startTime  . FILTER (?startTime > \"2014-05-05T18:34:54.000\"^^xsd:dateTime) .\n" +
                "    ?obs se:hasObservationStopTime ?stopTime  .  FILTER (?stopTime < \"2014-05-05T18:55:40.000\"^^xsd:dateTime) .\n" +
                "\t?obs se:hasObservationContent ?content . }";

        List<BindingSet> resultsObservationTypesFiltered = Repositories.tupleQuery(repository, queryString, r -> QueryResults.asList(r));
        Set<BindingSet> resultsObservationTypesFilteredAsSet = new LinkedHashSet<>(resultsObservationTypesFiltered);

        System.out.println("Query String is : ");
        System.out.println(queryString);
        System.out.println();

        System.out.println("-----OBSERVATION TYPES FILTERED THAT EXIST IN DATABASE----");
        resultsObservationTypesFilteredAsSet.forEach(System.out::println);

        repository.getConnection().close();
    }
}

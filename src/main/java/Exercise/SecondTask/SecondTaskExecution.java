package Exercise.SecondTask;

import AuxiliaryPackage.AuxiliaryPackageConstants;
import Exercise.SecondTask.Entities.Activity;
import Exercise.SecondTask.Entities.Observation;
import Exercise.SecondTask.Entities.Person;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

import static AuxiliaryPackage.AuxiliaryPackageConstants.GRAPHDB_SERVER;
import static AuxiliaryPackage.AuxiliaryPackageConstants.REPOSITORY_ID;
import static Exercise.Auxiliary.Constants.*;
import static Exercise.Auxiliary.Utils.retrieveRepository;
import static org.eclipse.rdf4j.model.util.Values.getValueFactory;

/**
 * This class represents the execution of the second task of the Semantics Exercise.
 *
 * @author  Vasileiadis Angelos
 * @since   Q2_2021
 * @version Q2_2021
 */
public class SecondTaskExecution {

    public static void main(String[] args) throws IOException, ParseException, IllegalAccessException {

        // Set the time zone to UTC.
        System.setProperty("user.timezone", "UTC");

        // Create json parser and the corresponding object.
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(JSON_FILE_NAME));

        // Create a new model, representing a person. All the necessary parsing operation take place automatically through
        // the constructor of the person object.
        Person person = new Person(object);

        // Declare auxiliary variables.
        String namespace = NAMESPACE_STRING;
        String prefix    = "se";

        // Create a model builder and set the namespace accordingly.
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace(prefix, namespace);

        // Create the person's IRI
        IRI personIRI = getValueFactory().createIRI(namespace, "person");
        System.out.println(personIRI.toString());

        // Create the IRI of the activity predicate.
        IRI hasActivityIRI = getValueFactory().createIRI(namespace, "hasActivity");

        // For each activity in the person's activities.
        for (Activity activity : person.getActivities()) {

            // Create the specific activity's predicate IRI
            IRI activityIRI = getValueFactory().createIRI(namespace, "activity" + activity.getId());

            // Set a triple through the builder
            builder.subject(personIRI).add(hasActivityIRI, activityIRI);

            // Create the IRI of the predicate of the start time of this activity, and create a literal value of the
            // corresponding start time.
            IRI hasActivityStartTimeIRI = getValueFactory().createIRI(namespace, "hasActivityStartTime");
            Value startTime = getValueFactory().createLiteral(activity.getStarTime().toString(), XSD.DATETIME);

            // Create the IRI of the predicate of the stop time of this activity, and create a literal value of the
            // corresponding stop time.
            IRI hasActivityStopTimeIRI = getValueFactory().createIRI(namespace, "hasActivityStopTime");
            Value stopTime = getValueFactory().createLiteral(activity.getStopTime().toString(), XSD.DATETIME);

            // Create the IRI of the predicate of the content of this activity, and create a literal value of the
            // corresponding content.
            IRI hasActivityContentIRI = getValueFactory().createIRI(namespace, "hasActivityContent");
            Value content = getValueFactory().createLiteral(activity.getContent(), XSD.STRING);

            // Create triples in the builder utilizing the previous IRIs and values.
            builder.subject(activityIRI)
                    .add(hasActivityStartTimeIRI, startTime)
                    .add(hasActivityStopTimeIRI,  stopTime)
                    .add(hasActivityContentIRI,   content);

            // For each observation in the activity's observations
            for (Observation observation : activity.getObservations()) {

                // Create the IRIs of the observation under examination as well it's predicate of the observation.
                IRI observationIRI = getValueFactory().createIRI(namespace, "observation" + observation.getId());
                IRI hasObservationIRI = getValueFactory().createIRI(namespace, "hasObservation");

                // Create a triple in the builder.
                builder.subject(activityIRI).add(hasObservationIRI, observationIRI);

                // Create the IRI of the predicate of the start time of this observation, and create a literal value of the
                // corresponding start time.
                IRI hasObservationStartTimeIRI = getValueFactory().createIRI( namespace,"hasObservationStartTime");
                Value observationStartTime = getValueFactory().createLiteral(observation.getStarTime().toString(), XSD.DATETIME);

                // Create the IRI of the predicate of the stop time of this observation, and create a literal value of the
                // corresponding stop time.
                IRI hasObservationStopTimeIRI = getValueFactory().createIRI(namespace, "hasObservationStopTime");
                Value observationStopTime = getValueFactory().createLiteral(observation.getStopTime().toString(), XSD.DATETIME);

                // Create the IRI of the predicate of the content of this observation, and create a literal value of the
                // corresponding content.
                IRI hasObservationContentIRI = getValueFactory().createIRI(namespace, "hasObservationContent");
                Value observationContent = getValueFactory().createLiteral(observation.getContent(), XSD.STRING);

                // Create a triple in the builder.
                builder.subject(observationIRI)
                        .add(hasObservationStartTimeIRI, observationStartTime)
                        .add(hasObservationStopTimeIRI,  observationStopTime)
                        .add(hasObservationContentIRI,   observationContent);
            }
        }

        // Retrieve the model from the builder.
        Model model = builder.build();

        // Retrieve the repository of interest.
        Repository repository = retrieveRepository(GRAPHDB_SERVER, REPOSITORY_ID);

        // Consume all the statements of the model.
        Repositories.consume(repository, repCon -> model.filter(null, null, null).forEach(repCon::add));
    }
}

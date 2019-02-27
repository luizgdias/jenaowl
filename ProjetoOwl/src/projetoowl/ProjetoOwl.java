/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoowl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.management.Query;
import static org.apache.jena.enhanced.BuiltinPersonalities.model;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.util.FileManager;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RSIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import static org.apache.jena.sparql.engine.http.Service.base;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.util.iterator.ExtendedIterator;
import static org.apache.jena.vocabulary.VOID.properties;

/**
 *
 * @author luizgustavo
 */
public class ProjetoOwl {

    public static void main(String[] args) {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        model.read("file:/home/luizgustavo/NetBeansProjects/ProjetoOwl/src/projetoowl/ontologyRdfTest.owl", "RDF/XML");

        String subject = "?NamedIndividual ";
        String predicate = "rdf:type";
        String object = "<http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#Data_transformation>";

        //showOntologyComponents(model, "Data_transformation");
        queryingDataTransformations(model, subject, predicate, object);
        //queryingEquivalentTransformations(model);
    }

    private static void queryingEquivalentTransformations(OntModel model) {

        String queryString
                = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "
                + "PREFIX jena: <http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#>  "
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX xml:<http://www.w3.org/XML/1998/namespace>"
                + "select ?Data_transformation "
                + "where { ?Data_transformation jena:same <http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#dt_c> }    "
                + "\n ";

        org.apache.jena.query.Query query = QueryFactory.create(queryString);

        System.out.println("----------------------");

        System.out.println("----------------------");

        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        // Output query results   
        //showing off the query results about all Data_transformations
        ResultSetFormatter.out(System.out, results, query);

    }

    private static void queryingDataTransformations(OntModel model, String subject, String predicate, String object) {
        String queryString
                = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "
                + "PREFIX jena: <http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#>  "
                + "PREFIX owl: <https://www.w3.org/2002/07/owl#:>"
                + "select " + subject
                + "where { "
                + subject + predicate + object + " ; "
                + " } "
                + "ORDER BY DESC(" + subject + ")\n"
                + "LIMIT 10 \n ";

        org.apache.jena.query.Query query = QueryFactory.create(queryString);

        System.out.println("----------------------");
        System.out.println("Listing all data transformations");
        System.out.println("----------------------");
        System.out.println("Subject: " + subject);
        System.out.println("Predicate: " + predicate);
        System.out.println("Object: " + object);
        System.out.println("----------------------");

        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        // Output query results   
        //showing off the query results about all Data_transformations
        //ResultSetFormatter.out(System.out, results, query);
        ArrayList<String> data_transformations = new ArrayList<>();

        //adding results into list
        while (results.hasNext()) {
            //taking just the uri and add into java list
            //the resultset store: ( ?NamedIndividual = <http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#dt_merge> ) into list
            String temp = results.next().toString();
            String content = temp.substring(temp.indexOf("= ") + 2, temp.indexOf(" )"));
            data_transformations.add(content);
        }

        boolean blnFound = data_transformations.contains("<http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#dt_merge>");
        System.out.println("Does arrayList contain jena:dt_merge ? " + blnFound);

        //Printing the javalist to confirm the process
        System.out.println("Sparql results - java list:");
        for (int i = 0; i < data_transformations.size(); i++) {
            //System.out.println(data_transformations.get(i).substring(data_transformations.get(i).indexOf("= ")+1, data_transformations.get(i).indexOf(" )")));
            System.out.println(data_transformations.get(i));
        }

        for (int j = 0; j < data_transformations.size(); j++) {
            System.out.println("------" + j);
            String subject2 = " ?dt_exists ";
            String predicate2 = "jena:same";
            String object2 = " "+data_transformations.get(j) + " ";

            System.out.println("Subject2 i: " + j + " " + subject2);
            System.out.println("Object j: " + j + " " + object2);

            String queryString2
                    = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                    + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "
                    + "PREFIX jena: <http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#>  "
                    + "PREFIX owl: <https://www.w3.org/2002/07/owl#:>"
                    + "select " + subject2
                    + "where { "
                    + subject2 + predicate2 + object2 + " ; "
                    + " } "
                    + "ORDER BY DESC(" + subject2 + ")\n"
                    + "LIMIT 10 \n ";

            org.apache.jena.query.Query query2 = QueryFactory.create(queryString2);
            QueryExecution qe2 = QueryExecutionFactory.create(query2, model);
            ResultSet results2 = qe2.execSelect();
            ResultSetFormatter.out(System.out, results2, query2);
        }
    }

    private static void showOntologyComponents(OntModel model, String termo) {
        ExtendedIterator classes = model.listClasses();

        while (classes.hasNext()) {
            OntClass thisClass = (OntClass) classes.next();
            System.out.println("Found class: " + thisClass.toString());
            ExtendedIterator instances = thisClass.listInstances();

            while (instances.hasNext()) {
                Individual thisInstance = (Individual) instances.next();
                System.out.println("    Found instance: " + thisInstance.getLocalName()); //to list the link, change getLocalName for toString

                for (StmtIterator j = thisInstance.listProperties(); j.hasNext();) {
                    Statement s = j.next(); //statment = arcs. A statement has three parts: subject predicade object
                    System.out.print("      Found property 1: " + s.getPredicate().getLocalName() + " -> ");

                    if (s.getObject().isLiteral()) {
                        System.out.println("IF: " + s.getLiteral().getLexicalForm());
                    } else {
                        System.out.println("Else: " + s.getObject()); //
                    }
                }

                System.out.println("\n");
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoowl;

import java.util.ArrayList;
import java.util.Iterator;
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

        String subject       = "?Data_transformation ";
        String predicate    = "rdf:type";
        String object       = "<http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#Data_transformation>";
        //showOntologyComponents(model, "Data_transformation");
        queryingDataflow(model, subject, predicate, object);
    }

    private static void queryingDataflow(OntModel model, String subject, String predicate, String object) {
        String queryString
                = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "
                + "PREFIX jena: <http://www.semanticweb.org/luizgustavo/ontologies/2019/1/jena#>  "
                + "PREFIX owl: <https://www.w3.org/2002/07/owl#:>"
                
                + "select " + subject
                + "where { "
                + subject + predicate + object +" ;"
               
                + "} \n ";

        org.apache.jena.query.Query query = QueryFactory.create(queryString);
        
        System.out.println("----------------------");

        System.out.println("Subject: "+ subject);
        System.out.println("Predicate: "+ predicate);
        System.out.println("Object: "+ object);

        System.out.println("----------------------");

        System.out.println("Descendentes diretos e indiretos do modelo");

        System.out.println("-------------------");

        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        // Output query results   
        //showing off the query results about all Data_transformations
        ResultSetFormatter.out(System.out, results, query);
        
        ArrayList<String> data_transformations = new ArrayList<>();
        
        //adding results into list
        while (results.hasNext()) {
            //RDFNode temp = results.next().get(queryString);
           data_transformations.add(results.next().toString());
        }
       
        //Printing the javalist to confirm the process
        System.out.println("Sparql results stored at java list:");
        for (int i = 0; i < data_transformations.size(); i++) {
            System.out.println(data_transformations.get(i));  
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

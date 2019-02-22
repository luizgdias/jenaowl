/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoowl;

import java.util.Iterator;
import java.util.Properties;
import javax.management.Query;
import static org.apache.jena.enhanced.BuiltinPersonalities.model;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.util.FileManager;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.rdf.model.Property;
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
        OntModel model = ModelFactory.createOntologyModel();
        model.read("file:/home/luizgustavo/NetBeansProjects/ProjetoOwl/src/projetoowl/ontologyRdfTest.owl");

        showOntologyComponents(model, "Data_transformation");

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
                    Statement s = j.next();
                    System.out.print("    " + s.getPredicate().getLocalName() + " -> ");

                    if (s.getObject().isLiteral()) {
                        System.out.println(s.getLiteral().getLexicalForm());
                    } else {
                        System.out.println(s.getObject());
                    }
                }

                System.out.println("\n");
            }
        }
    }
}

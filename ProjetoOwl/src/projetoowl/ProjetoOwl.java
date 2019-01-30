/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoowl;

import javax.management.Query;
import static org.apache.jena.enhanced.BuiltinPersonalities.model;
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
import org.apache.jena.util.PrintUtil;

/**
 *
 * @author luizgustavo
 */
public class ProjetoOwl {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileManager.get().addLocatorClassLoader(ProjetoOwl.class.getClassLoader());
        Model data = FileManager.get().loadModel("file:/home/luizgustavo/ontology.owl");
        Model schema = FileManager.get().loadModel("https://www.w3.org/2000/01/rdf-schema");
        //model.write(System.out, "TURTLE");     
        
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
               
        String objeto = "", objeto1 = "workflow";
        
        
        for (int i = 1; i < 4; i++) {
            objeto = objeto1+""+i;
            System.out.println("\n** Data about "+objeto+" **");

            Resource entidade = infmodel.getResource("http://www.semanticweb.org/luizgustavo/ontologies/2019/0/untitled-ontology-2#"+objeto);
            System.out.println(objeto+" *:");
            showWorkflowsSteps(infmodel, entidade, null, null);
        }
        
    }   

    private static void showWorkflowsSteps(InfModel infmodel, Resource s, Property p, Resource o) {
        
        for (StmtIterator i = infmodel.listStatements(s, p, o); i.hasNext(); ) {
            Statement stmt = i.nextStatement();
            System.out.println("\n - " + PrintUtil.print(stmt));
            //System.out.println("\n : " + stmt.getObject());
        }
        
    }

    
}

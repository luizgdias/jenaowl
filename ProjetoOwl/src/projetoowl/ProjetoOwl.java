/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoowl;

import java.util.Iterator;
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
import org.apache.jena.util.PrintUtil;

/**
 *
 * @author luizgustavo
 */
public class ProjetoOwl {

    public static void main(String[] args) {
        String diretorio = "/home/luizgustavo/NetBeansProjects/ProjetoOwl/src/projetoowl/ontology2.owl";
        FileManager.get().addLocatorClassLoader(ProjetoOwl.class.getClassLoader());
        Model data = FileManager.get().loadModel("file:/home/luizgustavo/NetBeansProjects/ProjetoOwl/src/projetoowl/ontology.owl");
        Model schema = FileManager.get().loadModel("https://www.w3.org/2000/01/rdf-schema"); 
        
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        
        InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
        Resource ontologia = infmodel.getResource("http://www.semanticweb.org/luizgustavo/ontologies/2019/0/untitled-ontology-2#");
        
        /* Funções executáveis:
        * showAllAboutWorkflow: Lista todas as informações sobre a ontologia
        * showInfoAboutSpecificWorkflow: Lista a classe de uma instância específica
        * listRelationsAndIndividuals: Lista os relacionamentos de uma entidade específica
        */ 
        
        
        //showAllAboutWorkflow(infmodel, ontologia, null, null);
        //showInfoAboutSpecificWorkflow(infmodel, ontologia, diretorio, "atividade1");
        listRelationsAndIndividuals(infmodel, ontologia, diretorio, "experimento1");
        listEquivalentWorkflows(infmodel, diretorio, "experimento1");
    }   

    private static void showAllAboutWorkflow(InfModel infmodel, Resource ontologia, Property p, Resource o) {
        String objeto, objeto1 = "workflow";
        String link;
        
        for (int i = 1; i < 4; i++) {
            objeto = objeto1+""+i;
            System.out.println("\n** Data about "+objeto+" **");
            link = ontologia+objeto;
            Resource entidade = infmodel.getResource(link);
            
            for (StmtIterator j = infmodel.listStatements(entidade, p, o); j.hasNext(); ) {
                Statement stmt = j.nextStatement();
                System.out.println("\n - " + PrintUtil.print(stmt));
                //System.out.println("\n : " + stmt.getObject());
                System.out.println("\n asdasd: " + stmt.getClass());
            }           
        }   
    }
    private static void showInfoAboutSpecificWorkflow(InfModel modelo, Resource ontologia, String diretorio, String instancia) {
        System.out.println("**Inferencia**");
        //System.out.println(diretorio);
        OntModel base = ModelFactory.createOntologyModel();
        base.read(diretorio, "OWL/XML");
        
        
        String url = "http://www.semanticweb.org/luizgustavo/ontologies/2019/0/untitled-ontology-2#";
        OntClass classeInstancia = base.getOntClass(url + instancia);
        Individual individuo = base.createIndividual(url + instancia, classeInstancia);
        
        for (Iterator<Resource> i = individuo.listRDFTypes(true); i.hasNext();) {
            System.out.print("\nInstância: "+ individuo.getLocalName()+ "\nfaz parte da classe \n" + i.next().getLocalName() + "\n");
            
            //System.out.print("\n "+ workflow3.getURI() + "\n faz parte da classe \n" + i.next() + "\n");
        }
        
        
        
        
    }
    private static void listRelationsAndIndividuals(InfModel infmodel, Resource ontologia, String diretorio, String instancia) {
        OntModel base = ModelFactory.createOntologyModel();
        base.read(diretorio, "OWL/XML");
        
        
        String url = "http://www.semanticweb.org/luizgustavo/ontologies/2019/0/untitled-ontology-2#";
        OntClass classeInstancia = base.getOntClass(url + instancia);
        Individual individuo = base.createIndividual(url + instancia, classeInstancia);
       
        
        System.out.println("**Relations and Individuals**");
        System.out.println("*Instancia*: "+ individuo.getLocalName());
        
         for (StmtIterator j = individuo.listProperties(); j.hasNext(); ) {
                    Statement s = j.next();
                    System.out.print( "    " + s.getPredicate().getLocalName() + " -> " );

                    if (s.getObject().isLiteral()) {
                        System.out.println( s.getLiteral().getLexicalForm() );
                    }
                    else {
                        System.out.println( s.getObject() );
                    }
        }   
    }
    private static void listEquivalentWorkflows(InfModel infmodel, String diretorio, String experimento) {
        System.out.println("**Inferencia**");
        //System.out.println(diretorio);
        OntModel base = ModelFactory.createOntologyModel();
        base.read(diretorio, "OWL/XML");
        
        
        String url = "http://www.semanticweb.org/luizgustavo/ontologies/2019/0/untitled-ontology-2#";
        OntClass classeInstancia = base.getOntClass(url + experimento);
        Individual individuo = base.createIndividual(url + experimento, classeInstancia);
        
        for (Iterator<Resource> i = individuo.listRDFTypes(true); i.hasNext();) {
            System.out.print("\nInstância: "+ individuo.getLocalName()+ "\nfaz parte da classe \n" + i.next().getLocalName() + "\n");
            
            //System.out.print("\n "+ workflow3.getURI() + "\n faz parte da classe \n" + i.next() + "\n");
        }
    }
}

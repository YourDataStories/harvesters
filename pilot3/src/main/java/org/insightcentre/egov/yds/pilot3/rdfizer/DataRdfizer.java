/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insightcentre.egov.yds.pilot3.rdfizer;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.util.NodeFactoryExtra;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.hp.hpl.jena.tdb.sys.TDBInternal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Islam A. Hassan
 */
public class DataRdfizer {

    static Model model;
    static DatasetGraphTDB dsg = TDBInternal.getBaseDatasetGraphTDB(TDBFactory.createDatasetGraph());
    static Graph graph = dsg.getDefaultGraph();

    private static void parseCSV(File fileLocation) throws FileNotFoundException, JSONException {
        if (fileLocation.getName().contains("csv")) {
            //Get scanner instance
            Scanner scanner = new Scanner(fileLocation);
            while (scanner.hasNext()) {
                String[] line = scanner.next().split(",");
                //System.out.println(line[0] + "|"+line[1]);
//           §
                if (line.length == 3) {
                    creatRDF(line);
                }
            }

            scanner.close();
        }
    }
    private static void parseCSV(String fileContent) throws FileNotFoundException, JSONException {
            //Get scanner instance
            Scanner scanner = new Scanner(fileContent);
            while (scanner.hasNext()) {
                String[] line = scanner.next().split(",");
                //System.out.println(line[0] + "|"+line[1]);
//           §
                if (line.length == 3) {
                    creatRDF(line);
                }
            }
            scanner.close();
        
    }

    public static void run(String csvFileContent) throws FileNotFoundException, JSONException {
        File dir = new File("/Users/Home/Documents/YDS/ExtendedPaper/graph/pilot3");
        File[] directoryListing = dir.listFiles();
       
            parseCSV(csvFileContent);
        
    }

    private static void creatRDF(String[] line) {
        String s = line[0];
        String p = line[1];
        String o = line[2];

        Node nodeS = Node.createURI(s);
        Node nodeP = Node.createURI("<" + p + ">");
        Node nodeO = null;

        if (o.contains("http")) {
            nodeO = Node.createURI("<" + o + ">");
        } else if (o.contains(".")) {
            //double
            NodeFactoryExtra c = new NodeFactoryExtra();
            nodeO = c.doubleToNode(Double.parseDouble(o));
        } else {
            nodeO = Node.createLiteral(o);
        }
        Triple triple = Triple.create(nodeS, nodeS, nodeS);

        //Statement sta = ResourceFactory.createStatement(s, p, o);
        graph.add(triple);

    }

    static void rdfWriter(Model model, String filename, boolean time) throws FileNotFoundException,
            IOException {
        //get current date time with Date()
        Date now = new Date();
        OutputStream out;
        if (time) {
            out = new FileOutputStream(filename + "_" + now.getHours() + now.getMinutes() + ".rdf");
        } else {
            out = new FileOutputStream(filename + ".rdf");
        }
        model.write(out, "RDF/XML");
        out.close();
    }
    public static void rdfWriter() throws IOException{
            rdfWriter(model, "contractsOwl", true);
    }
    

    public static void DataRdfizer(String arg[]) throws FileNotFoundException, JSONException, IOException {
        model = ModelFactory.createModelForGraph(graph) ;
        modelReadRDF();
    }

    private static void modelReadRDF() throws FileNotFoundException, IOException {
        Model model = ModelFactory.createDefaultModel();
        model.read("/Users/Home/yds/pilot3/owl/contractsOwl.rdf");
        rdfWriter(model, "contractsOwl", true);
    }
}

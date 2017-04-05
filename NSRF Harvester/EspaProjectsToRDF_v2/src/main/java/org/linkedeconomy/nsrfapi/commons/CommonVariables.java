package org.linkedeconomy.nsrfapi.commons;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author G. Vafeiadis
 */
public class CommonVariables {

    /**
     *
     * Variables that are commonly used by other methods
     *
     */
    static DateFormat dfCurrent = new SimpleDateFormat("yyyy/MM/dd");
    static Calendar calobj = Calendar.getInstance();
    @SuppressWarnings("StaticNonFinalUsedInInitialization")
    static String text = dfCurrent.format(calobj.getTime());
    @SuppressWarnings("StaticNonFinalUsedInInitialization")
    public static String currentDate = text.replace("/", "-");

    public static Model model = ModelFactory.createDefaultModel();
    public static Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
    @SuppressWarnings("StaticNonFinalUsedInInitialization")
    public static InfModel infModel = ModelFactory.createInfModel(reasoner, model);

    public static DecimalFormat dfNumberThree = new DecimalFormat("0.000");
    public static DecimalFormat dfNumber = new DecimalFormat("0.00");
    public static DecimalFormat dfNumberOne = new DecimalFormat("0.0");
    public static DecimalFormat dfNoDecimal = new DecimalFormat("0");

    public static String serverPath = "/home/ncsr/nsrf_rdf/";

}

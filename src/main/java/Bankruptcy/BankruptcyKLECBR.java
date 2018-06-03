package Bankruptcy;

import KLECBR.KLECBR;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.util.FileIO;
import org.apache.commons.logging.LogFactory;
import weka.LogisticRegression;
import jcolibri.cbrcore.Connector;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class BankruptcyKLECBR implements KLECBR {

    private static BankruptcyKLECBR _instance = null;
    Connector _connector;
    CBRCaseBase _caseBase;
    Collection<CBRCase> localCaseBase;
    BankruptcySolution _solution;
    ArrayList<CBRCase> _fortioriCase;
    NNConfig _simConfig;

    public static BankruptcyKLECBR getInstance() {
        if(_instance == null) {
            _instance = new BankruptcyKLECBR();
        }

        return _instance;
    }

    @Override
    public void configure() throws ExecutionException {
        HSQLDBserver.init();
        _caseBase = new LinealCaseBase();
        _connector = new DataBaseConnector();
        URL url = FileIO.findFile("src/main/java/Bankruptcy/databaseconfig.xml");
        _connector.initFromXMLfile(url);
    }

    @Override
    public CBRCaseBase preCycle() throws ExecutionException {
        _caseBase.init(_connector);
        return _caseBase;
    }

    @Override
    public void cycle(CBRQuery cbrQuery) throws ExecutionException {
        NNConfig simConfig = new NNConfig();
        setUpNNConfig(simConfig);

        _simConfig = simConfig;

        // This creates a sorted rank of all the cases seems kind of expensive imo
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), cbrQuery, simConfig);

        Iterator var2 = eval.iterator();

        while(var2.hasNext()) {
            RetrievalResult help = (RetrievalResult) var2.next();
            System.out.println(help);
        }

        _solution = (BankruptcySolution) calculateSolution(eval, 20);
        localCaseBase = createLocalCaseBase(eval, 20);

        Iterator var6 = localCaseBase.iterator();

        while(var6.hasNext()) {
            CBRCase nse = (CBRCase)var6.next();
            BankruptcySolution ms =(BankruptcySolution) nse.getSolution();
            System.out.println(ms.getClassification());
            System.out.println(nse);
        }

        URL savedCaseBase = FileIO.findFile("data/breastcancer/localcasebase.arff");
        URL fileTemplate = FileIO.findFile("data/breastcancer/breastCancerTemplate.arff");
        URL outputFile = FileIO.findFile("data/breastcancer/output.arff");

        CBRCase queryCase = new CBRCase();
        queryCase.setDescription(cbrQuery.getDescription());
        queryCase.setSolution(_solution);

        BufferedWriter bw;
        BufferedWriter outputWriter;
        BufferedReader br;

        ArrayList<CBRCase> outputCases = new ArrayList<>();
        outputCases.add(queryCase);
        outputCases.addAll(_fortioriCase);

        ArrayList<String> lines = getArffCases(localCaseBase);
        ArrayList<String> outputStrings = getArffCases(outputCases);
        String line;

        try {
            br = new BufferedReader(new FileReader(fileTemplate.getFile()));
            bw = new BufferedWriter(new FileWriter(savedCaseBase.getFile()));
            outputWriter = new BufferedWriter(new FileWriter(outputFile.getFile()));

            outputWriter.write("");
            bw.write("");
            while((line = br.readLine()) != null) {
                bw.append(line);
                bw.newLine();
                outputWriter.append(line);
                outputWriter.newLine();
            }
            for(String cases : lines) {
                bw.append(cases);
                bw.newLine();
            }

            for(String oStrings: outputStrings) {
                outputWriter.append(oStrings);
                outputWriter.newLine();
            }

            br.close();
            bw.close();
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postCycle() throws ExecutionException {

    }

    @Override
    public void setUpNNConfig(NNConfig simConfig) {

    }

    @Override
    public Collection<CBRCase> createLocalCaseBase(Collection<RetrievalResult> cases, int k) {
        return null;
    }

    @Override
    public ArrayList<String> getArffCases(Collection<CBRCase> localCaseBase) {
        return null;
    }

    @Override
    public void printCaseBase() {

    }

    @Override
    public CBRQuery createQuery() {
        return null;
    }

    @Override
    public void fortioriCase(ArrayList<CBRCase> caseBase, CBRCase cbrCase) {

    }

    @Override
    public CaseComponent calculateSolution(Collection<RetrievalResult> cases, int k) {
        return null;
    }

    @Override
    public BankruptcySolution getSolution() {
        return _solution;
    }

    public static void main(String[] args) {
        BankruptcyKLECBR klecbr = getInstance();
        CBRQuery query = klecbr.createQuery();

        try {
            klecbr.configure();
            klecbr.preCycle();
            klecbr.cycle(query);
            System.out.println("Solution of the case is: " + klecbr.getSolution().getClassification());
            klecbr.postCycle();
            for (CBRCase fcases : klecbr._fortioriCase) {
                BankruptcySolution ms = (BankruptcySolution) fcases.getSolution();
                System.out.println("AN E and a P " + ms.getClassification());
            }

        } catch (Exception var6) {
            LogFactory.getLog(BankruptcyKLECBR.class).error(var6);
        }

        /*
        LogisticRegression logisticRegression = new LogisticRegression();
        ArrayList<HashMap<String, Double>> ratios = new ArrayList<>();

        String solution = klecbr._solution.get_classification() == 2 ? "benign" : "malignant";
        KLECBR.KLECaseComponent kleCaseComponent = (KLECBR.KLECaseComponent) query.getDescription();

        KLECBR.ExplanationGenerator explainer = new KLECBR.ExplanationGenerator("data/breastcancer/explanation.txt", solution, kleCaseComponent, klecbr._fortioriCase.get(0));

        try {
            ratios = logisticRegression.processNumeric(
                    "/data/breastcancer/breastCancer.arff",
                    "/data/breastcancer/output.arff",
                    9
            );
            explainer.generateExplanation(ratios.get(0), ratios.get(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

}

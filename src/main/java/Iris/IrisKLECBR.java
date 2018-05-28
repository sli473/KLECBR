package Iris;

import KLECBR.KLECBR;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.*;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.connector.DataBaseConnector;
import jcolibri.util.FileIO;
import org.apache.commons.logging.LogFactory;
import weka.LogisticRegression;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.net.URL;
import java.util.Iterator;


public class IrisKLECBR implements KLECBR {
    private static IrisKLECBR _instance = null;
    Connector _connector;
    CBRCaseBase _caseBase;
    Collection<CBRCase> localCaseBase;
    IrisSolution _solution;
    ArrayList<CBRCase> _fortioriCase;
    NNConfig _simConfig;

    public static IrisKLECBR getInstance(){
        if(_instance == null) {
            _instance = new IrisKLECBR();
        }

        return _instance;
    }


    @Override
    public void configure() throws ExecutionException {
        HSQLDBserver.init();
        _caseBase = new LinealCaseBase();
        _connector = new DataBaseConnector();
        URL url = FileIO.findFile("src/main/java/Iris/databaseconfig.xml");
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

        _solution = (IrisSolution)calculateSolution(eval, 20);
        localCaseBase = createLocalCaseBase(eval, 20);

        Iterator var6 = localCaseBase.iterator();

        while(var6.hasNext()) {
            CBRCase nse = (CBRCase)var6.next();
            IrisSolution ms =(IrisSolution) nse.getSolution();
            System.out.println(ms.get_species());
            System.out.println(nse);
        }

        URL savedCaseBase = FileIO.findFile("data/iris/localcasebase.arff");
        URL fileTemplate = FileIO.findFile("data/iris/irisTemplate.arff");
        URL outputFile = FileIO.findFile("data/iris/output.arff");

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

        LogisticRegression logisticRegression = new LogisticRegression();
        try {
            logisticRegression.processNumeric();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postCycle() throws ExecutionException {
        this._connector.close();
        HSQLDBserver.shutDown();
    }

    @Override
    public void setUpNNConfig(NNConfig simConfig) {
        simConfig.setDescriptionSimFunction(new Average());
        simConfig.addMapping(new Attribute("_sepalLengthCm", IrisDescription.class), new Interval(3.6D));
        simConfig.addMapping(new Attribute("_sepalWidthCm", IrisDescription.class), new Interval(2.4D));
        simConfig.addMapping(new Attribute("_petalLengthCm", IrisDescription.class), new Interval(5.9D));
        simConfig.addMapping(new Attribute("_petalWidthCm", IrisDescription.class), new Interval(2.4D));
    }

    @Override
    public Collection<CBRCase> createLocalCaseBase(Collection<RetrievalResult> cases, int k) {
        ArrayList<CBRCase> res = new ArrayList();
        ArrayList<CBRCase> setosa = new ArrayList();
        ArrayList<CBRCase> versicolor = new ArrayList();
        Iterator<RetrievalResult> iterator = cases.iterator();
        int casesSupportingClassification = 0;
        int casesAgainstClassification = 0;
        CBRCase currentCase;
        IrisSolution caseSolution;
        String isSetosa;

        while (iterator.hasNext() && (casesSupportingClassification < k || casesAgainstClassification < k)) {
            RetrievalResult rs = iterator.next();
            currentCase = rs.get_case();
            caseSolution = (IrisSolution) currentCase.getSolution();
            isSetosa = caseSolution.get_species();
            if (isSetosa.equals("Iris-setosa") && casesSupportingClassification < k) {
                setosa.add(currentCase);
                res.add(currentCase);
                casesSupportingClassification++;
                System.out.println("Print true case" + rs);
            } else if (isSetosa.equals("Iris-versicolor") && casesAgainstClassification < k) {
                versicolor.add(currentCase);
                res.add(currentCase);
                casesAgainstClassification++;
                System.out.println("Print false case" + rs);
            }

        }

        String solution = _solution.get_species();

        if (solution.equals("Iris-setosa")) {
            fortioriCase(setosa, versicolor.get(0));
        } else {
            fortioriCase(versicolor, setosa.get(0));
        }


        return res;
    }

    @Override
    public ArrayList<String> getArffCases(Collection<CBRCase> localCaseBase) {
        ArrayList<String> arffText = new ArrayList<>();
        Iterator<CBRCase> caseIterator = localCaseBase.iterator();
        StringBuilder stringBuilder;

        while(caseIterator.hasNext()) {
            CBRCase cbrCase = caseIterator.next();
            IrisSolution solution = (IrisSolution)cbrCase.getSolution();
            IrisDescription description = (IrisDescription)cbrCase.getDescription();
            stringBuilder = new StringBuilder();
            stringBuilder.append(description.get_sepalLengthCm() + ",");
            stringBuilder.append(description.get_sepalWidthCm() + ",");
            stringBuilder.append(description.get_petalLengthCm() + ",");
            stringBuilder.append(description.get_petalWidthCm() + ",");
            stringBuilder.append(solution.get_species());
            arffText.add(stringBuilder.toString());
        }
        return arffText;
    }

    @Override
    public void printCaseBase() {
        Collection<CBRCase> cb = _caseBase.getCases();
        Iterator var3 = cb.iterator();

        while(var3.hasNext()) {
            CBRCase c = (CBRCase)var3.next();
            IrisDescription d = (IrisDescription) c.getDescription();
            System.out.println(d.get_caseId());
            IrisSolution s = (IrisSolution) c.getSolution();
            System.out.println("solution"+ s.get_species());
            System.out.println(c);
        }
    }

    @Override
    public CBRQuery createQuery() {
        IrisDescription queryDesc = new IrisDescription();
        queryDesc.set_sepalLengthCm(5.1);
        queryDesc.set_sepalWidthCm(3.5);
        queryDesc.set_petalLengthCm(1.4);
        queryDesc.set_petalWidthCm(0.2);
        CBRQuery query = new CBRQuery();
        query.setDescription(queryDesc);
        return query;
    }

    @Override
    public void fortioriCase(ArrayList<CBRCase> caseBase, CBRCase cbrCase) {
        CBRQuery query = new CBRQuery();
        query.setDescription(cbrCase.getDescription());

        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase, query, _simConfig);
        double maxSim = 0;
        CBRCase currentMax = caseBase.get(0);

        for(RetrievalResult r: eval) {
            System.out.println("TELL ME THE R VALUE" + r.getEval());
            if(r.getEval() > maxSim) {
                currentMax = r.get_case();
                maxSim = r.getEval();
            }
        }

        _fortioriCase = new ArrayList<>();
        _fortioriCase.add(currentMax);
        _fortioriCase.add(cbrCase);
    }

    @Override
    public CaseComponent calculateSolution(Collection<RetrievalResult> cases, int k) {
        Iterator<RetrievalResult> iterator = cases.iterator();
        int casesSupportingClassification = 0;
        double supportingAccumulation = 0;
        int casesAgainstClassification = 0;
        double againstAccumulation = 0;
        CBRCase currentCase;
        IrisSolution caseSolution = new IrisSolution();
        String isSetosa;

        while (iterator.hasNext() && (casesSupportingClassification < k || casesAgainstClassification < k)) {
            RetrievalResult rs = iterator.next();
            currentCase = rs.get_case();
            caseSolution = (IrisSolution) currentCase.getSolution();
            isSetosa = caseSolution.get_species();
            if (isSetosa.equals("Iris-setosa") && casesSupportingClassification < k) {
                supportingAccumulation += rs.getEval();
                casesSupportingClassification++;
                System.out.println("Print true case" + rs);
            } else if (isSetosa.equals("Iris-versicolor") && casesAgainstClassification < k) {
                againstAccumulation += rs.getEval();
                casesAgainstClassification++;
                System.out.println("Print false case" + rs);
            }
        }

        if(supportingAccumulation > againstAccumulation) {
            caseSolution.set_species("Iris-setosa");
        } else {
            caseSolution.set_species("Iris-versicolor");
        }

        return caseSolution;
    }

    @Override
    public IrisSolution getSolution() {
        return _solution;
    }

    public static void main(String[] args) {
        IrisKLECBR klecbr = getInstance();

        try {
            klecbr.configure();
            klecbr.preCycle();
            CBRQuery query = klecbr.createQuery();
            klecbr.cycle(query);
            System.out.println("Solution of the case is: " + klecbr.getSolution().get_species());
            klecbr.postCycle();
            for(CBRCase fcases: klecbr._fortioriCase) {
                IrisSolution ms = (IrisSolution) fcases.getSolution();
                System.out.println("AN E and a P"+ ms.get_species());
            }

        } catch (Exception var6) {
            LogFactory.getLog(IrisKLECBR.class).error(var6);
        }
    }
}

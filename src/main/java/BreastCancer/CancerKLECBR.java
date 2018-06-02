package BreastCancer;

import KLECBR.KLECBR;
import KLECBR.ExplanationGenerator;
import KLECBR.KLECaseComponent;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.*;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.util.FileIO;
import org.apache.commons.logging.LogFactory;
import weka.LogisticRegression;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CancerKLECBR implements KLECBR {
    private static CancerKLECBR _instance = null;
    Connector _connector;
    CBRCaseBase _caseBase;
    Collection<CBRCase> localCaseBase;
    CancerSolution _solution;
    ArrayList<CBRCase> _fortioriCase;
    NNConfig _simConfig;

    public static CancerKLECBR getInstance(){
        if(_instance == null) {
            _instance = new CancerKLECBR();
        }

        return _instance;
    }

    @Override
    public void configure() throws ExecutionException {
        HSQLDBserver.init();
        _caseBase = new LinealCaseBase();
        _connector = new DataBaseConnector();
        URL url = FileIO.findFile("src/main/java/BreastCancer/databaseconfig.xml");
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

        _solution = (CancerSolution) calculateSolution(eval, 20);
        localCaseBase = createLocalCaseBase(eval, 20);

        Iterator var6 = localCaseBase.iterator();

        while(var6.hasNext()) {
            CBRCase nse = (CBRCase)var6.next();
            CancerSolution ms =(CancerSolution) nse.getSolution();
            System.out.println(ms.get_classification());
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
        this._connector.close();
        HSQLDBserver.shutDown();
    }

    @Override
    public void setUpNNConfig(NNConfig simConfig) {
        simConfig.setDescriptionSimFunction(new Average());
        simConfig.addMapping(new Attribute("_clumpThickness", CancerDescription.class), new Interval(10));
        simConfig.addMapping(new Attribute("_uniformityOfCellSize", CancerDescription.class), new Interval(10));
        simConfig.addMapping(new Attribute("_uniformityOfCellShape", CancerDescription.class), new Interval(10));
        simConfig.addMapping(new Attribute("_marginalAdhesion", CancerDescription.class), new Interval(10));
        simConfig.addMapping(new Attribute("_singleEpithelialCellSize", CancerDescription.class), new Interval(10));
        simConfig.addMapping(new Attribute("_bareNuclei", CancerDescription.class), new Interval(10));
        simConfig.addMapping(new Attribute("_blandChromatin", CancerDescription.class), new Interval(10));
        simConfig.addMapping(new Attribute("_normalNucleoli", CancerDescription.class), new Interval(10));
        simConfig.addMapping(new Attribute("_mitoses", CancerDescription.class), new Interval(10));
    }

    @Override
    public Collection<CBRCase> createLocalCaseBase(Collection<RetrievalResult> cases, int k) {
        ArrayList<CBRCase> res = new ArrayList();
        ArrayList<CBRCase> benign = new ArrayList();
        ArrayList<CBRCase> malignant = new ArrayList();
        Iterator<RetrievalResult> iterator = cases.iterator();
        int casesSupportingClassification = 0;
        int casesAgainstClassification = 0;
        CBRCase currentCase;
        CancerSolution caseSolution;
        int isBenign;

        while (iterator.hasNext() && (casesSupportingClassification < k || casesAgainstClassification < k)) {
            RetrievalResult rs = iterator.next();
            currentCase = rs.get_case();
            caseSolution = (CancerSolution) currentCase.getSolution();
            isBenign = caseSolution.get_classification();
            if (isBenign == 2 && casesSupportingClassification < k) {
                benign.add(currentCase);
                res.add(currentCase);
                casesSupportingClassification++;
                System.out.println("Print true case" + rs);
            } else if (isBenign == 4 && casesAgainstClassification < k) {
                malignant.add(currentCase);
                res.add(currentCase);
                casesAgainstClassification++;
                System.out.println("Print false case" + rs);
            }

        }

        int solution = _solution.get_classification();

        if (solution == 2) {
            fortioriCase(benign, malignant.get(0));
        } else {
            fortioriCase(malignant, benign.get(0));
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
            CancerSolution solution = (CancerSolution)cbrCase.getSolution();
            CancerDescription description = (CancerDescription)cbrCase.getDescription();
            stringBuilder = new StringBuilder();
            stringBuilder.append(description.get_clumpThickness() + ",");
            stringBuilder.append(description.get_uniformityOfCellSize() + ",");
            stringBuilder.append(description.get_uniformityOfCellShape() + ",");
            stringBuilder.append(description.get_marginalAdhesion() + ",");
            stringBuilder.append(description.get_singleEpithelialCellSize() + ",");
            stringBuilder.append(description.get_bareNuclei() + ",");
            stringBuilder.append(description.get_blandChromatin() + ",");
            stringBuilder.append(description.get_normalNucleoli() + ",");
            stringBuilder.append(description.get_mitoses() + ",");
            stringBuilder.append(solution.get_classification());
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
            CancerDescription d = (CancerDescription) c.getDescription();
            System.out.println(d.get_caseId());
            CancerSolution s = (CancerSolution) c.getSolution();
            System.out.println("solution"+ s.get_classification());
            System.out.println(c);
        }
    }

    @Override
    public CBRQuery createQuery() {
        CancerDescription queryDesc = new CancerDescription();
        queryDesc.set_clumpThickness(4);
        queryDesc.set_uniformityOfCellSize(8);
        queryDesc.set_uniformityOfCellShape(6);
        queryDesc.set_marginalAdhesion(4);
        queryDesc.set_singleEpithelialCellSize(3);
        queryDesc.set_bareNuclei(4);
        queryDesc.set_blandChromatin(10);
        queryDesc.set_normalNucleoli(6);
        queryDesc.set_mitoses(1);
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
        CancerSolution caseSolution = new CancerSolution();
        int isBenign;

        while (iterator.hasNext() && (casesSupportingClassification < k || casesAgainstClassification < k)) {
            RetrievalResult rs = iterator.next();
            currentCase = rs.get_case();
            caseSolution = (CancerSolution) currentCase.getSolution();
            isBenign = caseSolution.get_classification();
            if (isBenign == 2 && casesSupportingClassification < k) {
                supportingAccumulation += rs.getEval();
                casesSupportingClassification++;
                System.out.println("Print true case" + rs);
            } else if (isBenign == 4 && casesAgainstClassification < k) {
                againstAccumulation += rs.getEval();
                casesAgainstClassification++;
                System.out.println("Print false case" + rs);
            }
        }

        if(supportingAccumulation > againstAccumulation) {
            caseSolution.set_classification(2);
        } else {
            caseSolution.set_classification(4);
        }

        return caseSolution;
    }

    @Override
    public CancerSolution getSolution() {
        return _solution;
    }

    public static void main(String[] args) {
        CancerKLECBR klecbr = getInstance();
        CBRQuery query = klecbr.createQuery();

        try {
            klecbr.configure();
            klecbr.preCycle();
            klecbr.cycle(query);
            System.out.println("Solution of the case is: " + klecbr.getSolution().get_classification());
            klecbr.postCycle();
            for (CBRCase fcases : klecbr._fortioriCase) {
                CancerSolution ms = (CancerSolution) fcases.getSolution();
                System.out.println("AN E and a P " + ms.get_classification());
            }

        } catch (Exception var6) {
            LogFactory.getLog(CancerKLECBR.class).error(var6);
        }

        LogisticRegression logisticRegression = new LogisticRegression();
        ArrayList<HashMap<String, Double>> ratios = new ArrayList<>();

        String solution = klecbr._solution.get_classification() == 2 ? "benign" : "malignant";
        KLECaseComponent kleCaseComponent = (KLECaseComponent) query.getDescription();

        ExplanationGenerator explainer = new ExplanationGenerator("data/breastcancer/explanation.txt", solution, kleCaseComponent, klecbr._fortioriCase.get(0));

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
    }
}

package Mushroom;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.*;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
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
import KLECBR.ExplanationGenerator;
import KLECBR.KLECaseComponent;

import KLECBR.KLECBR;

public class MushroomKLECBR implements KLECBR {
    private static MushroomKLECBR _instance = null;
    Connector _connector;
    CBRCaseBase _caseBase;
    Collection<CBRCase> localCaseBase;
    MushroomSolution _solution;
    ArrayList<CBRCase> _fortioriCase;
    NNConfig _simConfig;

    public static MushroomKLECBR getInstance() {
        if (_instance == null) {
            _instance = new MushroomKLECBR();
        }

        return _instance;
    }

    private MushroomKLECBR() {
    }

    @Override
    public void configure() throws ExecutionException {
        HSQLDBserver.init();
        _caseBase = new LinealCaseBase();
        _connector = new DataBaseConnector();
        URL url = FileIO.findFile("src/main/java/Mushroom/databaseconfig.xml");
        System.out.println("what is the url you found? " + url);
        _connector.initFromXMLfile(FileIO.findFile("src/main/java/Mushroom/databaseconfig.xml"));
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

        _solution = (MushroomSolution)calculateSolution(eval, 20);
        localCaseBase = createLocalCaseBase(eval, 20);
        _solution = (MushroomSolution)calculateSolution(eval, 20);

        Iterator var6 = localCaseBase.iterator();

        while(var6.hasNext()) {
            CBRCase nse = (CBRCase)var6.next();
            MushroomSolution ms =(MushroomSolution) nse.getSolution();
            System.out.println(ms.is_isPoisonous());
            System.out.println(nse);
        }

        URL savedCaseBase = FileIO.findFile("data/mushrooms/localcasebase.arff");
        URL fileTemplate = FileIO.findFile("data/mushrooms/mushroomTemplate.arff");
        URL outputFile = FileIO.findFile("data/mushrooms/output.arff");

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
        simConfig.addMapping(new Attribute("_capShape", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_capSurface", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_capColour", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_bruises", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_odor", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_gillAttachment", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_gillSpacing", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_gillSize", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_gillColor", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_stalkShape", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_stalkRoot", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_stalkSurfaceAboveRing", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_stalkSurfaceBelowRing", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_stalkColourAboveRing", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_stalkColourBelowRing", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_veilType", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_veilColour", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_ringNumber", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_ringType", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_sporePrintColor", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_population", MushroomDescription.class), new Equal());
        simConfig.addMapping(new Attribute("_habitat", MushroomDescription.class), new Equal());
    }

    @Override
    public Collection<CBRCase> createLocalCaseBase(Collection<RetrievalResult> cases, int k) {
        ArrayList<CBRCase> res = new ArrayList();
        ArrayList<CBRCase> poisonousCases = new ArrayList();
        ArrayList<CBRCase> edibleCases = new ArrayList();
        Iterator<RetrievalResult> iterator = cases.iterator();
        int casesSupportingClassification = 0;
        int casesAgainstClassification = 0;
        CBRCase currentCase;
        MushroomSolution caseSolution;
        String isPoisonous;

        while (iterator.hasNext() && (casesSupportingClassification < k || casesAgainstClassification < k)) {
            RetrievalResult rs = iterator.next();
            currentCase = rs.get_case();
            caseSolution = (MushroomSolution) currentCase.getSolution();
            isPoisonous = caseSolution.is_isPoisonous();
            if (isPoisonous.equals("e") && casesSupportingClassification < k) {
                edibleCases.add(currentCase);
                res.add(currentCase);
                casesSupportingClassification++;
                System.out.println("Print true case" + rs);
            } else if (isPoisonous.equals("p") && casesAgainstClassification < k) {
                poisonousCases.add(currentCase);
                res.add(currentCase);
                casesAgainstClassification++;
                System.out.println("Print false case" + rs);
            }

        }

        String solution = _solution.is_isPoisonous();

        if (solution.equals("e")) {
            fortioriCase(edibleCases, poisonousCases.get(0));
        } else {
            fortioriCase(poisonousCases, edibleCases.get(0));
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
            MushroomSolution solution = (MushroomSolution)cbrCase.getSolution();
            MushroomDescription description = (MushroomDescription)cbrCase.getDescription();
            stringBuilder = new StringBuilder();
            stringBuilder.append(solution.is_isPoisonous() +',');
            stringBuilder.append(description.get_capShape() + ',');
            stringBuilder.append(description.get_capSurface() + ',');
            stringBuilder.append(description.get_capColour() + ',');
            stringBuilder.append(description.get_bruises() + ',');
            stringBuilder.append(description.get_odor() + ',');
            stringBuilder.append(description.get_gillAttachment() + ',');
            stringBuilder.append(description.get_gillSpacing() + ',');
            stringBuilder.append(description.get_gillSize() + ',');
            stringBuilder.append(description.get_gillColor() + ',');
            stringBuilder.append(description.get_stalkShape() + ',');
            stringBuilder.append(description.get_stalkRoot() + ',');
            stringBuilder.append(description.get_stalkSurfaceAboveRing() + ',');
            stringBuilder.append(description.get_stalkSurfaceBelowRing() + ',');
            stringBuilder.append(description.get_stalkColourAboveRing() + ',');
            stringBuilder.append(description.get_stalkColourBelowRing() + ',');
            stringBuilder.append(description.get_veilType() + ',');
            stringBuilder.append(description.get_veilColour() + ',');
            stringBuilder.append(description.get_ringNumber() + ',');
            stringBuilder.append(description.get_ringType() + ',');
            stringBuilder.append(description.get_sporePrintColor() + ',');
            stringBuilder.append(description.get_population() + ',');
            stringBuilder.append(description.get_habitat());
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
            MushroomDescription d = (MushroomDescription) c.getDescription();
            System.out.println(d.get_bruises());
            MushroomSolution s = (MushroomSolution) c.getSolution();
            System.out.println("solution"+ s.is_isPoisonous());
            System.out.println(c);
        }
    }

    @Override
    public CBRQuery createQuery() {
        MushroomDescription queryDesc = new MushroomDescription();
        queryDesc.set_bruises("f");
        queryDesc.set_capColour("w");
        queryDesc.set_capShape("f");
        queryDesc.set_capSurface("f");
        queryDesc.set_odor("n");
        queryDesc.set_gillAttachment("f");
        queryDesc.set_gillSpacing("w");
        queryDesc.set_gillSize("b");
        queryDesc.set_gillColor("k");
        queryDesc.set_stalkShape("t");
        queryDesc.set_stalkRoot("e");
        queryDesc.set_stalkSurfaceAboveRing("s");
        queryDesc.set_stalkSurfaceBelowRing("s");
        queryDesc.set_stalkColourAboveRing("w");
        queryDesc.set_stalkColourBelowRing("w");
        queryDesc.set_veilType("p");
        queryDesc.set_veilColour("w");
        queryDesc.set_ringNumber("o");
        queryDesc.set_ringType("p");
        queryDesc.set_sporePrintColor("n");
        queryDesc.set_population("y");
        queryDesc.set_habitat("u");
        CBRQuery query = new CBRQuery();
        query.setDescription(queryDesc);
        return query;
    }

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

        System.out.println("poison case" + cbrCase);
        System.out.println("edible case" + currentMax);

        _fortioriCase = new ArrayList<>();
        _fortioriCase.add(currentMax);
        _fortioriCase.add(cbrCase);
    }

    public CaseComponent calculateSolution(Collection<RetrievalResult> cases, int k) {
        MushroomSolution solution;
        Iterator<RetrievalResult> iterator = cases.iterator();
        int casesSupportingClassification = 0;
        double supportingAccumulation = 0;
        int casesAgainstClassification = 0;
        double againstAccumulation = 0;
        CBRCase currentCase;
        MushroomSolution caseSolution = new MushroomSolution();
        String isPoisonous;

        while (iterator.hasNext() && (casesSupportingClassification < k || casesAgainstClassification < k)) {
            RetrievalResult rs = iterator.next();
            currentCase = rs.get_case();
            caseSolution = (MushroomSolution) currentCase.getSolution();
            isPoisonous = caseSolution.is_isPoisonous();
            if (isPoisonous.equals("e") && casesSupportingClassification < k) {
                supportingAccumulation += rs.getEval();
                casesSupportingClassification++;
                System.out.println("Print true case" + rs);
            } else if (isPoisonous.equals("p") && casesAgainstClassification < k) {
                againstAccumulation += rs.getEval();
                casesAgainstClassification++;
                System.out.println("Print false case" + rs);
            }
        }

        if(supportingAccumulation > againstAccumulation) {
            caseSolution.set_isPoisonous("e");
        } else {
            caseSolution.set_isPoisonous("p");
        }

        return caseSolution;
    }

    public MushroomSolution getSolution() {
        return _solution;
    }

    public static void main(String[] args) {
        MushroomKLECBR klecbr = getInstance();
        CBRQuery query = klecbr.createQuery();

        try {
            klecbr.configure();
            klecbr.preCycle();
            klecbr.cycle(query);
            System.out.println("Solution of the case is: " + klecbr.getSolution().is_isPoisonous());
            klecbr.postCycle();
            for(CBRCase fcases: klecbr._fortioriCase) {
                MushroomSolution ms = (MushroomSolution) fcases.getSolution();
                System.out.println("AN E and a P"+ ms.is_isPoisonous());
            }

        } catch (Exception var6) {
            LogFactory.getLog(MushroomKLECBR.class).error(var6);
        }

        LogisticRegression logisticRegression = new LogisticRegression();
        ArrayList<HashMap<String, Double>> ratios;

        String solution = klecbr._solution.is_isPoisonous() == "e" ? "edible" : "poisonous";
        KLECaseComponent kleCaseComponent = (KLECaseComponent) query.getDescription();

        ExplanationGenerator explainer = new ExplanationGenerator("data/mushrooms/explanation.txt", solution, kleCaseComponent, klecbr._fortioriCase.get(0));

        try {
            ratios = logisticRegression.processCategorical(
                    "/data/mushrooms/localcasebase.arff",
                    "/data/mushrooms/output.arff",
                    0
            );

            explainer.generateExplanation(ratios.get(0), ratios.get(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

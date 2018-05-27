import Mushroom.HSQLDBserver;
import Mushroom.MushroomDescription;
import Mushroom.MushroomSolution;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.InitializingException;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.util.FileIO;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jcolibri.cbrcore.CBRCase;

import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import weka.LogisticRegression;

public class Main {

    static Connector _connector;
    static CBRCaseBase _caseBase;

    public static void main (String[] args) {

        Collection<CBRCase> localCaseBase;
        HSQLDBserver.init();

        _caseBase = new LinealCaseBase();
        _connector = new DataBaseConnector();
        try {
            URL url = FileIO.findFile("src/main/java/databaseconfig.xml");
            System.out.println("what is the url you found? " + url);
            _connector.initFromXMLfile(FileIO.findFile("src/main/java/databaseconfig.xml"));
        } catch (InitializingException e) {
            e.printStackTrace();
        }

        try {
            _caseBase.init(_connector);
        } catch (InitializingException e) {
            e.printStackTrace();
        }

        CBRQuery query = createQuery();

        NNConfig simConfig = new NNConfig();
        setUpNNConfig(simConfig);

        // This creates a sorted rank of all the cases seems kind of expensive imo
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);

        Iterator var2 = eval.iterator();

        while(var2.hasNext()) {
            RetrievalResult help = (RetrievalResult) var2.next();
            System.out.println(help);
        }

        localCaseBase = createLocalCaseBase(eval, 20);

        Iterator var6 = localCaseBase.iterator();

        while(var6.hasNext()) {
            CBRCase nse = (CBRCase)var6.next();
            MushroomSolution ms =(MushroomSolution) nse.getSolution();
            System.out.println(ms.is_isPoisonous());
            System.out.println(nse);
        }

        URL savedCaseBase = FileIO.findFile("data/localcasebase.arff");
        URL fileTemplate = FileIO.findFile("data/mushroomTemplate.arff");

        BufferedWriter bw;
        BufferedReader br;

        ArrayList<String> lines = getArffCases(localCaseBase);
        String line;

        try {
            br = new BufferedReader(new FileReader(fileTemplate.getFile()));
            bw = new BufferedWriter(new FileWriter(savedCaseBase.getFile()));
            bw.write("");
            while((line = br.readLine()) != null) {
                bw.append(line);
                bw.newLine();
            }
            for(String cases : lines) {
                bw.append(cases);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogisticRegression logisticRegression = new LogisticRegression();
        try {
            logisticRegression.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Collection<CBRCase> createLocalCaseBase(Collection<RetrievalResult> cases, int k) {
        ArrayList<CBRCase> res = new ArrayList();
        Iterator<RetrievalResult> iterator = cases.iterator();
        int casesSupportingClassification = 0;
        int casesAgainstClassification = 0;
        CBRCase currentCase;
        MushroomSolution caseSolution;
        String isPoisonous;

        while(iterator.hasNext() && (casesSupportingClassification < k || casesAgainstClassification < k)) {
            RetrievalResult rs = iterator.next();
            currentCase = rs.get_case();
            caseSolution = (MushroomSolution) currentCase.getSolution();
            isPoisonous = caseSolution.is_isPoisonous();
            if(isPoisonous.equals("e") && casesSupportingClassification < k) {
                res.add(currentCase);
                casesSupportingClassification++;
                System.out.println("Print true case" + rs);
            } else if(isPoisonous.equals("p") && casesAgainstClassification < k) {
                res.add(currentCase);
                casesAgainstClassification++;
                System.out.println("Print false case" + rs);
            }
        }
        return res;
    }

    public static CBRQuery createQuery() {
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

    public static void setUpNNConfig(NNConfig simConfig) {
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

    public static void printCaseBase() {
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

    public static ArrayList<String> getArffCases(Collection<CBRCase> localCaseBase) {
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
}

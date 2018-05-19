import jcolibri.casebase.CachedLinealCaseBase;
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
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.util.FileIO;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import jcolibri.method.retrieve.NNretrieval.similarity.StandardGlobalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;

public class Main {

    static Connector _connector;
    static CBRCaseBase _caseBase;

    public static void main (String[] args) {
        System.out.println("Hello world,");

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

        NNConfig simConfig = new NNConfig();
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

        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        eval = SelectCases.selectTopKRR(eval, 5);

        System.out.println();
        Iterator var6 = eval.iterator();

        while(var6.hasNext()) {
            RetrievalResult nse = (RetrievalResult)var6.next();
            System.out.println(nse);
        }
    }
}

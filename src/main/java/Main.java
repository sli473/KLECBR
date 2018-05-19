import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.InitializingException;
import jcolibri.util.FileIO;
import java.net.URL;

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

    }
}

import KLE.LogisticRegressionManager;
import jcolibri.casebase.CachedLinealCaseBase;
import smile.classification.LogisticRegression;

public class Main {

    public static void main (String[] args) {
        System.out.println("Hello world,");

        CaseBaseGenerator cb = new CaseBaseGenerator();
        cb.generate();

        LogisticRegressionManager lgm = new LogisticRegressionManager();
        lgm.parseArffFile();
        lgm.trainLogisticRegression();
    }
}

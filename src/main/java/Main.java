import weka.LogisticRegression;


public class Main {

    public static void main (String[] args) {
        System.out.println("Hello world,");

        LogisticRegression logisticRegression = new LogisticRegression();
        try {
            logisticRegression.process();
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        CaseBaseGenerator cb = new CaseBaseGenerator();
        cb.generate();
    }
}

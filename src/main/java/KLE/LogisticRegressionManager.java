package KLE;

import smile.classification.LogisticRegression;
import smile.data.AttributeDataset;
import smile.data.parser.ArffParser;

import java.io.FileInputStream;

/**
 * Created by sueyeon on 14/05/18.
 */
public class LogisticRegressionManager {

    private double[][] x;
    private int[] y;


    public void parseArffFile(){
        ArffParser arffParser = new ArffParser();
        arffParser.setResponseIndex(0); // The index of the class (which is index 0 for this data set)
        try {
            AttributeDataset mushrooms = arffParser.parse(new FileInputStream(System.getProperty("user.dir") + "/data/mushrooms.arff"));
            System.out.println(mushrooms.get(0));
            System.out.println(mushrooms.size());
            x = mushrooms.toArray(new double[mushrooms.size()][]);
            y = mushrooms.toArray(new int[mushrooms.size()]);
        } catch (Exception e) {
            System.out.println("File not found!" + e);
        }

    }

    public void trainLogisticRegression(){
        int error = 0;

        LogisticRegression.Trainer trainer = new LogisticRegression.Trainer();

        LogisticRegression logisticRegression = trainer.train(x,y);

        int predictedClass = logisticRegression.predict(x[0]); // prediction of class using trained logistic regression
        System.out.println(predictedClass);

        System.out.println(y[1]);
        System.out.println(y[0]);

        for (int i = 0; i < y.length; i++) {
            if (y[i] == logisticRegression.predict(x[i])) {
                error++;
            }
        }

        System.out.println("Logistic regression error: " + error);
    }
}

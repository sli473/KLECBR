package weka;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * CODE ADAPTED FROM https://tech.io/playgrounds/4821/machine-learning-with-java---part-2-logistic-regression
 * BY Gowgi
 * Created by sueyeon on 19/05/18.
 */
public class LogisticRegression {

    public static final String TRAINING_DATA_SET_FILENAME="/data/mushrooms.arff";
    public static final String TESTING_DATA_SET_FILENAME="/data/small-test.arff";
    public static final String PREDICTION_DATA_SET_FILENAME="/data/mushrooms.arff";

    /**
     * This method is to load the data set.
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Instances getDataSet(String fileName) throws IOException {

        int classIndex = 0;
        /** the arffloader to load the arff file */
        ArffLoader loader = new ArffLoader();

        /** load the traing data */
        InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + fileName);
        loader.setSource(inputStream);
        /**
         * we can also set the file like loader3.setFile(new
         * File("test-confused.arff"));
         */
        Instances dataSet = loader.getDataSet();
        /** set the index based on the data given in the arff files */
        dataSet.setClassIndex(classIndex);
        return dataSet;
    }

    /**
     * This method is used to process the input and return the statistics.
     *
     * @throws Exception
     */
    public static void process() throws Exception {

        Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME);
        Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME);
        /** Classifier here is Linear Regression */
        Logistic classifier = new Logistic();
        /** */
        classifier.buildClassifier(trainingDataSet);

        double[][] coefficients = classifier.coefficients();
        /**
         * train the alogorithm with the training data and evaluate the
         * algorithm with testing data
         */
        Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(classifier, testingDataSet);
        /** Print the algorithm summary */
//        System.out.println("** Linear Regression Evaluation with Datasets **");
//        System.out.println(eval.toSummaryString());
//        System.out.print(" the expression for the input data as per alogorithm is ");
        System.out.println(classifier);
//        System.out.println(classifier.getCapabilities());

        for (int i = 0; i < coefficients.length; i++) {
            System.out.println(coefficients[i][0]);
        }

        System.out.println(coefficients.length);
        System.out.println(coefficients[0].length);

        Instance predicationDataSet = getDataSet(PREDICTION_DATA_SET_FILENAME).lastInstance();
        double value = classifier.classifyInstance(predicationDataSet);
        /** Prediction Output */
        System.out.println(value);
    }

}

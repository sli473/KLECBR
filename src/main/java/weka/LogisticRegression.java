package weka;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * CODE ADAPTED FROM https://tech.io/playgrounds/4821/machine-learning-with-java---part-2-logistic-regression
 * BY Gowgi
 * Created by sueyeon on 19/05/18.
 */
public class LogisticRegression {

    private static final String TRAINING_DATA_SET_FILENAME="/data/mushrooms.arff";
    private static final String TESTING_DATA_SET_FILENAME="/data/small-test.arff";
    private static final String PREDICTION_DATA_SET_FILENAME="/data/mushrooms.arff";

    private static HashMap<String, HashMap<String, Double>> _coefficients;

    /**
     * This method is to load the data set.
     * Currently temporarily until we find out how to parse in raw data from CBR
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

    public static void sortCoefficients(Instance instance, double[][] coefficients){
        _coefficients = new HashMap<>();
        int coefficientCount = 1;

        for (int i = 1; i < instance.numAttributes(); i++) {
            HashMap<String, Double> variables = new HashMap<>();
            Attribute attribute = instance.attribute(i);
            System.out.println(attribute.name());
            System.out.println(attribute.numValues());
            if (attribute.numValues() == 2) {
                variables.put(attribute.value(1), coefficients[coefficientCount][0]);
                coefficientCount++;
            } else if (attribute.numValues() > 2) {
                for (int j = 0; j < attribute.numValues(); j++) {
                    variables.put(attribute.value(j), coefficients[coefficientCount][0]);
                    coefficientCount++;
                }
            }
            _coefficients.put(attribute.name(), variables);
        }

    }

    public static double[][] findOddsRatio(Instance caseQuery, Instance explanationQuery) {

//        double oddsRatio = coefficients[0][0];
        int coefficientCount = 1;

        System.out.println(caseQuery.attribute(2).numValues());

        for (int i = 0; i < caseQuery.numAttributes(); i++) {
            Attribute attribute = caseQuery.attribute(i);
            System.out.print(attribute + ":           " + caseQuery.stringValue(i));
            System.out.println("");

            for(int j = 0; j < attribute.numValues(); j++){

                coefficientCount++;
            }
        }

//        for (int i = 0; i < coefficients[0].length; i++) {
//            Attribute attribute = caseQuery.attribute(i);
//            System.out.print(attribute + ":           " + caseQuery.stringValue(i));
//            System.out.println("");
//
//            for(int j = 0; j < attribute.numValues(); j++){
//
//            }
//        }

        return null;
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
        System.out.println("** Linear Regression Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.print(" the odds ratio is...");
        System.out.println(classifier);

        System.out.println("SIZE: " + coefficients.length);

        sortCoefficients(trainingDataSet.firstInstance(), coefficients);
        System.out.println("FINNNNNEE ================================");

        for (int j = 0; j < coefficients.length; j++) {
            double oddsRatio = Math.exp(coefficients[j][0]);
            System.out.println(coefficients[j][0]);
        }

        Instance predicationDataSet = getDataSet(PREDICTION_DATA_SET_FILENAME).lastInstance();
        double value = classifier.classifyInstance(predicationDataSet);
        double[] instanceDistribution = classifier.distributionForInstance(predicationDataSet);

        for (int j = 0; j < instanceDistribution.length; j++) {
//            System.out.println(instanceDistribution[j]);
        }

        findOddsRatio(predicationDataSet, predicationDataSet);

        /** Prediction Output */
        System.out.println(value);
    }

}

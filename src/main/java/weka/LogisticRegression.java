package weka;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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

    /**
     * This method sorts the coefficient retrieved from the training set. it sorts the data
     * into a Hashmap of hash maps, where each attribute is sorted into the coefficients of
     * their different types.
     * @param instance
     * @param coefficients
     */
    public static void sortCoefficients(Instance instance, double[][] coefficients){
        _coefficients = new HashMap<>();
        int coefficientCount = 1;

        for (int i = 1; i < instance.numAttributes(); i++) {
            HashMap<String, Double> variables = new HashMap<>();
            Attribute attribute = instance.attribute(i);
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

    /**
     * This finds the odds ratio by comparing two cases
     * @param queryCase
     * @param explanationQuery
     * @return
     */
    public static HashMap<String, Double> findOddsRatio(Instance queryCase, Instance explanationQuery) {

        HashMap<String, Double> oddsRatios = new HashMap<>();
        double queryCoefficient;
        double explanationCoefficient;

        for (int i = 1; i < queryCase.numAttributes(); i++) {
            Attribute attribute = queryCase.attribute(i);
//            System.out.print(attribute + ":           " + queryCase.stringValue(i));
//            System.out.println("");
            HashMap<String, Double> attributeValues = _coefficients.get(attribute.name());
            try {
                queryCoefficient = attributeValues.get(queryCase.stringValue(i));
                explanationCoefficient = attributeValues.get(explanationQuery.stringValue(i));
            } catch (NullPointerException e) {
                queryCoefficient = 0;
                explanationCoefficient = 0;
            }
            double oddsRatio = Math.exp(queryCoefficient - explanationCoefficient);
            oddsRatios.put(attribute.name(), oddsRatio);
        }
        return oddsRatios;
    }

    /**
     * This method is used to process the input and return the statistics.
     *
     * @throws Exception
     */
    public static void process() throws Exception {

        Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME);
        Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME);
        Logistic classifier = new Logistic();
        classifier.buildClassifier(trainingDataSet);

        double[][] coefficients = classifier.coefficients();
        sortCoefficients(trainingDataSet.firstInstance(), coefficients);

        Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(classifier, testingDataSet);

        Instance predicationDataSet = getDataSet(PREDICTION_DATA_SET_FILENAME).lastInstance();
        Instance hi = getDataSet(TESTING_DATA_SET_FILENAME).firstInstance();
        double value = classifier.classifyInstance(predicationDataSet);

        HashMap<String, Double> oddsRatio = findOddsRatio(hi, predicationDataSet);
        System.out.println("===========OODS RATIO=========");
        System.out.println(oddsRatio);

        /** Prediction Output */
        System.out.println(value);
    }

}

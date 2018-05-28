package weka;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveUseless;

/**
 * CODE ADAPTED FROM https://tech.io/playgrounds/4821/machine-learning-with-java---part-2-logistic-regression
 * BY Gowgi
 *
 * NOTE: When changing datasets, set the class, and when doing coefficient stuff, don't forget to configure it depending on the
 * index of the class
 * Created by sueyeon on 19/05/18.
 */
public class LogisticRegression {

    private static final String TRAINING_DATA_SET_FILENAME="/data/iris/iris.arff";
    private static final String TESTING_DATA_SET_FILENAME="/data/iris/iris.arff";
    private static final String ODDS_RATIO_DATA_SET_FILENAME="/data/iris/iris.arff";

    private static HashMap<String, HashMap<String, Double>> _coefficientsCategorical;
    private static HashMap<String, Double> _coefficientsNumeric;

    /**
     * This method is to load the data set.
     * Currently temporarily until we find out how to parse in raw data from CBR
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Instances getDataSet(String fileName) throws IOException {

        int classIndex = 4;
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
     * @param instances
     * @param coefficients
     */
    public static void sortCoefficientsCategorical(Instances instances, double[][] coefficients){
        _coefficientsCategorical = new HashMap<>();
        ArrayList<Attribute> attributeList = obtainCoefficientAttributes(instances);
        int coefficientCount = 1;
        for (int i = 1; i < attributeList.size(); i++) {
            HashMap<String, Double> variables = new HashMap<>();
            Attribute attribute = attributeList.get(i);
            if (attribute.numValues() == 2) {
                variables.put(attribute.value(1), coefficients[coefficientCount][0]);
                coefficientCount++;
            } else if (attribute.numValues() > 2) {
                for (int j = 0; j < attribute.numValues(); j++) {
                    variables.put(attribute.value(j), coefficients[coefficientCount][0]);
                    coefficientCount++;
                }
            }
            _coefficientsCategorical.put(attribute.name(), variables);
        }

    }

    public static void sortCoefficientsNumeric(Instances instances, double[][] coefficients){
        _coefficientsNumeric = new HashMap<>();
        ArrayList<Attribute> attributeList = obtainCoefficientAttributes(instances);
        int coefficientCount = 1;
        for (int i = 0; i < attributeList.size() - 1; i++) {
            Attribute attribute = attributeList.get(i);
            _coefficientsNumeric.put(attribute.name(), coefficients[coefficientCount][0]);
            coefficientCount++;
        }

    }

    /**
     * This finds the odds ratio by comparing two cases
     * @param queryCase
     * @param explanationQuery
     * @return
     */
    public static HashMap<String, Double> findOddsRatioCategorical(Instance queryCase, Instance explanationQuery) {

        HashMap<String, Double> oddsRatios = new HashMap<>();
        double queryCoefficient;
        double explanationCoefficient;

        for (int i = 1; i < queryCase.numAttributes(); i++) {
            Attribute attribute = queryCase.attribute(i);
            HashMap<String, Double> attributeValues = _coefficientsCategorical.get(attribute.name());
            try {
                queryCoefficient = attributeValues.get(queryCase.stringValue(i));
            } catch (NullPointerException e) {
                queryCoefficient = 0;
            }
            try {
                explanationCoefficient = attributeValues.get(explanationQuery.stringValue(i));
            } catch (NullPointerException e) {
                explanationCoefficient = 0;
            }
            double oddsRatio = Math.exp(queryCoefficient - explanationCoefficient);
            oddsRatios.put(attribute.name(), oddsRatio);
        }
        return oddsRatios;
    }

    public static HashMap<String, Double> findOddsRatioNumeric(Instance queryCase, Instance explanationQuery) {

        HashMap<String, Double> oddsRatios = new HashMap<>();
        double queryCoefficient;
        double explanationCoefficient;

        for (int i = 1; i < queryCase.numAttributes(); i++) {
            Attribute attribute = queryCase.attribute(i);
            try {
                queryCoefficient = _coefficientsNumeric.get(attribute);
            } catch (NullPointerException e) {
                queryCoefficient = 0;
            }
            try {
                explanationCoefficient = _coefficientsNumeric.get(attribute);
            } catch (NullPointerException e) {
                explanationCoefficient = 0;
            }
            double oddsRatio = Math.exp(queryCoefficient - explanationCoefficient);
            oddsRatios.put(attribute.name(), oddsRatio);
        }
        return oddsRatios;
    }

    public static ArrayList<Attribute> obtainCoefficientAttributes(Instances instances){
        ArrayList<Attribute> result = new ArrayList();
        Filter filter = new RemoveUseless();
        try {
            filter.setInputFormat(instances);
            Instances filtered = Filter.useFilter(instances, filter);
            Instance instance = filtered.firstInstance();
            for (int j = 0; j < instance.numAttributes(); j++) {
                result.add(instance.attribute(j));
            }
        } catch (Exception e) {
            System.out.println("Could not get coefficient instances");
        }
        return result;
    }

    /**
     * This method is used to process the input and return the statistics.
     *
     * @throws Exception
     */
    public static void processCategorical() throws Exception {

        ArrayList<Instance> fortioriCases = new ArrayList<>();
        Instance predicationForCase = getDataSet(ODDS_RATIO_DATA_SET_FILENAME).get(1);
        Instance predicationAgainstCase = getDataSet(ODDS_RATIO_DATA_SET_FILENAME).lastInstance();
        fortioriCases.add(predicationForCase);
        fortioriCases.add(predicationAgainstCase);
        Instance queryCase = getDataSet(ODDS_RATIO_DATA_SET_FILENAME).firstInstance();

        Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME);
        Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME);
        Logistic classifier = new Logistic();
        classifier.buildClassifier(trainingDataSet);

        System.out.println(classifier);

        double[][] coefficients = classifier.coefficients();

        obtainCoefficientAttributes(trainingDataSet);

        sortCoefficientsCategorical(trainingDataSet, coefficients);

//        double value = classifier.classifyInstance(predicationCase);

        HashMap<String, Double> oddsRatioFor = findOddsRatioCategorical(queryCase, predicationForCase);
        System.out.println("===========OODS RATIO FOR=========");
        System.out.println("");
        System.out.println(oddsRatioFor);

        HashMap<String, Double> oddsRatioAgainst = findOddsRatioCategorical(queryCase, predicationAgainstCase);
        System.out.println("===========OODS RATIO AGAINST=========");
        System.out.println(oddsRatioAgainst);

//        /** Prediction Output */
//        System.out.println(value);
    }

    public static void processNumeric() throws Exception {

        ArrayList<Instance> fortioriCases = new ArrayList<>();
        Instance predicationForCase = getDataSet(ODDS_RATIO_DATA_SET_FILENAME).get(1);
        Instance predicationAgainstCase = getDataSet(ODDS_RATIO_DATA_SET_FILENAME).lastInstance();
        fortioriCases.add(predicationForCase);
        fortioriCases.add(predicationAgainstCase);
        Instance queryCase = getDataSet(ODDS_RATIO_DATA_SET_FILENAME).firstInstance();

        Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME);
        Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME);
        Logistic classifier = new Logistic();
        classifier.buildClassifier(trainingDataSet);

        System.out.println(classifier);

        double[][] coefficients = classifier.coefficients();

        obtainCoefficientAttributes(trainingDataSet);

        sortCoefficientsNumeric(trainingDataSet, coefficients);

        HashMap<String, Double> oddsRatioFor = findOddsRatioNumeric(queryCase, predicationForCase);
        System.out.println("===========OODS RATIO FOR=========");
        System.out.println("");
        System.out.println(oddsRatioFor);

        HashMap<String, Double> oddsRatioAgainst = findOddsRatioNumeric(queryCase, predicationAgainstCase);
        System.out.println("===========OODS RATIO AGAINST=========");
        System.out.println(oddsRatioAgainst);

//        /** Prediction Output */
//        System.out.println(value);
    }

}

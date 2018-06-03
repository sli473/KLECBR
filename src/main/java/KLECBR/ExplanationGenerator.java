package KLECBR;

import BreastCancer.CancerSolution;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.util.FileIO;
import jcolibri.cbrcore.CBRCase;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.net.URL;
import java.io.BufferedWriter;

public class ExplanationGenerator {

    String outputFile;
    String solution;
    KLECaseComponent queryCase;
    CBRCase fortioriCase;
    HashMap<Double, String> PCAWeightings = new HashMap<>();
    HashMap<String, Double> oddsRatios = new HashMap<>();
    List<Double> numericalWeights = new ArrayList<>();
    private static int NUMBER_OF_SIGNIFICANT_VARIABLES = 3;

    public ExplanationGenerator(String outputAddress, String solution, KLECaseComponent queryCase, CBRCase fortioriCase){
        outputFile = outputAddress;
        this.solution = solution;
        this.queryCase = queryCase;
        this.fortioriCase = fortioriCase;
        setPCAWeightings("Cancer");
    }

    public void generateExplanation(
            HashMap<String, Double> ORfor, HashMap<String, Double> ORagainst
    ) {
        oddsRatios = ORfor;
        StringBuilder explanation;
        URL url = FileIO.findFile(outputFile);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(url.getFile()));
            bw.write("Query Case:");
            bw.newLine();
            bw.write("Features:");
            bw.newLine();

            KLECaseComponent fortioriComponent = (KLECaseComponent)fortioriCase.getDescription();
            HashMap<String, String> fortioriInfo = fortioriComponent.getHashMap();
            HashMap<String, String> queryInfo = queryCase.getHashMap();
            int maxLength = maxLength(queryInfo.keySet());

            for(String q: queryInfo.keySet()) {
                bw.write(q + generateTabs(q, maxLength) + queryInfo.get(q));
                bw.newLine();
            }

            bw.write("Classification for Case: " + solution);
            bw.newLine();
            bw.newLine();
            bw.write("Explanation Case:");
            bw.newLine();
            bw.write("Features:");
            bw.newLine();

            for(String w: fortioriInfo.keySet()) {
                bw.write(w + generateTabs(w, maxLength) + fortioriInfo.get(w));
                bw.newLine();
            }

            CancerSolution fortioriSolution = (CancerSolution) fortioriCase.getSolution();
            String val = fortioriSolution.get_classification() == 2 ? "benign" : "malignant";

            bw.write("Classification for Case: " + val);
            bw.newLine();

            bw.write("The query case was compared to a pre-determined classification (explanation case) where both were determined to be " + val + ".");

            // Initialize local variables used to store the features that are attempting to be explained
            HashMap<String, List<String>> classification = new HashMap<>();
            List<String> higherVariables = new ArrayList<>();
            List<String> lowerVariables = new ArrayList<>();
            classification.put("higher", higherVariables);
            classification.put("lower", lowerVariables);

            // Putting the keys into higher or lower categories
            for(String s: ORfor.keySet()) {
                double oddRatio = ORfor.get(s);
                String key = replaceDash(s);

                // Classifying variables to chuck into higher or lower categories
                if(oddRatio > 1) {
                    String relation = Integer.parseInt(queryInfo.get(key)) > Integer.parseInt(fortioriInfo.get(key)) ? "higher" : "lower";
                    List<String> variableList = classification.get(relation);
                    variableList.add(key);
                }
            }

            higherVariables = classification.get("higher");
            lowerVariables = classification.get("lower");
            bw.newLine();

            // CREATING SIGNIFICANCE EXPLANATIONS FOR PCA
            for (int i = 0; i < NUMBER_OF_SIGNIFICANT_VARIABLES; i++) {
                explanation = new StringBuilder();
                String weightedVariable = PCAWeightings.get(numericalWeights.get(i));

                if (higherVariables.contains(weightedVariable)) {
                    int index = higherVariables.indexOf(weightedVariable);
                    String exp = createPCAExplanation("higher", val, higherVariables.get(index), i + 1);
                    explanation.append(exp);
                    higherVariables.remove(weightedVariable);

                    bw.write(explanation.toString());
                    bw.newLine();
                } else if (lowerVariables.contains(weightedVariable)) {
                    int index = lowerVariables.indexOf(weightedVariable);
                    String exp = createPCAExplanation("lower", val, lowerVariables.get(index), i + 1);
                    explanation.append(exp);
                    lowerVariables.remove(weightedVariable);

                    bw.write(explanation.toString());
                    bw.newLine();
                }
            }

            bw.write("Other variables such as: \n");
            explanation = new StringBuilder();
            // CREATING LIST OF EXPLANATIONS
            if (higherVariables.size() > 0) {
                String higherVarExplanation = createMultipleExplanation("higher", val, higherVariables);
                explanation.append(higherVarExplanation);
            }

            if (lowerVariables.size() > 0) {
                if (higherVariables.size() > 0) {
                    explanation.append("Furthermore, ");
                }
                String lowerVarExplanation = createMultipleExplanation("lower", val, lowerVariables);
                explanation.append(lowerVarExplanation);
            }

            bw.write(explanation.toString());
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int maxLength(Set<String> keySet) {

        int length = 0;

        for(String key: keySet) {
            if(key.length() > length) {
                length = key.length();
            }
        }
        return length;
    }

    public String generateTabs(String current, int maxLength) {
        int differenceInLength = maxLength - current.length();
        int numberOfTabs = differenceInLength / 4;

        StringBuilder sb = new StringBuilder();
        sb.append("\t\t");

        for(int i = 0; i < numberOfTabs; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }

    public String replaceDash(String current) {
        return current.replaceAll("-", " ");
    }

    private String createMultipleExplanation(String relation, String classification, List<String> variables) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < variables.size(); i++) {
            double oddsRatio = 0;
            for(String s: oddsRatios.keySet()) {
                double newOddRatio = oddsRatios.get(s);
                String key = replaceDash(s);
                if (key.equals(variables.get(i))) {
                    oddsRatio = newOddRatio;
                }
            }

            int multiplier = (int)Math.ceil(oddsRatio);
            if (multiplier != 1) {
                sb.append("+ " + variables.get(i) + " being " + relation + " on the query compared to the explanation means it is " + multiplier + " times more likely to be " + classification + ". \n");
            }
        }

        return sb.toString();
    }

    private String createExplanation(String relation, String classification, List<String> variables) {
        StringBuilder sb = new StringBuilder();
        sb.append("Other variables such as ");

        if (variables.size() == 1) {
            sb.append(variables.get(0));
        } else if (variables.size() == 2) {
            sb.append(variables.get(0) + " and " + variables.get(1));
        } else {
            for (int i = 0; i < variables.size(); i++) {
                if (i == variables.size() - 1) {
                    sb.append("and " + variables.get(i));
                } else {
                    sb.append(variables.get(i) + ", ");
                }
            }
        }

        sb.append(" being " + relation + " on the query compared to the explanation means it is more likely to be " + classification + ". ");
        return sb.toString();
    }

    private String createPCAExplanation(String relation, String classification, String variable, int number) {
        StringBuilder sb = new StringBuilder();
        String rank = "";
        switch(number) {
            case 1:
                rank = " ";
                break;
            case 2:
                rank = " second ";
                break;
            case 3:
                rank = " third ";
                break;
        }

        double oddsRatio = 0;
        for(String s: oddsRatios.keySet()) {
            double newOddRatio = oddsRatios.get(s);
            String key = replaceDash(s);
            if (key.equals(variable)) {
                oddsRatio = newOddRatio;
            }
        }
        int multiplier = (int)Math.ceil(oddsRatio);

        sb.append(variable + " was identified as the" + rank + "most significant variable in classifying " + classification);
        sb.append(". It was found that a " + relation + " value of " + variable + " in the query individual leads to a " + multiplier + " times higher likelihood of being " + classification + " than the explanation individual.");

        return sb.toString();
    }

    private void setPCAWeightings(String dataset) {
        if (dataset.equals("Cancer")) {
            PCAWeightings.put(0.318596, "Clump Thickness");
            PCAWeightings.put(0.4051296, "Uniformity Of Cell Size");
            PCAWeightings.put(0.3994052, "Uniformity Of Cell Shape");
            PCAWeightings.put(0.3489156, "Marginal Adhesion");
            PCAWeightings.put(0.3593192, "Single Epithelial Cell Size");
            PCAWeightings.put(0.363963,"Bland Chromatin");
            PCAWeightings.put(0.3591949, "Normal Nucleoli");
            PCAWeightings.put(0.2499993, "Mitoses");

            numericalWeights.add(0.318596);
            numericalWeights.add(0.4051296);
            numericalWeights.add(0.3994052);
            numericalWeights.add(0.3489156);
            numericalWeights.add(0.3593192);
            numericalWeights.add(0.363963);
            numericalWeights.add(0.3591949);
            numericalWeights.add(0.2499993);

            sortPCAWeightings();
        }
    }

    private void sortPCAWeightings() {
        Collections.sort(numericalWeights);
        Collections.reverse(numericalWeights);
    }
}

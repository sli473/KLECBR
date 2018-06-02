package KLECBR;

import BreastCancer.CancerSolution;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.util.FileIO;
import jcolibri.cbrcore.CBRCase;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.net.URL;
import java.io.BufferedWriter;

public class ExplanationGenerator {

    String outputFile;
    String solution;
    KLECaseComponent queryCase;
    CBRCase fortioriCase;

    public ExplanationGenerator(String outputAddress, String solution, KLECaseComponent queryCase, CBRCase fortioriCase){
        outputFile = outputAddress;
        this.solution = solution;
        this.queryCase = queryCase;
        this.fortioriCase = fortioriCase;
    }

    public void generateExplanation(
            HashMap<String, Double> ORfor, HashMap<String, Double> ORagainst
    ) {
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

            explanation = new StringBuilder();
            higherVariables = classification.get("higher");
            lowerVariables = classification.get("lower");

            if (higherVariables.size() > 0) {
                String higherVarExplanation = createExplanation("higher", val, higherVariables);
                explanation.append(higherVarExplanation);
            }

            if (lowerVariables.size() > 0) {
                if (higherVariables.size() > 0) {
                    explanation.append("Furthermore, ");
                }
                String lowerVarExplanation = createExplanation("lower", val, lowerVariables);
                explanation.append(lowerVarExplanation);
            }

            bw.newLine();
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

    private String createExplanation(String relation, String classification, List<String> variables) {
        StringBuilder sb = new StringBuilder();

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
}

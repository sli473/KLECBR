package KLECBR;

import BreastCancer.CancerSolution;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.util.FileIO;
import jcolibri.cbrcore.CBRCase;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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


            for(String s: ORfor.keySet()) {
                explanation = new StringBuilder();
                double oddRatio = ORfor.get(s);

                String key = replaceDash(s);

                if(oddRatio > 1) {
                    bw.newLine();
                    String relation = Integer.parseInt(queryInfo.get(key)) > Integer.parseInt(fortioriInfo.get(key)) ? "higher" : "lower";
                    if(relation.equals("higher")) {
                        bw.write("A higher "+key+" Value supports the classification of:" +solution);
                    } else if(relation.equals("lower")) {
                        bw.write("A lower " + key + " Value supports the classification of:" + solution);
                    }
                    explanation.append(key + " is "+ oddRatio +" times more likely to support the same classification as the explanation. ");
                    explanation.append("Because the query case '");
                    explanation.append(key);
                    explanation.append("' value is ");
                    explanation.append(relation + " than the explanation case.");

                    bw.newLine();
                    bw.write(explanation.toString());
                }
            }

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
}

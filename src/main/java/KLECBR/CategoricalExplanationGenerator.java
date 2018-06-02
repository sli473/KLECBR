package KLECBR;

import Mushroom.MushroomSolution;
import jcolibri.cbrcore.CBRCase;
import jcolibri.util.FileIO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class CategoricalExplanationGenerator extends ExplanationGenerator{
    public CategoricalExplanationGenerator(String outputAddress, String solution, KLECaseComponent queryCase, CBRCase fortioriCase) {
        super(outputAddress, solution, queryCase, fortioriCase);
    }

    @Override
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
            bw.newLine();
            bw.write("Explanation Case:");
            bw.newLine();
            bw.write("Features:");
            bw.newLine();

            for(String w: fortioriInfo.keySet()) {
                bw.write(w + generateTabs(w, maxLength) + fortioriInfo.get(w));
                bw.newLine();
            }

            MushroomSolution fortioriSolution = (MushroomSolution) fortioriCase.getSolution();

            String val = fortioriSolution.is_isPoisonous().equals("e") ? "edible" : "poisonous";

            bw.write("Classification for Case: " + val);
            bw.newLine();


            for(String s: ORfor.keySet()) {
                explanation = new StringBuilder();
                double oddRatio = ORfor.get(s);

                String key = replaceDash(s);

                if(oddRatio > 1) {
                    bw.newLine();
                    explanation.append(key + " with a "+ queryInfo.get(key) +" value is "+oddRatio+" times more likely to support the classification than ");
                    explanation.append(fortioriInfo.get(key));
                    bw.write(explanation.toString());
                }
            }

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

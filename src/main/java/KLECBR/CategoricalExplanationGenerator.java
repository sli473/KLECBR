package KLECBR;

import Mushroom.MushroomSolution;
import jcolibri.cbrcore.CBRCase;
import jcolibri.util.FileIO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CategoricalExplanationGenerator extends ExplanationGenerator{

    HashMap<Double, String> PCAWeightings = new HashMap<>();
    HashMap<String, Double> oddsRatios = new HashMap<>();
    List<Double> numericalWeights = new ArrayList<>();

    public CategoricalExplanationGenerator(String outputAddress, String solution, KLECaseComponent queryCase, CBRCase fortioriCase) {
        super(outputAddress, solution, queryCase, fortioriCase);
        setPCAWeightings("Mushroom");
    }

    @Override
    public void generateExplanation(
            HashMap<String, Double> ORfor, HashMap<String, Double> ORagainst
    ) {
        StringBuilder explanation;
        URL url = FileIO.findFile(outputFile);
        oddsRatios = ORfor;
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

            List<String> variables = new ArrayList<>();

            for(String s: ORfor.keySet()) {
                explanation = new StringBuilder();
                double oddRatio = ORfor.get(s);

                String key = replaceDash(s);

                if(oddRatio > 1) {
                    bw.newLine();
                    variables.add(key);
                    explanation.append(key + " with a "+ queryInfo.get(key) +" value is "+oddRatio+" times more likely to support the classification than ");
                    explanation.append(fortioriInfo.get(key));
                    bw.write(explanation.toString());
                }
            }

            // CREATING SIGNIFICANCE EXPLANATIONS FOR PCA
            for (int i = 0; i < 3; i++) {
                explanation = new StringBuilder();
                String weightedVariable = PCAWeightings.get(numericalWeights.get(i));

                if (variables.contains(weightedVariable)) {
                    int index = variables.indexOf(weightedVariable);
                    String exp = createPCAExplanation("", val, variables.get(index), i + 1);
                    explanation.append(exp);
                    variables.remove(weightedVariable);

                    bw.write(explanation.toString());
                    bw.newLine();
                }
            }

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setPCAWeightings(String dataset) {
        if (dataset.equals("Mushroom")) {
            PCAWeightings.put(0.037329637, "Cap Shape");
            PCAWeightings.put(0.090467869, "Cap Surface");
            PCAWeightings.put(0.157081732, "Cap Color");
            PCAWeightings.put(0.428337712, "Bruises");
            PCAWeightings.put(0.201708930, "Odor");
            PCAWeightings.put(0.004086984,"Gill Attachment");
            PCAWeightings.put(0.154700030, "Gill Spacing");
            PCAWeightings.put(0.074343986, "Gill Size");
            PCAWeightings.put(0.314157001 ,"Gill Color");
            PCAWeightings.put(0.182076707, "Stalk Shape");
            PCAWeightings.put(0.052566595, "Stalk Root");
            PCAWeightings.put(0.363895079, "Stalk Surface Above Ring");
            PCAWeightings.put(0.350970244, "Stalk Surface Below Ring");
            PCAWeightings.put(0.136846306, "Stalk Color Above Ring");
            PCAWeightings.put(0.144957503 ,"Stalk Color Below Ring");
            PCAWeightings.put(0.017748065, "Veil Color");
            PCAWeightings.put(0.084506338, "Ring Number");
            PCAWeightings.put(0.418242387, "Ring Type");
            PCAWeightings.put(0.267006190, "Spore Print Color");
            PCAWeightings.put(0.117791917, "Population");
            PCAWeightings.put(0.120882160, "Habitat");

            numericalWeights.add(0.037329637);
            numericalWeights.add(0.090467869);
            numericalWeights.add(0.157081732);
            numericalWeights.add(0.428337712);
            numericalWeights.add(0.201708930);
            numericalWeights.add(0.004086984);
            numericalWeights.add(0.154700030);
            numericalWeights.add(0.074343986);
            numericalWeights.add(0.314157001);
            numericalWeights.add(0.182076707);
            numericalWeights.add(0.052566595);
            numericalWeights.add(0.363895079);
            numericalWeights.add(0.350970244);
            numericalWeights.add(0.136846306);
            numericalWeights.add(0.144957503);
            numericalWeights.add(0.017748065);
            numericalWeights.add(0.084506338);
            numericalWeights.add(0.418242387);
            numericalWeights.add(0.267006190);
            numericalWeights.add(0.117791917);
            numericalWeights.add(0.120882160);


            sortPCAWeightings();
        }
    }

    private void sortPCAWeightings() {
        Collections.sort(numericalWeights);
        Collections.reverse(numericalWeights);
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
}

package output;

import jcolibri.cbrcore.CBRCase;
import java.util.HashMap;

/**
 * Created by sueyeon on 26/05/18.
 */
public class TextTemplate {
    private static  HashMap<String, Double> _oddsRatioFor;
    private static  HashMap<String, Double> _oddsRatioAgainst;
    private static CBRCase _queryCase;

    TextTemplate(HashMap<String, Double> oddsRatioFor, HashMap<String, Double> oddsRatioAgainst, CBRCase queryCase) {
        HashMap<String, Double> _oddsRatioFor = oddsRatioFor;
        HashMap<String, Double> _oddsRatioAgainst = oddsRatioAgainst;
        CBRCase _queryCase = queryCase;
    }

    private void interpretValues(){

    }

    public void PrintExplanation(){
        System.out.println("hello");

    }
}

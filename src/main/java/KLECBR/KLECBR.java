package KLECBR;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.exception.ExecutionException;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.cbrcore.CBRCase;
import java.util.ArrayList;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.method.retrieve.RetrievalResult;

import java.util.Collection;


public interface KLECBR extends StandardCBRApplication {

    void configure() throws jcolibri.exception.ExecutionException;

    CBRCaseBase preCycle() throws ExecutionException;

    void cycle(CBRQuery cbrQuery) throws ExecutionException;

    void postCycle() throws ExecutionException;

    void setUpNNConfig(NNConfig simConfig);

    Collection<CBRCase> createLocalCaseBase(Collection<RetrievalResult> cases, int k);

    ArrayList<String> getArffCases(Collection<CBRCase> localCaseBase);

    void printCaseBase();

    CBRQuery createQuery();

    void fortioriCase(ArrayList<CBRCase> caseBase, CBRCase cbrCase);

    CaseComponent calculateSolution(Collection<RetrievalResult> cases, int k);

    CaseComponent getSolution();

}

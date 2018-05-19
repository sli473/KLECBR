import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.ExecutionException;

public class KLECBR implements StandardCBRApplication {
    @Override
    public void configure() throws ExecutionException {

    }

    @Override
    public CBRCaseBase preCycle() throws ExecutionException {
        return null;
    }

    @Override
    public void cycle(CBRQuery cbrQuery) throws ExecutionException {

    }

    @Override
    public void postCycle() throws ExecutionException {

    }
}

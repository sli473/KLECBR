package BreastCancer;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class CancerSolution implements CaseComponent {

    int _caseId;
    int _classification;

    public CancerSolution() {
    }

    public int get_caseId() {
        return _caseId;
    }

    public void set_caseId(int _caseId) {
        this._caseId = _caseId;
    }

    public int get_classification() {
        return _classification;
    }

    public void set_classification(int _classification) {
        this._classification = _classification;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("_caseId", this.getClass());
    }
}

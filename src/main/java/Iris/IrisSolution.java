package Iris;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class IrisSolution implements CaseComponent {

    private int _caseId;
    private String _species;

    public IrisSolution() {
    }

    public int get_caseId() {
        return _caseId;
    }

    public void set_caseId(int _caseId) {
        this._caseId = _caseId;
    }

    public String get_species() {
        return _species;
    }

    public void set_species(String _species) {
        this._species = _species;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("_caseId", this.getClass());
    }
}

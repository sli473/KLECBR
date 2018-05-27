package Iris;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class IrisDescription implements CaseComponent {

    int _caseId;
    Double _sepalLengthCm;
    Double _sepalWidthCm;
    Double _petalLengthCm;
    Double _petalWidthCm;

    public IrisDescription() {}

    public int get_caseId() {
        return _caseId;
    }

    public void set_caseId(int _caseId) {
        this._caseId = _caseId;
    }

    public Double get_sepalLengthCm() {
        return _sepalLengthCm;
    }

    public void set_sepalLengthCm(Double _sepalLengthCm) {
        this._sepalLengthCm = _sepalLengthCm;
    }

    public Double get_sepalWidthCm() {
        return _sepalWidthCm;
    }

    public void set_sepalWidthCm(Double _sepalWidthCm) {
        this._sepalWidthCm = _sepalWidthCm;
    }

    public Double get_petalLengthCm() {
        return _petalLengthCm;
    }

    public void set_petalLengthCm(Double _petalLengthCm) {
        this._petalLengthCm = _petalLengthCm;
    }

    public Double get_petalWidthCm() {
        return _petalWidthCm;
    }

    public void set_petalWidthCm(Double _petalWidthCm) {
        this._petalWidthCm = _petalWidthCm;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("_caseId", this.getClass());
    }
}

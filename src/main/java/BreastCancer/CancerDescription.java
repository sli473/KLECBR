package BreastCancer;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class CancerDescription implements CaseComponent{

    int _caseId;
    int _clumpThickness;
    int _uniformityOfCellSize;
    int _uniformityOfCellShape;
    int _marginalAdhesion;
    int _singleEpithelialCellSize;
    int _bareNuclei;
    int _blandChromatin;
    int _normalNucleoli;
    int _mitoses;

    public CancerDescription() {}

    public int get_caseId() {
        return _caseId;
    }

    public void set_caseId(int _caseId) {
        this._caseId = _caseId;
    }

    public int get_clumpThickness() {
        return _clumpThickness;
    }

    public void set_clumpThickness(int _clumpThickness) {
        this._clumpThickness = _clumpThickness;
    }

    public int get_uniformityOfCellSize() {
        return _uniformityOfCellSize;
    }

    public void set_uniformityOfCellSize(int _uniformityOfCellSize) {
        this._uniformityOfCellSize = _uniformityOfCellSize;
    }

    public int get_uniformityOfCellShape() {
        return _uniformityOfCellShape;
    }

    public void set_uniformityOfCellShape(int _uniformityOfCellShape) {
        this._uniformityOfCellShape = _uniformityOfCellShape;
    }

    public int get_marginalAdhesion() {
        return _marginalAdhesion;
    }

    public void set_marginalAdhesion(int _marginalAdhesion) {
        this._marginalAdhesion = _marginalAdhesion;
    }

    public int get_singleEpithelialCellSize() {
        return _singleEpithelialCellSize;
    }

    public void set_singleEpithelialCellSize(int _singleEpithelialCellSize) {
        this._singleEpithelialCellSize = _singleEpithelialCellSize;
    }

    public int get_bareNuclei() {
        return _bareNuclei;
    }

    public void set_bareNuclei(int _bareNuclei) {
        this._bareNuclei = _bareNuclei;
    }

    public int get_blandChromatin() {
        return _blandChromatin;
    }

    public void set_blandChromatin(int _blandChromatin) {
        this._blandChromatin = _blandChromatin;
    }

    public int get_normalNucleoli() {
        return _normalNucleoli;
    }

    public void set_normalNucleoli(int _normalNucleoli) {
        this._normalNucleoli = _normalNucleoli;
    }

    public int get_mitoses() {
        return _mitoses;
    }

    public void set_mitoses(int _mitoses) {
        this._mitoses = _mitoses;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("_caseId", this.getClass());
    }
}

import jcolibri.cbrcore.Attribute;

public class MushroomDescription implements jcolibri.cbrcore.CaseComponent {

    String _caseId;
    String _classification;
    String _capSurface;
    String _capColour;
    String _bruises;
    String _odor;
    String _gillAttachment;
    String _gillSpacing;
    String _gillSize;
    String _gillColor;
    String _stalkShape;
    String _stalkRoot;
    String _stalkSurfaceAboveRing;
    String _stalkColourAboveRing;
    String _stalkColourBelowRing;
    String _veilType;
    String _veilColour;
    String _ringNumber;
    String _ringType;
    String _sporePrintColor;
    String _population;
    String _habitat;

    public String getCaseId() { return _caseId; }
    public void setCaseId(String caseId) { this._caseId = caseId; }
    public String getClassification() { return _classification; }
    public void setClassification(String classification) {_classification = classification;}
    public String getCapSurface() { return _capSurface; }
    public void setCapSurface(String capSurface) { _capSurface = capSurface; }
    public String getCapColour() { return _capColour; }
    public void setCapColour(String capColour) { _capColour = capColour; }
    public String getBruises() { return _bruises; }
    public void setBruises(String bruises) { _bruises = bruises; }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("caseId", this.getClass());
    }
}

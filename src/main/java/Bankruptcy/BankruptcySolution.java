package Bankruptcy;

import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Attribute;

import java.util.HashMap;

public class BankruptcySolution implements CaseComponent {

    private String caseId;
    private String classification;

    public BankruptcySolution() {}

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("caseId", this.getClass());
    }
}

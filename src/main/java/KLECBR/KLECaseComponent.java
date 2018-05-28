package KLECBR;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

import java.util.HashMap;

public interface KLECaseComponent extends CaseComponent{

    Attribute getIdAttribute();

    HashMap<String, String> getHashMap();
}

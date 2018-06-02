package Mushroom;

import KLECBR.KLECaseComponent;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

import java.util.HashMap;

public class MushroomDescription implements KLECaseComponent {

    int _caseId;
    String _capShape;
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
    String _stalkSurfaceBelowRing;
    String _stalkColourAboveRing;
    String _stalkColourBelowRing;
    String _veilType;
    String _veilColour;
    String _ringNumber;
    String _ringType;
    String _sporePrintColor;
    String _population;
    String _habitat;

    public MushroomDescription() {}

    public int get_caseId() {
        return _caseId;
    }

    public void set_caseId(int _caseId) {
        this._caseId = _caseId;
    }

    public String get_capShape() {
        return _capShape;
    }

    public void set_capShape(String _capShape) {
        this._capShape = _capShape;
    }

    public String get_capSurface() {
        return _capSurface;
    }

    public void set_capSurface(String _capSurface) {
        this._capSurface = _capSurface;
    }

    public String get_capColour() {
        return _capColour;
    }

    public void set_capColour(String _capColour) {
        this._capColour = _capColour;
    }

    public String get_bruises() {
        return _bruises;
    }

    public void set_bruises(String _bruises) {
        this._bruises = _bruises;
    }

    public String get_odor() {
        return _odor;
    }

    public void set_odor(String _odor) {
        this._odor = _odor;
    }

    public String get_gillAttachment() {
        return _gillAttachment;
    }

    public void set_gillAttachment(String _gillAttachment) {
        this._gillAttachment = _gillAttachment;
    }

    public String get_gillSpacing() {
        return _gillSpacing;
    }

    public void set_gillSpacing(String _gillSpacing) {
        this._gillSpacing = _gillSpacing;
    }

    public String get_gillSize() {
        return _gillSize;
    }

    public void set_gillSize(String _gillSize) {
        this._gillSize = _gillSize;
    }

    public String get_gillColor() {
        return _gillColor;
    }

    public void set_gillColor(String _gillColor) {
        this._gillColor = _gillColor;
    }

    public String get_stalkShape() {
        return _stalkShape;
    }

    public void set_stalkShape(String _stalkShape) {
        this._stalkShape = _stalkShape;
    }

    public String get_stalkRoot() {
        return _stalkRoot;
    }

    public void set_stalkRoot(String _stalkRoot) {
        this._stalkRoot = _stalkRoot;
    }

    public String get_stalkSurfaceAboveRing() {
        return _stalkSurfaceAboveRing;
    }

    public void set_stalkSurfaceAboveRing(String _stalkSurfaceAboveRing) {
        this._stalkSurfaceAboveRing = _stalkSurfaceAboveRing;
    }

    public String get_stalkSurfaceBelowRing() {
        return _stalkSurfaceBelowRing;
    }

    public void set_stalkSurfaceBelowRing(String _stalkSurfaceBelowRing) {
        this._stalkSurfaceBelowRing = _stalkSurfaceBelowRing;
    }

    public String get_stalkColourAboveRing() {
        return _stalkColourAboveRing;
    }

    public void set_stalkColourAboveRing(String _stalkColourAboveRing) {
        this._stalkColourAboveRing = _stalkColourAboveRing;
    }

    public String get_stalkColourBelowRing() {
        return _stalkColourBelowRing;
    }

    public void set_stalkColourBelowRing(String _stalkColourBelowRing) {
        this._stalkColourBelowRing = _stalkColourBelowRing;
    }

    public String get_veilType() {
        return _veilType;
    }

    public void set_veilType(String _veilType) {
        this._veilType = _veilType;
    }

    public String get_veilColour() {
        return _veilColour;
    }

    public void set_veilColour(String _veilColour) {
        this._veilColour = _veilColour;
    }

    public String get_ringNumber() {
        return _ringNumber;
    }

    public void set_ringNumber(String _ringNumber) {
        this._ringNumber = _ringNumber;
    }

    public String get_ringType() {
        return _ringType;
    }

    public void set_ringType(String _ringType) {
        this._ringType = _ringType;
    }

    public String get_sporePrintColor() {
        return _sporePrintColor;
    }

    public void set_sporePrintColor(String _sporePrintColor) {
        this._sporePrintColor = _sporePrintColor;
    }

    public String get_population() {
        return _population;
    }

    public void set_population(String _population) {
        this._population = _population;
    }

    public String get_habitat() {
        return _habitat;
    }

    public void set_habitat(String _habitat) {
        this._habitat = _habitat;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("_caseId", this.getClass());
    }

    @Override
    public HashMap<String, String> getHashMap() {
        HashMap<String, String> hm = new HashMap<>();
        String capShape = "";
        if(_capShape.equals("b")) {
            capShape = "bell";
        } else if (_capShape.equals("c")) {
            capShape = "conical";
        } else if (_capShape.equals("x")) {
            capShape = "convex";
        } else if (_capShape.equals("f")) {
            capShape = "flat";
        } else if (_capShape.equals("k")) {
            capShape = "knobbed";
        } else {
            capShape = "sunken";
        }

        hm.put("Cap Shape", capShape);

        if(_stalkSurfaceAboveRing.equals("f")) {
            capShape = "fibrous";
        } else if (_stalkSurfaceAboveRing.equals("y")) {
            capShape = "scaly";
        } else if (_stalkSurfaceAboveRing.equals("k")) {
            capShape = "silky";
        } else {
            capShape = "smooth";
        }

        hm.put("Stalk Surface Above Ring", capShape);

        if(_stalkSurfaceBelowRing.equals("f")) {
            capShape = "fibrous";
        } else if (_stalkSurfaceBelowRing.equals("y")) {
            capShape = "scaly";
        } else if (_stalkSurfaceBelowRing.equals("k")) {
            capShape = "silky";
        } else {
            capShape = "smooth";
        }

        hm.put("Stalk Surface Below Ring", capShape);

        if(_stalkColourAboveRing.equals("n")) {
            capShape = "brown";
        } else if (_stalkColourAboveRing.equals("b")) {
            capShape = "buff";
        } else if (_stalkColourAboveRing.equals("c")) {
            capShape = "cinnamon";
        } else if (_stalkColourAboveRing.equals("g")) {
            capShape = "gray";
        } else if (_stalkColourAboveRing.equals("o")) {
            capShape = "orange";
        } else if (_stalkColourAboveRing.equals("p")) {
            capShape = "pink";
        } else if (_stalkColourAboveRing.equals("e")) {
            capShape = "red";
        } else if (_stalkColourAboveRing.equals("w")) {
            capShape = "white";
        } else {
            capShape = "yellow";
        }
        hm.put("Stalk Colour Above Ring", capShape);

        if(_stalkColourBelowRing.equals("n")) {
            capShape = "brown";
        } else if (_stalkColourBelowRing.equals("b")) {
            capShape = "buff";
        } else if (_stalkColourBelowRing.equals("c")) {
            capShape = "cinnamon";
        } else if (_stalkColourBelowRing.equals("g")) {
            capShape = "gray";
        } else if (_stalkColourBelowRing.equals("o")) {
            capShape = "orange";
        } else if (_stalkColourBelowRing.equals("p")) {
            capShape = "pink";
        } else if (_stalkColourBelowRing.equals("e")) {
            capShape = "red";
        } else if (_stalkColourBelowRing.equals("w")) {
            capShape = "white";
        } else {
            capShape = "yellow";
        }
        hm.put("Stalk Colour Below Ring", capShape);

        if(_veilType.equals("p")) {
            capShape = "partial";
        } else {
            capShape = "universal";
        }
        hm.put("Veil Type", capShape);

        if(_veilColour.equals("n")) {
            capShape = "brown";
        } else if (_veilColour.equals("o")) {
            capShape = "orange";
        } else if (_veilColour.equals("w")) {
            capShape = "white";
        } else {
            capShape = "yellow";
        }
        hm.put("Veil Colour", capShape);

        if(_ringNumber.equals("n")) {
            capShape = "none";
        } else if (_ringNumber.equals("o")) {
            capShape = "one";
        } else {
            capShape = "two";
        }
        hm.put("Ring Number", capShape);

        if(_ringType.equals("c")) {
            capShape = "cobwebby";
        } else if (_ringType.equals("e")) {
            capShape = "evanescent";
        } else if (_ringType.equals("f")) {
            capShape = "flaring";
        } else if (_ringType.equals("l")) {
            capShape = "large";
        } else if (_ringType.equals("n")) {
            capShape = "none";
        } else if (_ringType.equals("p")) {
            capShape = "pendant";
        } else if (_ringType.equals("s")) {
            capShape = "sheathing";
        } else {
            capShape = "zone";
        }
        hm.put("Ring Type", capShape);

        if(_sporePrintColor.equals("k")) {
            capShape = "black";
        } else if (_sporePrintColor.equals("n")) {
            capShape = "brown";
        } else if (_sporePrintColor.equals("b")) {
            capShape = "buff";
        } else if (_sporePrintColor.equals("h")) {
            capShape = "chocolate";
        } else if (_sporePrintColor.equals("r")) {
            capShape = "green";
        } else if (_sporePrintColor.equals("o")) {
            capShape = "orange";
        } else if (_sporePrintColor.equals("u")) {
            capShape = "purple";
        } else if (_sporePrintColor.equals("w")) {
            capShape = "white";
        } else {
            capShape = "yellow";
        }
        hm.put("Spore Print Color", capShape);

        if(_population.equals("a")) {
            capShape = "abundant";
        } else if (_population.equals("c")) {
            capShape = "clustered";
        } else if (_population.equals("n")) {
            capShape = "numerous";
        } else if (_population.equals("s")) {
            capShape = "scattered";
        } else if (_population.equals("v")) {
            capShape = "several";
        } else {
            capShape = "solitary";
        }
        hm.put("Population", capShape);

        if(_habitat.equals("g")) {
            capShape = "grasses";
        } else if (_habitat.equals("l")) {
            capShape = "leaves";
        } else if (_habitat.equals("m")) {
            capShape = "meadows";
        } else if (_habitat.equals("p")) {
            capShape = "paths";
        } else if (_habitat.equals("u")) {
            capShape = "urban";
        } else if (_habitat.equals("w")) {
            capShape = "waste";
        } else {
            capShape = "woods";
        }
        hm.put("Habitat", capShape);


        return hm;
    }
}

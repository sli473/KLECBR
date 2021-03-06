package Mushroom;

import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class MushroomSolution implements CaseComponent {

    int _id;
    String _isPoisonous;

    public MushroomSolution() {}

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("_id", this.getClass());
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String is_isPoisonous() {
        return _isPoisonous;
    }

    public void set_isPoisonous(String _isPoisonous) {
        this._isPoisonous = _isPoisonous;
    }

}

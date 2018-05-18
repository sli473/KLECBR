import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Attribute;

public class MushroomSolution implements CaseComponent {

    String _id;
    boolean _isPoisonous;

    public MushroomSolution() {}

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("_id", this.getClass());
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean is_isPoisonous() {
        return _isPoisonous;
    }

    public void set_isPoisonous(boolean _isPoisonous) {
        this._isPoisonous = _isPoisonous;
    }

}

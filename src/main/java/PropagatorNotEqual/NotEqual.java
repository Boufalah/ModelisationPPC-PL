package PropagatorNotEqual;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

public class NotEqual extends Propagator<IntVar> {

    public NotEqual(IntVar[] vars) {
        super(vars, PropagatorPriority.BINARY,false);
    }
    @Override
    public void propagate(int i) throws ContradictionException {

    }

    @Override
    public ESat isEntailed() {
        return ESat.TRUE;
    }
}

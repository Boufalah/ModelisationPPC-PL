package PropagatorNotEqual;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.IntVar;

import java.util.Arrays;

public class NotEqualModel {

    public static void main(String[] args) {
        Model model = new Model("Testing not equal constraint");
        IntVar[] vars = model.intVarArray("x", 2, 1, 3, false);
        Propagator<IntVar> propagator = new NotEqual(vars);
        Constraint neq = new Constraint("NotEqual", propagator);

        model.post(neq);

        Solver solver = model.getSolver();
        while(solver.solve()) {
            System.out.println(Arrays.toString(vars));
        }
    }
}

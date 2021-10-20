import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.util.Collections;

public class PrimalModel implements TryYourStuff {
    @Override
    public void general() {
        int n = 8;
        Model model = new Model(n + "-queens problem");
        IntVar[] vars = new IntVar[n];

        for (int q = 0; q < n; q++) {
            vars[q] = model.intVar("Q_" + q, 1, n);
            System.out.println(vars[q]);
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                model.arithm(vars[i], "!=", vars[j]).post();
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }
        Solution solution = model.getSolver().findSolution();
        if (solution != null) {
            System.out.println(solution.toString());
        }
    }

    public static void main(String[] args) {
        PrimalModel m = new PrimalModel();
        m.general();
    }

}
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class RowColumnModel_ferre {
    public static void main(String[] args) {
        int n = 8;
        Model model = new Model(n + "-queens problem, row/column");
        IntVar[] cols = new IntVar[n];
        IntVar[] rows = new IntVar[n];

        for (int i = 0; i < n; i++) {
            rows[i] = model.intVar("R_" + i, 1, n);
            cols[i] = model.intVar("C_" + i, 1, n);
            System.out.println("" + rows[i] + " " + cols[i]);
        }


        for (int i = 0; i < n; i++) {
            for(int j=0; j < n; j++) {
                if (j != i) {
                    rows[j].sub(rows[i]).ne(cols[j].sub(cols[i])).post();
                    rows[i].sub(rows[j]).ne(cols[j].sub(cols[i])).post();
                }
            }
        }

        //constraints
        model.post(
                model.allDifferent(rows),
                model.allDifferent(cols)
        );


        /*for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                model.arithm(vars[i], "!=", vars[j]).post();
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }*/

        Solution solution = model.getSolver().findSolution();
        if (solution != null) {
            System.out.println(solution.toString());
        }

    }

}

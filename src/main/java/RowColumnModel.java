import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

import java.util.stream.IntStream;

public class RowColumnModel implements TryYourStuff {
    @Override
    public void general() {
        int n = 8;
        Model model = new Model(n + "-queens problem");

        IntVar[] c = new IntVar[n];
        IntVar[] l = new IntVar[n];

        for (int i = 0; i < n; i++) {
            c[i] = model.intVar("C_" + i, 1, n);
            l[i] = model.intVar("L_" + i, 1, n);
        }

        model.allDifferent(c).post();
        model.allDifferent(l).post();

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(i != j) {
                    ArExpression tmp_l = l[i].add(c[j].sub(c[i]));
                    ArExpression tmp_c = c[i].add(l[i].sub(l[j]));
                    tmp_l.ne(l[j]).post();
                    tmp_c.ne(c[j]).post();
                }
            }
        }

        Solution solution = model.getSolver().findSolution();
        if (solution != null) {
            for(int i = 0; i < n; i++) {
                System.out.print("(" + solution.getIntVal(l[i]) + ", " + solution.getIntVal(c[i]) + ") ");
            }
            System.out.println();
            // System.out.println(solution.toString());
        }
    }

    @Override
    public void ferre(){
        int n = 4;
        Model model = new Model(n + "-queens problem, row/column");
        IntVar[] cols = new IntVar[n];
        IntVar[] rows = new IntVar[n];

        for (int i = 0; i < n; i++) {
            rows[i] = model.intVar("R_" + i, 1, n);
            cols[i] = model.intVar("C_" + i, 1, n);
            System.out.println("" + rows[i] + " " + cols[i]);
        }

        //constraints
        for (int i = 0; i < n; i++) {
            for(int j=0; j < n; j++) {
                if (j != i) {
                    rows[j].sub(rows[i]).ne(cols[j].sub(cols[i])).post();
                    rows[i].sub(rows[j]).ne(cols[j].sub(cols[i])).post();
                }
            }
        }
        model.post(
                model.allDifferent(rows),
                model.allDifferent(cols)
        );

        Solver solver = model.getSolver();
        while (solver.solve()) {
            System.out.println("A solution was found");
        }
    }

    public static void main(String[] args) {
        RowColumnModel m = new RowColumnModel();
        m.general();
    }
}

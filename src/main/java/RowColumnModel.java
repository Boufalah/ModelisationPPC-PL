import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.variables.IntVar;

public class RowColumnModel {
    public static void main(String[] args) {
        int n = 8;
        Model model = new Model(n + "-queens problem");

        IntVar[] c = new IntVar[n];
        IntVar[] l = new IntVar[n];

        for (int i = 0; i < n; i++) {
            c[i] = model.intVar("C_" + i, 1, n);
            l[i] = model.intVar("L_" + i, 1, n);
        }

        /* Davide Ferre's solution - START */

        /*
        //constraints (l stands for 'line' and c stands for 'col')
        for (int i = 0; i < n; i++) {
            for(int j=0; j < n; j++) {
                if (j != i) {
                    l[j].sub(l[i]).ne(c[j].sub(c[i])).post();
                    l[i].sub(l[j]).ne(c[j].sub(c[i])).post();
                }
            }
        }
        model.post(
                model.allDifferent(rows),
                model.allDifferent(cols)
        );

        Solution solution = model.getSolver().findSolution();
        if (solution != null) {
            System.out.println(solution.toString());
        }
         */
        /* Davide Ferre's solution - END */

        /* Alternative solution - START */

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

        /* Alternative solution - END */
    }
}

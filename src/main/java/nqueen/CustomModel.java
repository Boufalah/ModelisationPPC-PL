package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class CustomModel extends BaseQueenModel implements Callable{
    private IntVar[] rQueens;

    public CustomModel(int n, boolean enumerate, boolean print) {
        super(n + "-queen problem custom", n, enumerate, print);
    }

    public Stats buildAndSolve() {
        rQueens = model.intVarArray("RQ", n, 0, n-1, false);

        /* Constraints */
        Constraint myConstraint = new Constraint("QueenConstraint", new CustomProp(rQueens, n));
        myConstraint.post();

        /* Solving and enumerating */
        Stats stats = solve(this);

        return stats;
    }

    public void printSolutions() {
        int[][] solved_matrix = new int[n][n];

        // print graphical solution
        for (int i = 0; i < n; i++) {
            solved_matrix[i][rQueens[i].getValue()] = 1;
        }

        Utilities.printMatrix(solved_matrix, n);
    }
}

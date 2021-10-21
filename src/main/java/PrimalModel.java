import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.Collections;

public class PrimalModel implements TryYourStuff {
    @Override
    public void ferre() {
        int n = 8;
        Model model = new Model(n + "-queens problem");
        IntVar[] rQueens = model.intVarArray("RQ", n, 0, n-1, false);

        /* Constraints */
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                model.arithm(rQueens[i], "!=", rQueens[j]).post();
                model.arithm(rQueens[i], "!=", rQueens[j], "-", j - i).post();
                model.arithm(rQueens[i], "!=", rQueens[j], "+", j - i).post();
            }
        }

        /* Solving and enumerating */
        Solver solver = model.getSolver();
        solver.showShortStatisticsOnShutdown();
        for (int i = 1; solver.solve(); i++) {
            System.out.println("****** Solution n° " + i + " ******");
            printSolution(rQueens, n);
        }
    }

    @Override
    public void general() {
        int n = 13;
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
//        m.general();
        m.ferre();
    }

    public static void printSolution(IntVar[] rQueens, int n) {
        int[][] solved_matrix = new int[n][n];

        // print graphical solution
        for (int i = 0; i < n; i++) {
            solved_matrix[i][rQueens[i].getValue()] = 1;
        }

        Utilities.printMatrix(solved_matrix, n);
    }
}

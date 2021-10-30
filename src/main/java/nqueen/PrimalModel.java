package nqueen;

import org.chocosolver.solver.variables.IntVar;

public class PrimalModel extends BaseQueenModel implements Callable {
    private IntVar[] rQueens;

    public PrimalModel(int n, boolean enumerate, boolean print) {
        super(n + "-queen problem primal", n, enumerate, print);
    }

    public Stats buildAndSolve() {
        rQueens = model.intVarArray("RQ", n, 0, n-1, false);

        /* Constraints */
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                model.arithm(rQueens[i], "!=", rQueens[j]).post(); // no both queens on same column
                model.arithm(rQueens[i], "!=", rQueens[j], "-", j - i).post(); // no both queens on same major diagonal
                model.arithm(rQueens[i], "!=", rQueens[j], "+", j - i).post(); // no both queens on same minor diagonal
            }
        }

        /* Solving and enumerating */
        Stats stats = solve(this);

        return stats;
    }

    public static void main(String[] args) {
        new PrimalModel(12, true, false).buildAndSolve();

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

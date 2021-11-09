package nqueen;

import org.chocosolver.solver.variables.IntVar;

public class BooleanModel extends BaseQueenModel implements Callable {
    private IntVar[][] rows;

    public BooleanModel(int n, boolean enumerate, boolean print) {
        super(n + "-queen problem boolean", n, enumerate, print);
    }

    public Stats buildAndSolve() {
        // Matrix of nxn int variables {0,1}
        rows = model.intVarMatrix("c", n, n, 0, 1);

        /* Constraints */
        IntVar[][] cols = new IntVar[n][n];
        for(int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                cols[j][n - 1 - i] = rows[i][j]; // rotates the rows matrix of 90° clockwise
            }
        }

        for(int i = 0; i < n; i++) {
            model.sum(rows[i], "=", 1).post(); // 1 queen in each row
            model.sum(cols[i], "=", 1).post(); // 1 queen in each column
        }

        for(int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - 1; j++) {
                for (int k = 1; i + k < n && j + k < n; k++) {
                    model.arithm(rows[i][j], "+", rows[i + k][j + k], "<=", 1).post(); // max 1 queen in every major diagonal
                    model.arithm(cols[i][j], "+", cols[i + k][j + k], "<=", 1).post(); // max 1 queen in every minor diagonal
                }
            }
        }

        /* Solving and enumerating */
        Stats stats = solve(this);

        return stats;
    }

    public void printSolutions() {
        int[][] solved_matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                solved_matrix[i][j] = rows[i][j].getValue();
            }
        }

        Utilities.printMatrix(solved_matrix, n);
    }
}

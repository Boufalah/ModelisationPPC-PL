package nqueen;

import org.chocosolver.solver.variables.BoolVar;

import static org.chocosolver.util.tools.ArrayUtils.getColumn;

public class BooleanModelAlt extends BaseQueenModel implements Callable, TryYourStuff {
    private BoolVar[][] rows;

    public BooleanModelAlt(int n, boolean enumerate, boolean print) {
        super(n + "-queen problem boolean alternative", n, enumerate, print);
    }

    @Override
    public BaseQueenModel.Stats pizzoli() {
        rows = model.boolVarMatrix("c", n, n);

        /* Constraints */
        for (int i = 0; i < n; i++) {
            model.addClausesAtMostOne(rows[i]); // at most one true value for each row
            model.addClausesAtMostOne(getColumn(rows, i)); // at most one true value for each column

            model.addClausesBoolOrArrayEqualTrue(rows[i]); // no all false rows
            model.addClausesBoolOrArrayEqualTrue(getColumn(rows, i)); // no all false columns
        }

        for(int i = 0; i < n-1; i++) {
            for(int j = 0; j < n-1; j++) {
                for(int y = -Math.min(i, j); y < n - Math.max(i,j); y++) { // constraints on diagonals
                    if(y != 0) {
                        model.arithm(rows[i][j].intVar(), "+", rows[i + y][j + y].intVar(), "<=", 1).post();
                        model.arithm(rows[i][n-j-1].intVar(), "+", rows[i + y][n-j-1 - y].intVar(), "<=", 1).post();
                    }
                }
            }
        }

        /* Solving and enumerating */
        BaseQueenModel.Stats stats = solve(this);

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

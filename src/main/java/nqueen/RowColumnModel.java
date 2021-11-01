package nqueen;

import org.chocosolver.solver.variables.IntVar;

public class RowColumnModel extends BaseQueenModel implements Callable {
    private IntVar[] rows;
    private IntVar[] cols;

    public RowColumnModel(int n, boolean enumerate, boolean print) {
        super(n + "-queen problem row/column", n, enumerate, print);
    }
    
    public Stats buildAndSolve(){
        rows = model.intVarArray("R", n, 0, n-1);
        cols = model.intVarArray("C", n, 0, n-1);

        /* Constraints */
        for (int i = 0; i < n; i++) {
            for(int j=0; j < n; j++) {
                if (i != j) {
                    rows[i].sub(cols[i]).ne(rows[j].sub(cols[j])).post(); // no both queens on same major diagonal
                    rows[i].add(cols[i]).ne(rows[j].add(cols[j])).post(); // no both queens on same minor diagonal
                }
            }
        }
        model.post(
                model.allDifferent(rows), // no two queens on same row
                model.allDifferent(cols) // no two queens on same column
        );

        /* Solving and enumerating */
        Stats stats = solve(this);

        return stats;
    }

    public static void main(String[] args) {
        new RowColumnModel(4, true, true).buildAndSolve();
    }

    public void printSolutions() {
        int[][] solved_matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            solved_matrix[rows[i].getValue()][cols[i].getValue()] = 1;
        }

        Utilities.printMatrix(solved_matrix, n);
    }
}

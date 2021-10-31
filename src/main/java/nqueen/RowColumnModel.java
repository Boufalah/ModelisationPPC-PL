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
        /* Observations
            Unfortunately, this model is not efficient because it introduces several unwanted symmetries in the solutions.
            Every solution is represented as a set of n pairs (row,col); thus, for every legitimate solution, there are
            n! permutations of these pairs, which all represents the same solution, but are considered different by the solver.
            The total number of solution returned by the solver is then n!*k, where k is the number of legitimate solutions.
            Ex. With n=6 there are k=4 legitimate solutions, with this model the solver returns !6*4 = 2880 solutions.
            This is definitely not a good modeling choice for this problem.
        */
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

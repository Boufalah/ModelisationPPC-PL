package nqueen;

import org.chocosolver.solver.variables.IntVar;

import java.util.stream.IntStream;

public class PrimalDiffModel extends BaseQueenModel implements Callable {
	private IntVar[] rQueens;

	public PrimalDiffModel(int n, boolean enumerate, boolean print) {
		super(n + "-queen problem primal allDifferent", n, enumerate, print);
	}

	public Stats buildAndSolve() {
		rQueens = model.intVarArray("Q", n, 0, n-1, false);
		IntVar[] diag1 = IntStream.range(0, n).mapToObj(i -> rQueens[i].sub(i).intVar()).toArray(IntVar[]::new);
		IntVar[] diag2 = IntStream.range(0, n).mapToObj(i -> rQueens[i].add(i).intVar()).toArray(IntVar[]::new);

		/* Constraints */
		model.post(
				model.allDifferent(rQueens), // no two queens on same column
				model.allDifferent(diag1), // no two queens on same major diagonal
				model.allDifferent(diag2)  // no two queens on same minor diagonal
		);

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

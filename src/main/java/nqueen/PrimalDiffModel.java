package nqueen;

import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

public class PrimalDiffModel implements TryYourStuff {
	@Override
	public long ferre(int n, boolean print) {
		Model model = new Model(n + "-queens problem primal allDifferent");
		IntVar[] rQueens = model.intVarArray("Q", n, 0, n-1, false);
		IntVar[] diag1 = IntStream.range(0, n).mapToObj(i -> rQueens[i].sub(i).intVar()).toArray(IntVar[]::new);
		IntVar[] diag2 = IntStream.range(0, n).mapToObj(i -> rQueens[i].add(i).intVar()).toArray(IntVar[]::new);

		/* Constraints */
		model.post(
				model.allDifferent(rQueens),
				model.allDifferent(diag1),
				model.allDifferent(diag2)
		);
		/* Solving and enumerating */
		Solver solver = model.getSolver();
		if (print) {
			for (int i = 1; solver.solve(); i++) {
				System.out.println("****** Solution n° " + i + " ******");
				printSolution(rQueens, n);
			}
		} else {
			while(solver.solve()) {}
		}
		long estimatedTime = solver.getTimeCountInNanoSeconds();

		return estimatedTime;
		/* Observations
			The modeling is "cleaner" than the nqueen.PrimalModel in my eyes, but performances seem to be worse by an avg factor of 2/3.
		*/
	}

	@Override
	public void general() {
		int n = 8;
		Model model = new Model(n + "-queens problem");
		IntVar[] vars = model.intVarArray("Q", n, 1, n, false);
		IntVar[] diag1 = IntStream.range(0, n).mapToObj(i -> vars[i].sub(i).intVar()).toArray(IntVar[]::new);
		IntVar[] diag2 = IntStream.range(0, n).mapToObj(i -> vars[i].add(i).intVar()).toArray(IntVar[]::new);
		model.post(
				model.allDifferent(vars),
				model.allDifferent(diag1),
				model.allDifferent(diag2)
		);
		Solver solver = model.getSolver();
		solver.showStatistics();
		solver.setSearch(Search.domOverWDegSearch(vars));
		Solution solution = solver.findSolution();
		if (solution != null) {
			System.out.println(solution.toString());
		}
	}

	public static void main(String[] args) {
		PrimalDiffModel m = new PrimalDiffModel();
//		m.general();
//		m.ferre();
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

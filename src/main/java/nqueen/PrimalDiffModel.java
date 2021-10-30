package nqueen;

import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

public class PrimalDiffModel extends BaseQueenModel implements Callable,TryYourStuff {
	private IntVar[] rQueens;

	public PrimalDiffModel() {}
	public PrimalDiffModel(int n, boolean enumerate, boolean print) {
		super(n + "-queen problem primal allDifferent", n, enumerate, print);
	}

	@Override
	public Stats ferre() {
		rQueens = model.intVarArray("Q", n, 0, n-1, false);
		IntVar[] diag1 = IntStream.range(0, n).mapToObj(i -> rQueens[i].sub(i).intVar()).toArray(IntVar[]::new);
		IntVar[] diag2 = IntStream.range(0, n).mapToObj(i -> rQueens[i].add(i).intVar()).toArray(IntVar[]::new);

		/* Constraints */
		model.post(
				model.allDifferent(rQueens),
				model.allDifferent(diag1),
				model.allDifferent(diag2)
		);

		/* Solving and enumerating */
		Stats stats = solve(this);

		return stats;
		/* Observations
			The modeling is "cleaner" than the PrimalModel in my eyes, but performances seem to be worse by an avg factor of 2/3.
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
		PrimalDiffModel m = new PrimalDiffModel(6, true, true);
//		m.general();
		m.ferre();
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

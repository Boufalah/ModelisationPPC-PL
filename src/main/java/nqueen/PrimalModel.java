package nqueen;

import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMiddle;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.variables.IntVar;

public class PrimalModel extends BaseQueenModel implements Callable {
    private IntVar[] rQueens;
    private Benchmark.EnumSearchStrats chosenSearchStrat;

    public PrimalModel(int n, boolean enumerate, boolean print, Benchmark.EnumSearchStrats searchStrat) {
        super(n + "-queen problem custom", n, enumerate, print);
        this.chosenSearchStrat = searchStrat;
    }
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
        searchStrategy = switch (chosenSearchStrat) {  // search strategy choice
            case DOM_OVER_W -> Search.domOverWDegSearch(rQueens);
            case MIN_DOM_LB -> Search.minDomLBSearch(rQueens);
            case MIN_DOM_MID -> Search.intVarSearch(new FirstFail(model),
                    new IntDomainMiddle(false),
                    rQueens);
            case MIN_DOM_UB -> Search.minDomUBSearch(rQueens);
            case FIRST_LB -> Search.inputOrderLBSearch(rQueens);
            case FIRST_UB -> Search.inputOrderUBSearch(rQueens);
            case RANDOM -> Search.randomSearch(rQueens, System.currentTimeMillis());
        };
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

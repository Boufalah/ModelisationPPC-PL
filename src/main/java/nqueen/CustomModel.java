package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMiddle;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.Smallest;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelectorWithTies;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

public class CustomModel extends BaseQueenModel implements Callable{
    private IntVar[] rQueens;
    private Benchmark.EnumSearchStrats chosenSearchStrat;
    private boolean propVERBOSE = false;

    public static void main(String[] args) {
        new CustomModel(100, false, false).buildAndSolve();
    }

    public CustomModel(int n, boolean enumerate, boolean print, Benchmark.EnumSearchStrats searchStrat) {
        super(n + "-queen problem custom", n, enumerate, print);
        this.chosenSearchStrat = searchStrat;
    }
    public CustomModel(int n, boolean enumerate, boolean print) {
        this(n, enumerate, print, Benchmark.EnumSearchStrats.DOM_OVER_W);
    }
    public void setPropVERBOSE(boolean value) {
        this.propVERBOSE = value;
    }
    public Stats buildAndSolve() {
        rQueens = model.intVarArray("RQ", n, 0, n-1, false);

        /* Constraints */
        Constraint myConstraint = new Constraint("QueenConstraint", new CustomProp(rQueens, n, propVERBOSE));
        myConstraint.post();

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

    public void printSolutions() {
        int[][] solved_matrix = new int[n][n];

        // print graphical solution
        for (int i = 0; i < n; i++) {
            solved_matrix[i][rQueens[i].getValue()] = 1;
        }

        Utilities.printMatrix(solved_matrix, n);
    }
}

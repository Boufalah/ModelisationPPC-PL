package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

/**
 * Base class for all the models.
 */
public abstract class BaseQueenModel {
    protected Model model;
    protected int n;
    protected boolean enumerate;
    protected boolean print;

    static class Stats
    {
        public long resolutionTime;
        public long numOfNodes;
    };
    Stats stats;

    BaseQueenModel(String modelName, int n, boolean enumerate, boolean print) {
        this.model = new Model(modelName);
        this.n = n;
        this.enumerate = enumerate;
        this.print = print;
        this.stats = new Stats();
    }

    protected Stats solve(Callable subclassInstance) {
        Solver solver = model.getSolver();
        if (enumerate) {
            if (print) {
                for (int i = 1; solver.solve(); i++) {
                    System.out.println("****** Solution n° " + i + " ******");
                    subclassInstance.printSolutions();
                }
            } else {
                while (solver.solve());
            }
        } else {
            Solution solution = solver.findSolution();
            if (print && solution != null) {
                subclassInstance.printSolutions();
            }
        }
        stats.resolutionTime = solver.getTimeCountInNanoSeconds();
        stats.numOfNodes = solver.getNodeCount();

//        solver.printShortStatistics();

        return stats;
    }
}

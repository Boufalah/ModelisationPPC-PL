package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;

/**
 * Base class for all the models.
 * It implements a solve method that can also enumerate and print solutions.
 */
public abstract class BaseQueenModel {
    protected Model model;
    protected int n;
    protected boolean enumerate;
    protected boolean print;

    public abstract Stats buildAndSolve();

    public static class Stats
    {
        public long resolutionTime;
        public long numOfNodes;
        public long numOfSolutions;
    };
    public Stats stats;

    BaseQueenModel(String modelName, int n, boolean enumerate, boolean print) {
        this.model = new Model(modelName);
        if(n >= 1)
            this.n = n;
        else
            this.n = 1;
        this.enumerate = enumerate;
        this.print = print;
        this.stats = new Stats();
    }

    protected Stats solve(Callable subclassInstance) {
        Solver solver = model.getSolver();
        int i;
        if (enumerate) {
            if (print) {
                for (i = 1; solver.solve(); i++) {
                    System.out.println("****** Solution n° " + i + " ******");
                    subclassInstance.printSolutions();
                }
                i--;
            } else {
                i = 0;
                while (solver.solve())
                    i++;
            }
        } else {
            i = -1;
            Solution solution = solver.findSolution();
            if (print && solution != null) {
                subclassInstance.printSolutions();
            }
        }
        stats.resolutionTime = solver.getTimeCountInNanoSeconds();
        stats.numOfNodes = solver.getNodeCount();
        stats.numOfSolutions = i;

//        solver.printStatistics();

        return stats;
    }
}

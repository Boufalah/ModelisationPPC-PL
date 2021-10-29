package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;

public abstract class BaseQueenModel {
    protected Model model;
    protected int n;
    protected boolean enumerate;
    protected boolean print;

    BaseQueenModel(){}
    BaseQueenModel(String modelName, int n, boolean enumerate, boolean print) {
        this.model = new Model(modelName);
        this.n = n;
        this.enumerate = enumerate;
        this.print = print;
    }

    protected long enumerate(Callable subclassInstance) {
        /* Solving and enumerating */
        Solver solver = model.getSolver();
        if (print) {
            for (int i = 1; solver.solve(); i++) {
                System.out.println("****** Solution n° " + i + " ******");
                subclassInstance.printSolutions();
            }
        } else {
            while(solver.solve());
        }
        long estimatedTime = solver.getTimeCountInNanoSeconds();

        return estimatedTime;
    }
}

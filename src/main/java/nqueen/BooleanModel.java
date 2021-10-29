package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import java.lang.*;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import static org.chocosolver.util.tools.ArrayUtils.getColumn;

public class BooleanModel implements TryYourStuff {
    @Override
    public long ferre(int n, boolean print) {
        Model model = new Model(n + "-queen problem boolean");
        // Create a matrix of nxn int variables {0,1}
        IntVar[][] rows = model.intVarMatrix("c", n, n, 0, 1);

        /* Constraints */
        IntVar[] flatArray = new IntVar[n*n];
        for (int index = 0; index < n*n; index++) {
            int i = index / n;
            int j = index % n;
            flatArray[index] = rows[i][j];
        }
        model.sum(flatArray,"=",n).post(); // the total number of queens is n
//??? Is the constraint above somehow useful? The next two are enough to find solutions and the performances seem to be comparable

        IntVar[][] cols = new IntVar[n][n];
        for(int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                cols[j][n - 1 - i] = rows[i][j]; // rotates the rows matrix of 90° clockwise
            }
        }

        for(int i = 0; i < n; i++) {
            model.sum(rows[i], "=", 1).post(); // 1 queen in each row
            model.sum(cols[i], "=", 1).post(); // 1 queen in each column
        }

        for(int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - 1; j++) {
                for (int k = 1; i + k < n && j + k < n; k++) {
                    model.arithm(rows[i][j], "+", rows[i + k][j + k], "<=", 1).post(); // max 1 queen in every major diagonal
                    model.arithm(cols[i][j], "+", cols[i + k][j + k], "<=", 1).post(); // max 1 queen in every minor diagonal
                }
            }
        }

        /* Solving and enumerating */
        Solver solver = model.getSolver();
        if (print) {
            for (int i = 1; solver.solve(); i++) {
                System.out.println("****** Solution n° " + i + " ******");
                printSolution(rows, n);
            }
        } else {
            while(solver.solve()) {}
        }
        long estimatedTime = solver.getTimeCountInNanoSeconds();

        return estimatedTime;
    }

    @Override
    public void pizzoli(String[] args) {
        /* java <CLASSNAME> n printSolutionFlag printTimeFlag
         */
        int n = 8;
        boolean printSolutionFlag = true;
        boolean printTimeFlag = true;

        if(args.length > 0)
            n = Integer.parseInt(args[0]);
        if(args.length > 1)
            printSolutionFlag = Boolean.parseBoolean(args[1]);
        if(args.length > 2)
            printTimeFlag = Boolean.parseBoolean(args[2]);

        Model model = new Model(n + "-queens problem");
        BoolVar[][] vars = new BoolVar[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                vars[i][j] = model.boolVar("Q_" + i);
            }
        }

        for (int i = 0; i < n; i++) {
            model.addClausesAtMostOne(vars[i]); // At most one true value for each row
            model.addClausesAtMostOne(getColumn(vars, i)); // At most one true value for each column

            model.addClausesBoolOrArrayEqualTrue(vars[i]); // Remove "all false values" solution
            model.addClausesBoolOrArrayEqualTrue(getColumn(vars, i)); // Remove "all false values" solution
        }

        for(int i = 0; i < n-1; i++) {
            for(int j = 0; j < n-1; j++) {
                for(int y = -Math.min(i, j); y < n - Math.max(i,j); y++) {
                    if(y != 0) {
                        model.arithm(vars[i][j].intVar(), "+", vars[i + y][j + y].intVar(), "<=", 1).post();
                        model.arithm(vars[i][n-j-1].intVar(), "+", vars[i + y][n-j-1 - y].intVar(), "<=", 1).post();
                    }
                }
            }
        }

        Solver solver = model.getSolver();
        Solution solution = solver.findSolution();
        if(printSolutionFlag)
            if (solution != null) {
                for(int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        System.out.print(solution.getIntVal(vars[i][j]) + "  ");
                    }
                    System.out.println();
                }
                // System.out.println(solution.toString());
            }
        if(printTimeFlag) {
            long estimatedTime = solver.getTimeCountInNanoSeconds();
            System.out.println("Estimated time: " + ((float) estimatedTime / 1000000) + "ms");
        }
    }

    public static void main(String[] args) {
        BooleanModel m = new BooleanModel();
//        m.ferre();
        m.pizzoli(args);
    }

    public static void printSolution(IntVar[][] rows, int n) {
        int[][] solved_matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                solved_matrix[i][j] = rows[i][j].getValue();
            }
        }

        Utilities.printMatrix(solved_matrix, n);
    }
}

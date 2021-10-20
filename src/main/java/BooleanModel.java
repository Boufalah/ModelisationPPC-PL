import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BooleanModel implements TryYourStuff {
    @Override
    public void ferre() {
        int n = 8;
        Model model = new Model(n + "-queen problem boolean");
        // Create a matrix of nxn bool variables
        IntVar[][] matrix = model.intVarMatrix("c", n, n, 0, 1);

        //constraints
        IntVar[] flatArray = new IntVar[n*n];
        for (int index = 0; index < n*n; index++) {
            int i = index / n;
            int j = index % n;
            flatArray[index] = matrix[i][j];
        }
        model.sum(flatArray,"=",n).post(); // total number of queens constraint

        for(int i = 0; i < n; i++) {
            model.sum(matrix[i], "=", 1).post(); // 1 queen in each row constraint
        }

        IntVar[][] cols = new IntVar[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                cols[i][j] = matrix[j][i];
            }
        }
        for(int i = 0; i < n; i++) {
            model.sum(cols[i], "=", 1).post(); // 1 queen in each column constraint
        }

        Solution solution = model.getSolver().findSolution();
        if (solution != null) {
            System.out.println(solution.toString());
            Utilities.printMatrix(matrix, n);
        }
    }

    public static void main(String[] args) {
        BooleanModel m = new BooleanModel();
        m.ferre();
    }
}

import org.chocosolver.solver.variables.IntVar;

public final class Utilities {
    /**
     * Prints a IntVar matrix in a simple graphical form
     * @param matrix if a cell=1 then it prints #
     * @param n dimension of the square matrix
     */
    public static void printMatrix(IntVar matrix[][], int n) {
        int[][] solved_matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                solved_matrix[i][j] = matrix[i][j].getValue();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(solved_matrix[i][j] == 1) {
                    System.out.print("xx ");
                } else {
                    System.out.print("-- ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
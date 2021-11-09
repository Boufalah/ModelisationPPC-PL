package nqueen;

public final class Utilities {
    /**
     * Prints a int[n][n] matrix in a simple graphical form
     * @param matrix if a cell=1 then it prints 'XX', otherwise '--'
     * @param n dimension of the square matrix
     */
    public static void printMatrix(int matrix[][], int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(matrix[i][j] == 1) {
                    System.out.print("XX ");
                } else {
                    System.out.print("-- ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
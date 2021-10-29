package nqueen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Benchmark {

    public static void main(String[] args) throws IOException {
        System.out.println("Running tests and saving results to stats.csv ...");

        PrintWriter printWriter = new PrintWriter(new FileWriter("stats.csv"));
        printWriter.println("n; bool; prim; primDiff; primDual; primDualDiff");
        int n_tests = 5;
        for (int n = 4; n < 13; n++) {
            float boolSum = 0, primSum = 0, primDiffSum = 0, primDualSum = 0, primDualDiffSum = 0, rowColumnSum = 0;
            for (int i = 0; i < n_tests; i++) {
                boolSum += new BooleanModel(n, true, false).ferre()/(double)1000000000;
                primSum += new PrimalModel(n, true, false).ferre()/(double)1000000000;
                primDiffSum += new PrimalDiffModel(n, true, false).ferre()/(double)1000000000;
                primDualSum += new PrimalDualModel(n, true, false).ferre()/(double)1000000000;
                primDualDiffSum += new PrimalDualDiffModel(n, true, false).ferre()/(double)1000000000;
//                rowColumnSum += new RowColumnModel(n, true, false).ferre()/(double)1000000000;
            }

            printWriter.printf("n=%d; %.3f; %.3f; %.3f; %.3f; %.3f%n",
                    n, boolSum/n_tests, primSum/n_tests, primDiffSum/n_tests, primDualSum/n_tests, primDualDiffSum/n_tests);
        }

        printWriter.close();
    }
}

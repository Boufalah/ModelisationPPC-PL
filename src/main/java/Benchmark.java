import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Benchmark {
    public static void main(String[] args) throws IOException {
        System.out.println("Running tests and saving results in stats.csv ...");
        BooleanModel booleanModel = new BooleanModel();
        PrimalDiffModel primalDiffModel = new PrimalDiffModel();
        PrimalDualDiffModel primalDualDiffModel = new PrimalDualDiffModel();
        PrimalDualModel primalDualModel = new PrimalDualModel();
        PrimalModel primalModel = new PrimalModel();
        RowColumnModel rowColumnModel = new RowColumnModel();

        PrintWriter printWriter = new PrintWriter(new FileWriter("stats.csv"));
        printWriter.println("n; bool; prim; primDiff; primDual; primDualDiff");
        int n_tests = 5;
        for (int n = 4; n < 13; n++) {
                float boolSum = 0, primSum = 0, primDiffSum = 0, primDualSum = 0, primDualDiffSum = 0;
            for (int i = 0; i < n_tests; i++) {
                boolSum += booleanModel.ferre(n, false)/(double)1000000000;
                primSum += primalModel.ferre(n, false)/(double)1000000000;
                primDiffSum += primalDiffModel.ferre(n, false)/(double)1000000000;
                primDualSum += primalDualModel.ferre(n, false)/(double)1000000000;
                primDualDiffSum += primalDualDiffModel.ferre(n, false)/(double)1000000000;
            }
//            double rowCol = rowColumnModel.ferre(n, false)/(double)1000000000;
            printWriter.printf("n=%d; %.3f; %.3f; %.3f; %.3f; %.3f%n",
                    n, boolSum/n_tests, primSum/n_tests, primDiffSum/n_tests, primDualSum/n_tests, primDualDiffSum/n_tests);

            //System.out.println("row/col " + rowColumnModel.ferre(n, false)/(double)1000000000);
        }

        printWriter.close();
    }
}

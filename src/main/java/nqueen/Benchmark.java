package nqueen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumMap;

public class Benchmark {
    private final static int NANO_SEC = 1000000000;
    enum EnumModels { PRIMAL, PRIMAL_DIFF, BOOLEAN, PRIMAL_DUAL, PRIMAL_DUAL_DIFF, ROW_COLUMN, CUSTOM }

    public static void ferre() throws IOException {
        System.out.println("Running tests and saving results to stats.csv ...");

        PrintWriter enumWriter = new PrintWriter(new FileWriter("resolution_enum_stats.csv"));
        enumWriter.println("n; prim; primDiff; bool; primDual; primDualDiff");
        PrintWriter nodesWriter = new PrintWriter(new FileWriter("nodes_stats.csv"));
        nodesWriter.println("n; prim; primDiff; bool; primDual; primDualDiff");
        float n_tests = 5;

        /* Solving a model for the first time is very slow for some reasons,
        so we run each model once without saving the results to exclude the first
        execution from the benchmark */
        idleLoop();

        for (int n = 4; n < 10; n++) {
            EnumMap<EnumModels, BaseQueenModel.Stats> stats = new EnumMap<>(EnumModels.class);
            EnumMap<EnumModels, Long> numOfNodes = new EnumMap<>(EnumModels.class);
            EnumMap<EnumModels, Long> timeSum = new EnumMap<>(EnumModels.class);
            initializeMap(timeSum);

            for (int i = 0; i < n_tests; i++) {
                stats.put(EnumModels.PRIMAL, new PrimalModel(n, true, false).ferre());
                stats.put(EnumModels.PRIMAL_DIFF, new PrimalDiffModel(n, true, false).ferre());
                stats.put(EnumModels.BOOLEAN, new BooleanModel(n, true, false).ferre());
                stats.put(EnumModels.PRIMAL_DUAL, new PrimalDualModel(n, true, false).ferre());
                stats.put(EnumModels.PRIMAL_DUAL_DIFF, new PrimalDualDiffModel(n, true, false).ferre());

                /* Update resolution times */
                for (EnumModels model : stats.keySet()) {
                    BaseQueenModel.Stats stats_value = stats.get(model);
                    long crt_value = timeSum.get(model);
                    timeSum.put(model, crt_value + stats_value.resolutionTime);
                }
            }
            /* Get number of nodes */
            for (EnumModels model : stats.keySet()) {
                BaseQueenModel.Stats stats_value = stats.get(model);
                numOfNodes.put(model, stats_value.numOfNodes);
            }

            enumWriter.printf("n=%d; %.3f; %.3f; %.3f; %.3f; %.3f%n",
                    n, timeSum.get(EnumModels.PRIMAL)/n_tests/NANO_SEC, timeSum.get(EnumModels.PRIMAL_DIFF)/n_tests/NANO_SEC,
                    timeSum.get(EnumModels.BOOLEAN)/n_tests/NANO_SEC, timeSum.get(EnumModels.PRIMAL_DUAL)/n_tests/NANO_SEC,
                    timeSum.get(EnumModels.PRIMAL_DUAL_DIFF)/n_tests/NANO_SEC);
            nodesWriter.printf("n=%d; %d; %d; %d; %d; %d%n",
                    n, numOfNodes.get(EnumModels.PRIMAL), numOfNodes.get(EnumModels.PRIMAL_DIFF),
                    numOfNodes.get(EnumModels.BOOLEAN), numOfNodes.get(EnumModels.PRIMAL_DUAL),
                    numOfNodes.get(EnumModels.PRIMAL_DUAL_DIFF));
        }

        enumWriter.close();
        nodesWriter.close();
    }

    private static void idleLoop() {
        new PrimalModel(3, true, false).ferre();
        new PrimalDiffModel(3, true, false).ferre();
        new BooleanModel(3, true, false).ferre();
        new PrimalDualModel(3, true, false).ferre();
        new PrimalDualDiffModel(3, true, false).ferre();
        new RowColumnModel(3, true, false).ferre();
    }

    private static void initializeMap(EnumMap<EnumModels, Long> numOfNodes) {
        for (EnumModels model : EnumModels.values()) {
            numOfNodes.put(model, 0L);
        }
    }

    public static void main(String[] args) throws IOException {
        ferre();
    }
}

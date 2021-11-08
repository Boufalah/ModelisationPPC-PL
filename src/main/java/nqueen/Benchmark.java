package nqueen;

import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.variables.IntVar;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumMap;

public class Benchmark {
    private final static int NANO_SEC = 1000000000;
    private final static int MILLI_SEC = 1000000;
    enum EnumModels { PRIMAL, PRIMAL_DIFF, BOOLEAN, BOOLEAN_ALT, PRIMAL_DUAL, PRIMAL_DUAL_DIFF, ROW_COLUMN, CUSTOM }
    enum EnumSearchStrats { DOM_OVER_W, MIN_DOM_LB, MIN_DOM_MID, MIN_DOM_UB, FIRST_LB, FIRST_UB, RANDOM }

    private static void idleLoop() {
        new PrimalModel(10, true, false).buildAndSolve();
        new PrimalDiffModel(10, true, false).buildAndSolve();
        new BooleanModel(10, true, false).buildAndSolve();
        new BooleanModelAlt(10, true, false).buildAndSolve();
        new PrimalDualModel(10, true, false).buildAndSolve();
        new PrimalDualDiffModel(10, true, false).buildAndSolve();
        new RowColumnModel(10, true, false).buildAndSolve();
        new CustomModel(10, true, false).buildAndSolve();
    }

    private static void initializeMap(EnumMap<EnumModels, Long> mapLong) {
        for (EnumModels model : EnumModels.values()) {
            mapLong.put(model, 0L);
        }
    }

    public static void testModels() throws IOException {
        System.out.println("Running tests and saving results to resolution_enum_stats.csv and nodes_stats.csv...");

        PrintWriter enumWriter = new PrintWriter(new FileWriter("resolution_enum_stats.csv"));
        enumWriter.println("n; custom; prim; primDiff; bool; bool_alt; primDual; primDualDiff");
        PrintWriter nodesWriter = new PrintWriter(new FileWriter("nodes_stats.csv"));
        nodesWriter.println("n; custom; prim; primDiff; bool; bool_alt; primDual; primDualDiff");
        float n_tests = 10;

        /* Solving a model for the first time is very slow for some reasons,
        so we run each model once without saving the results to exclude the first
        execution from the benchmark */
        idleLoop();

        /* Enumeration */
        for (int n = 4; n < 13; n++) {
            System.out.println("Running tests for " + n + "-Queens...");

            EnumMap<EnumModels, BaseQueenModel.Stats> statsMap = new EnumMap<>(EnumModels.class);
            EnumMap<EnumModels, Long> timeSumMap = new EnumMap<>(EnumModels.class);
            initializeMap(timeSumMap);

            for (int i = 0; i < n_tests; i++) {
                statsMap.put(EnumModels.CUSTOM, new CustomModel(n, true, false).buildAndSolve());
                statsMap.put(EnumModels.PRIMAL, new PrimalModel(n, true, false).buildAndSolve());
                statsMap.put(EnumModels.PRIMAL_DIFF, new PrimalDiffModel(n, true, false).buildAndSolve());
                statsMap.put(EnumModels.BOOLEAN, new BooleanModel(n, true, false).buildAndSolve());
                statsMap.put(EnumModels.BOOLEAN_ALT, new BooleanModel(n, true, false).buildAndSolve());
                statsMap.put(EnumModels.PRIMAL_DUAL, new PrimalDualModel(n, true, false).buildAndSolve());
                statsMap.put(EnumModels.PRIMAL_DUAL_DIFF, new PrimalDualDiffModel(n, true, false).buildAndSolve());

                /* Update resolution times */
                for (EnumModels model : statsMap.keySet()) {
                    BaseQueenModel.Stats stats_value = statsMap.get(model);
                    long crt_value = timeSumMap.get(model);
                    timeSumMap.put(model, crt_value + stats_value.resolutionTime);
                }
            }

            enumWriter.printf("n=%d; %.6f; %.6f; %.6f; %.6f; %.6f; %.6f; %.6f%n",
                    n, timeSumMap.get(EnumModels.CUSTOM)/n_tests/NANO_SEC, timeSumMap.get(EnumModels.PRIMAL)/n_tests/NANO_SEC,
                    timeSumMap.get(EnumModels.PRIMAL_DIFF)/n_tests/NANO_SEC, timeSumMap.get(EnumModels.BOOLEAN)/n_tests/NANO_SEC,
                    timeSumMap.get(EnumModels.BOOLEAN_ALT)/n_tests/NANO_SEC,
                    timeSumMap.get(EnumModels.PRIMAL_DUAL)/n_tests/NANO_SEC, timeSumMap.get(EnumModels.PRIMAL_DUAL_DIFF)/n_tests/NANO_SEC);
            nodesWriter.printf("n=%d; %d; %d; %d; %d; %d; %d; %d%n",
                    n, statsMap.get(EnumModels.CUSTOM).numOfNodes, statsMap.get(EnumModels.PRIMAL).numOfNodes,
                    statsMap.get(EnumModels.PRIMAL_DIFF).numOfNodes, statsMap.get(EnumModels.BOOLEAN).numOfNodes,
                    statsMap.get(EnumModels.BOOLEAN_ALT).numOfNodes,
                    statsMap.get(EnumModels.PRIMAL_DUAL).numOfNodes, statsMap.get(EnumModels.PRIMAL_DUAL_DIFF).numOfNodes);
        }

        enumWriter.close();
        nodesWriter.close();
    }

    public static void testPropagateMethod() {
        int n = 8;
        CustomModel model = new CustomModel(n, false, false);
        model.setPropVERBOSE(true);
        model.buildAndSolve();
    }

    public static void testSearchStrategies() throws IOException {
        PrintWriter searchWriter = new PrintWriter(new FileWriter("search_stats.csv"));
        searchWriter.println("n; domOverW; minDomLB; minDomMid; minDomUB; firstLB; firstUB; random;");
        for (int n = 4; n <= 29; n++) {
            System.out.println("******** n = " + n +" *********");
            searchWriter.printf("%d; ", n);
            for (EnumSearchStrats searchStrat : EnumSearchStrats.values()) {
//                if (searchStrat == EnumSearchStrats.MIN_DOM_MID) {
                float time = new CustomModel(n, false, false, searchStrat).buildAndSolve().resolutionTime
                            / (float) MILLI_SEC;
                searchWriter.printf("%.3f; ", time);

//                }
            }
            searchWriter.printf("%n");
        }
        searchWriter.close();
    }

    public static void main(String[] args) throws IOException {
//        testModels();
        testPropagateMethod();

    }


}

package nqueen.junit;

import nqueen.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class nQueenTester {
    /*
    * If the class doesn't run due to a strange error in terminal, go to File > Settings > Http proxy
    * and check "auto-detect proxy settings"
    */
    int[] expected = {2, 10, 4, 40, 92, 352, 724, 2680, 14200};
    BaseQueenModel boolModel, boolModelAlt, primalModel, primalDualModel;
    BaseQueenModel primalDiffModel, primalDualDiffModel, rowColumnModel, customModel;

    @org.junit.jupiter.api.Test
    void boolBuildAndSolve() {
        for(int n = 4; n <= 12; n++)
        {
            boolModel = new BooleanModel(n, true, false);
            boolModelAlt = new BooleanModelAlt(n, true, false);

            assertEquals(expected[n-4], boolModel.buildAndSolve().numOfSolutions);
            assertEquals(expected[n-4], boolModelAlt.buildAndSolve().numOfSolutions);
        }
    }

    @org.junit.jupiter.api.Test
    void primalBuildAndSolve() {
        for(int n = 4; n <= 12; n++)
        {
            primalModel = new PrimalModel(n, true, false);
            primalDiffModel = new PrimalDiffModel(n, true, false);

            assertEquals(expected[n-4], primalModel.buildAndSolve().numOfSolutions);
            assertEquals(expected[n-4], primalDiffModel.buildAndSolve().numOfSolutions);
        }

    }

    @org.junit.jupiter.api.Test
    void primalDualBuildAndSolve() {
        for(int n = 4; n <= 12; n++)
        {
            primalDualModel = new PrimalDualModel(n, true, false);
            primalDualDiffModel = new PrimalDualDiffModel(n, true, false);

            assertEquals(expected[n-4], primalDualModel.buildAndSolve().numOfSolutions);
            assertEquals(expected[n-4], primalDualDiffModel.buildAndSolve().numOfSolutions);
        }

    }

//    @org.junit.jupiter.api.Test
//    void rowColumnBuildAndSolve() {
//        assertEquals(expected[n-4], rowColumnModel.buildAndSolve().numOfSolutions);
//    }

    @org.junit.jupiter.api.Test
    void customBuildAndSolve() {
        for(int n = 4; n <= 12; n++)
        {
            customModel = new CustomModel(n, true, false);

            assertEquals(expected[n-4], customModel.buildAndSolve().numOfSolutions);
        }

    }
}
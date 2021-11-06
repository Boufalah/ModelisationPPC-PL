package nqueen.junit;

import nqueen.*;

import static org.junit.jupiter.api.Assertions.*;

class nQueenTester {
    /*
    * If the class doesn't run due to a strange error in terminal, go to File > Settings > Http proxy
    * and check "auto-detect proxy settings"
    */
    int n = 8;
    BaseQueenModel boolModel, boolModelAlt, primalModel, primalDualModel;
    BaseQueenModel primalDiffModel, primalDualDiffModel, rowColumnModel, customModel;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        try{
            boolModel = new BooleanModel(n, true, false);
            boolModelAlt = new BooleanModelAlt(n, true, false);
            primalModel = new PrimalModel(n, true, false);
            primalDualModel = new PrimalDualModel(n, true, false);
            primalDiffModel = new PrimalDiffModel(n, true, false);
            primalDualDiffModel = new PrimalDualDiffModel(n, true, false);
            rowColumnModel = new RowColumnModel(n, true, false);
            customModel = new CustomModel(n, true, false);
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void boolBuildAndSolve() {
        assertEquals(92, boolModel.buildAndSolve().numOfSolutions);
        assertEquals(92, boolModelAlt.buildAndSolve().numOfSolutions);
    }

    @org.junit.jupiter.api.Test
    void primalBuildAndSolve() {
        assertEquals(92, primalModel.buildAndSolve().numOfSolutions);
        assertEquals(92, primalDiffModel.buildAndSolve().numOfSolutions);
    }

    @org.junit.jupiter.api.Test
    void primalDualBuildAndSolve() {
        assertEquals(92, primalDualModel.buildAndSolve().numOfSolutions);
        assertEquals(92, primalDualDiffModel.buildAndSolve().numOfSolutions);
    }

    @org.junit.jupiter.api.Test
    void rowColumnBuildAndSolve() {
        assertEquals(92, rowColumnModel.buildAndSolve().numOfSolutions);
    }

    @org.junit.jupiter.api.Test
    void customBuildAndSolve() {
        assertEquals(92, customModel.buildAndSolve().numOfSolutions);
    }
}
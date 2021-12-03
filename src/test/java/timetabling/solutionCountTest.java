package timetabling;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class solutionCountTest {

    @Test
    public void testModelsmall1(){
        Data d = Instances.small1;
        BoolModel boolModel = new BoolModel(d);
        boolModel.buildAndSolve();

        IntegerModel integerModel = new IntegerModel(d,false);//verbose
        // build
        integerModel.communConstraintWithBool();
        // solve
        Solver solver = integerModel.getModel().getSolver();
        solver.findAllSolutions();
        long solutionCount = solver.getSolutionCount();
        assertEquals(boolModel.solutionCount,solutionCount);
        integerModel = new IntegerModel(d,false);//verbose
        integerModel.buildAndSolve();
    }

}

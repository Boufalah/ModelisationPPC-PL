package timetabling;

import org.chocosolver.solver.variables.IntVar;

public class Variable {
    IntVar[][] W ;
    IntVar[][] D ;
    IntVar[][] T ;
    IntVar[] timeslots ;

    public Variable(int courses, int lectures) {
         W = new IntVar[courses][lectures];
         D = new IntVar[courses][lectures];
         T = new IntVar[courses][lectures];
    }
}

package timetabling;

import org.chocosolver.solver.Solver;

import java.util.ArrayList;

public class ModelsMain {
    public static void main(String[] args) {
        ArrayList<Data> instances = new ArrayList<>();
        instances.add(Instances.small1);
        instances.add(Instances.small2);
        instances.add(Instances.medium1);
        instances.add(Instances.medium1);
        instances.add(Instances.large1);
        instances.add(Instances.large2);

        int i = 0;
        for (Data d: instances) {
            IntegerModel integerModel1 = new IntegerModel(d,false);
            IntegerModel integerModel2 = new IntegerModel(d,false);
            BoolModel boolModel = new BoolModel(d);//verbose
            // build and solve
            System.out.println("################# ################# ################# ################# ");
            System.out.println("Instance "+i);
            System.out.println("################# Bool #################");
            boolModel.buildAndSolve();
            System.out.println("#################Interger commun Constraint with bool #################"+i);
            integerModel1.communConstraintWithBool();
            Solver solver = integerModel1.getModel().getSolver();
            while(solver.solve()) {
                continue;
            }
            solver.printStatistics();

            System.out.println("################# Interger all Constraint ################# "+i);
            integerModel2.buildAndSolve();
        }
    }
}

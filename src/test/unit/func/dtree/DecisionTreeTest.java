package test.unit.func.dtree;


import func.DecisionTreeClassifier;
import func.dtree.*;
import shared.DataSet;
import shared.Instance;
import util.TestUtil;

public class DecisionTreeTest {

    public void testDiscrete(){
        Instance[] instances =  {
                new Instance(new double[] {0, 0, 0, 1}, 1),
                new Instance(new double[] {1, 0, 0, 0}, 1),
                new Instance(new double[] {1, 0, 0, 0}, 1),
                new Instance(new double[] {1, 0, 0, 0}, 1),
                new Instance(new double[] {1, 0, 0, 1}, 0),
                new Instance(new double[] {1, 0, 0, 1}, 0),
                new Instance(new double[] {1, 0, 0, 1}, 0),
                new Instance(new double[] {1, 0, 0, 1}, 0)
        };
        Instance[] tests =  {
                new Instance(new double[] {0, 1, 1, 1}),
                new Instance(new double[] {0, 0, 0, 0}),
                new Instance(new double[] {1, 0, 0, 0}),
                new Instance(new double[] {1, 1, 1, 1})
        };
        DataSet set = new DataSet(instances);
        PruningCriteria cspc = new ChiSquarePruningCriteria(0);
        SplitEvaluator gse = new GINISplitEvaluator();
        SplitEvaluator igse = new InformationGainSplitEvaluator();
        DecisionTreeClassifier dt = new DecisionTreeClassifier(igse, null, true);
        dt.estimate(set);

        TestUtil.assertEquals(dt.value(tests[0]).getDiscrete(), 1);
        TestUtil.assertEquals(dt.value(tests[1]).getDiscrete(), 1);
        TestUtil.assertEquals(dt.value(tests[2]).getDiscrete(), 1);
        TestUtil.assertEquals(dt.value(tests[3]).getDiscrete(), 0);
    }
}

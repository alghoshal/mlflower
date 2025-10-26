package in.mlflow;

import org.mlflow.tracking.ActiveRun;
import org.mlflow.tracking.MlflowClient;
import org.mlflow.tracking.MlflowContext;

/**
 * NestedMlflowClient showing two level child/ grand-child runs nested within Parent run.
 * 
 * Creates run hierarchy of the form:
 * 
 * 	Parent
 * 		|
 * 		|----- Child1
 *		| 			|
 * 		|			---- GrandChild1
 * 		|
 * 		|---- Child2
 * 
 * @author algo
 */
public class NestedMlfClient {
	public static final String TRACKING_URI = "http://127.0.0.1:8080";

	public static void main(String[] args) {
		String experimentName = "EXP_1";

		MlflowClient client = new MlflowClient(TRACKING_URI);
		// Create experiment
		if (!client.getExperimentByName(experimentName).isPresent()) {
			client.createExperiment(experimentName);
		}

		MlflowContext mlflowContext = new MlflowContext(client);

		NestedMlfClient mlfClient = new NestedMlfClient();

		mlfClient.captureActiveRun(mlflowContext, experimentName, "Parent");


	}

	/**
	 * 
	 * @param mlflowContext
	 * @param experimentName
	 */
	void captureActiveRun(MlflowContext mlflowContext, String experimentName, String runName) {

		mlflowContext.setExperimentName(experimentName).withActiveRun(runName, (run -> {
			run.logParam("parent_dat1", "p1");
			run.logMetric("PARENT_METRIC", 20.0);
			
			// Start a nested child run
			ActiveRun childRun1 = mlflowContext.startRun("Child1", run.getId());
			childRun1.logParam("child1_dat1" , "c1");
			childRun1.logMetric("CHILD1_METRIC", 3);
			
			// Start a two levels nested, grand child run
			ActiveRun grandchildRun1 = mlflowContext.startRun("GrandChild1", childRun1.getId());
			grandchildRun1.logParam("grandchild1_dat1" , "grnc1");
			grandchildRun1.logMetric("grandchild1_METRIC", 30303);
			
			// End child1 run
			childRun1.endRun();
			
			// Start another nested child run
			ActiveRun childRun2 = mlflowContext.startRun("Child2", run.getId());
			childRun2.logParam("child2_dat2" , "c2");
			childRun2.logMetric("CHILD2_METRIC", 131);
			childRun2.endRun();
			
		}));
	}
}

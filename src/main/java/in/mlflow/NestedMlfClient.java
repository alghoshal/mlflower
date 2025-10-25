package in.mlflow;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.mlflow.tracking.ActiveRun;
import org.mlflow.tracking.MlflowClient;
import org.mlflow.tracking.MlflowContext;

/**
 * NestedMlflowClient showing Child runs nested within Parent run
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

		mlfClient.captureActiveRun(mlflowContext, experimentName, "parent");


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
			ActiveRun childRun1 = mlflowContext.startRun("child1", run.getId());
			childRun1.logParam("child1_dat1" , "c1");
			childRun1.logMetric("CHILD1_METRIC", 3);
			childRun1.endRun();
			
			// Start another nested child run
			ActiveRun childRun2 = mlflowContext.startRun("child2", run.getId());
			childRun2.logParam("child2_dat2" , "c2");
			childRun2.logMetric("CHILD2_METRIC", 131);
			childRun2.endRun();
			
		}));
	}
}

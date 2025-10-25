package in.mlflow;

import org.mlflow.tracking.MlflowClient;
import org.mlflow.tracking.MlflowContext;

/**
 * MlflowClient
 * 
 * @author algo
 */
public class MlfClient {
	public static final String TRACKING_URI = "http://127.0.0.1:8080";

	public static void main(String[] args) {
		String experimentName = "EXP_1";

		MlflowClient client = new MlflowClient(TRACKING_URI);
		// Create experiment
		if (!client.getExperimentByName(experimentName).isPresent()) {
			client.createExperiment(experimentName);
		}
		
		new MlfClient().captureActiveRun(new MlflowContext(client), experimentName);
	}

	/**
	 * 
	 * @param mlflowContext
	 * @param experimentName
	 */
	void captureActiveRun(MlflowContext mlflowContext, String experimentName) {

		mlflowContext.setExperimentName(experimentName).withActiveRun("runOne", (run -> {
			run.logParam("dat1", "43.53");
			run.logParam("dat2", "0.5123");
			run.logMetric("MY_METRIC1", 20.0);
		}));
	}
}

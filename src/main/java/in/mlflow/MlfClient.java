package in.mlflow;

import java.nio.file.FileSystems;
import java.nio.file.Path;

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

		MlflowContext mlflowContext = new MlflowContext(client);

		MlfClient mlfClient = new MlfClient();

		// 1st run
		mlfClient.captureActiveRun(mlflowContext, experimentName, "runOne");

		// 2nd run
		mlfClient.captureActiveRun(mlflowContext, experimentName, "runTwo");

	}

	/**
	 * 
	 * @param mlflowContext
	 * @param experimentName
	 */
	void captureActiveRun(MlflowContext mlflowContext, String experimentName, String runName) {

		mlflowContext.setExperimentName(experimentName).withActiveRun(runName, (run -> {
			run.logParam("dat1_" + runName, "148.53");
			run.logParam("dat2_" + runName, "10.5123");
			run.logMetric("MY_METRIC_" + runName, 20.0);
			Path artifactPath = FileSystems.getDefault().getPath("src/main/resources", "arti1");
			run.logArtifact(artifactPath, runName);
		}));
	}
}

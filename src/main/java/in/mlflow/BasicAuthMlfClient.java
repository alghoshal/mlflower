package in.mlflow;

import org.mlflow.tracking.MlflowClient;
import org.mlflow.tracking.MlflowContext;
import org.mlflow.tracking.creds.BasicMlflowHostCreds;

/**
 * BasicAuthMlflowClient.
 * 
 * Start the standalone Mlflow server with the additional '--app-name basic-auth' param:
 * 	mlflow server --host 127.0.0.1 --port 8080 --app-name basic-auth
 * 
 * Note: Default admin username/password: admin/password
 * (@See: https://mlflow.org/docs/latest/self-hosting/security/basic-http-auth/)
 * 
 * @author algo
 */
public class BasicAuthMlfClient extends MlfClient{

	public static void main(String[] args) {
		String experimentName = "EXP_1";

		BasicMlflowHostCreds basicAuthCredentials = new BasicMlflowHostCreds(TRACKING_URI, "admin", "password");
		MlflowClient client = new MlflowClient(basicAuthCredentials);
		
		// Create experiment
		if (!client.getExperimentByName(experimentName).isPresent()) {
			client.createExperiment(experimentName);
		}
		
		BasicAuthMlfClient mlfClient= new BasicAuthMlfClient();
		MlflowContext mlflowContext = new MlflowContext(client);

		// 1st run 
		mlfClient.captureActiveRun(mlflowContext, experimentName,"runAuthThree");
		
		// 2nd run
		mlfClient.captureActiveRun(mlflowContext, experimentName,"runAuthFour");
	}
	
}

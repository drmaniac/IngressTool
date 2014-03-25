package de.pieczewski.ingresstool.intelmap.backgroundTasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import de.pieczewski.ingresstool.R;
import de.pieczewski.ingresstool.util.IOUtils;
import de.pieczewski.ingresstool.util.IngressToolInformations;

public class UpdateStatusService extends AsyncTask<Void, Void, Boolean> {
	
	private static final String TAG = UpdateStatusService.class.getName();

	private Context context;
	
	public UpdateStatusService(Context context) {
		this.context = context;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		
		Boolean updateAvaiable = false;
		
		// CHECK FOR UPDATES
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 1000);
		DefaultHttpClient updateClient = new DefaultHttpClient(httpParams);
		
		//HttpPost updateMethode = new HttpPost("http://www.pieczewski.de/www-files/latestIngressTool.txt");
		HttpPost updateMethode = new HttpPost("https://drive.google.com/uc?export=download&confirm=no_antivirus&id=0B6jE5pO3jtCtRExOeFRCbnlWVEU");
		HttpResponse updateRes;
		try {
			updateRes = updateClient.execute(updateMethode);
			String latestVersion = IOUtils.convertInputStream(updateRes.getEntity().getContent());
			IngressToolInformations.latestVersion = latestVersion;
			Integer latestVersionInt = Integer.parseInt(latestVersion);
			if(IngressToolInformations.currentVersionInt != null && IngressToolInformations.currentVersionInt.compareTo(latestVersionInt) < 0) {
				updateAvaiable = true;
			}
			Log.i(TAG,"latest  version is "+latestVersion);
			Log.i(TAG,"current version is "+IngressToolInformations.currentVersion);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "something goes wrong",e);
		} catch (IOException e) {
			Log.e(TAG, "something goes wrong",e);
		} catch (Exception e) {
			Log.e(TAG, "something goes wrong",e);
		}
		return updateAvaiable;
	}
	
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(result) {
			Toast.makeText(context, context.getResources().getString(R.string.update_available), Toast.LENGTH_LONG).show();
		}
	}
}

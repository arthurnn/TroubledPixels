package com.fivehundredpx.troubledpixels.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.troubledpixels.Application;
import com.fivehundredpx.troubledpixels.R;

public class ImageUploadTask extends AsyncTask<Object, Void, String> {
	private static final String TAG = "ImageUploadTask";

	@Override
	protected String doInBackground(Object... pa) {

		final String photo_id = (String) pa[0];
		final String upload_key = (String) pa[1];
		final Uri selectedImage = (Uri) pa[2];
		final AccessToken accessToken = (AccessToken) pa[3];
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(
					"https://api.500px.com/v1/upload");


			final Context c = Application.getContext();
			
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();

//			Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(c.getContentResolver(), selectedImage);
//			bitmap.compress(CompressFormat.JPEG, 100, bos);

//			android.provider.MediaStore.Images.Media.
			
//			byte[] data = bos.toByteArray();
			entity.addPart("photo_id", new StringBody(photo_id));
			entity.addPart("upload_key", new StringBody(upload_key));
//			entity.addPart("file", new ByteArrayBody(data, "myImage.jpg"));
			entity.addPart("file", new FileBody(new File(selectedImage.getPath())));
			
			
			entity.addPart("consumer_key", new StringBody(
					c.getString(R.string.px_consumer_key)));
			entity.addPart("access_key", new StringBody(accessToken.getToken()));
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost,
					localContext);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							response.getEntity().getContent(), "UTF-8"));

			String sResponse = reader.readLine();

			// final int statusCode =
			// response.getStatusLine().getStatusCode();
			//
			// if (statusCode != HttpStatus.SC_OK) {
			// return "success";
			// }
			// return null;

			return sResponse;
		} catch (Exception e) {
			// if (dialog.isShowing())
			// dialog.dismiss();
			// Toast.makeText(getApplicationContext(),
			// "error , too bad. Sorry", Toast.LENGTH_LONG).show();
			Log.e(e.getClass().getName(), e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void onProgressUpdate(Void... unsued) {

	}

	@Override
	protected void onPostExecute(String sResponse) {
		final Context c = Application.getContext();
		try {
			// if (dialog.isShowing())
			// dialog.dismiss();

			if (sResponse != null) {
				JSONObject JResponse = new JSONObject(sResponse);
				String message = JResponse.getString("error");
				if (!"None.".equals(message)) {
					
					Toast.makeText(c.getApplicationContext(), message,
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(c.getApplicationContext(),
							"Photo uploaded successfully",
							Toast.LENGTH_SHORT).show();
					Log.w(TAG, message);

//					onFinishTask();

				}
			}
		} catch (Exception e) {
			Toast.makeText(c.getApplicationContext(), "error...",
					Toast.LENGTH_LONG).show();
			Log.e(e.getClass().getName(), e.getMessage(), e);
		}
	}

}
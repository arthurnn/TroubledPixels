package com.fivehundredpx.troubledpixels;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fivehundredpx.troubledpixels.controller.User;
import com.fivehundredpx.troubledpixels.tasks.CreatePhotoTask;
import com.google.inject.Inject;

@ContentView(R.layout.activity_photo)
public class PhotoActivity extends RoboActivity implements
		CreatePhotoTask.Delegate {

	private static final String TAG = "PhotoActivity";

	@InjectView(R.id.pic)
	ImageView pictureImageView;

	@InjectView(R.id.submit_btn)
	Button submitButton;
	
	@InjectView(R.id.editText1) EditText titleEdit;
	@InjectView(R.id.editText2) EditText descEdit;
	
	
	@Inject
	User user;

	private Uri selectedImage;
	
	private Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		selectedImage = getIntent().getData();
		ContentResolver cr = getContentResolver();

		try {
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(
					cr, selectedImage);

			pictureImageView.setImageBitmap(bitmap);

		} catch (Exception e) {
			Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
			Log.e("Camera", e.toString());
		}

		Log.w(TAG, user.fullname);

		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new CreatePhotoTask(PhotoActivity.this)
						.execute(user.accessToken, titleEdit.getText().toString(), descEdit.getText().toString() );

			}
		});

	}
	
	@Override
	protected void onDestroy() {
		

		pictureImageView.setImageDrawable(null);
//		bitmap.recycle();
		
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_photo, menu);
		return true;
	}

	class ImageUploadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... pa) {

			final String photo_id = pa[0];
			final String upload_key = pa[1];
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost("https://api.500px.com/v1/upload");

//				CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
//						getString(R.string.px_consumer_key),
//						getString(R.string.px_consumer_secret));
//
//				consumer.setTokenWithSecret(user.accessToken.getToken(),
//						user.accessToken.getTokenSecret());
//				consumer.sign(httpPost);
				
				
				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, bos);
				byte[] data = bos.toByteArray();
				entity.addPart("photo_id", new StringBody(photo_id));
				entity.addPart("upload_key", new StringBody(upload_key));
				entity.addPart("file", new ByteArrayBody(data, "myImage.jpg"));
				entity.addPart("consumer_key", new StringBody(getString(R.string.px_consumer_key)));
				entity.addPart("access_key", new StringBody(user.accessToken.getToken()));
				httpPost.setEntity(entity);
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));

				String sResponse = reader.readLine();
				
//				final int statusCode = response.getStatusLine().getStatusCode();
//
//				if (statusCode != HttpStatus.SC_OK) {
//					return "success";
//				}
//				return null;
				
				return sResponse;
			} catch (Exception e) {
				// if (dialog.isShowing())
				// dialog.dismiss();
//				Toast.makeText(getApplicationContext(),
//						"error , too bad. Sorry", Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				// if (dialog.isShowing())
				// dialog.dismiss();

				if (sResponse != null) {
					JSONObject JResponse = new JSONObject(sResponse);
					String message = JResponse.getString("error");
					if (!"None.".equals(message)) {
						Toast.makeText(getApplicationContext(), message,
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"Photo uploaded successfully",
								Toast.LENGTH_SHORT).show();
						Log.w(TAG, message);
						
						onFinishTask();
						
					}
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "error...",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}

	@Override
	public void success(JSONObject json) {
		
		try {
			new ImageUploadTask().execute(json.getJSONObject("photo").getString("id"),json.getString("upload_key"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		

	}

	@Override
	public void fail() {
		// TODO Auto-generated method stub

	}
	
	public void onFinishTask(){
		Intent i = new Intent(PhotoActivity.this, ConfirmationActivity.class);
		startActivity(i);
		finish();
	}

}

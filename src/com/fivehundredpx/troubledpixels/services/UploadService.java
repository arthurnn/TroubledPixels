package com.fivehundredpx.troubledpixels.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.troubledpixels.Application;
import com.fivehundredpx.troubledpixels.tasks.CreatePhotoTask;
import com.fivehundredpx.troubledpixels.tasks.ImageUploadTask;

public class UploadService extends Service implements CreatePhotoTask.Delegate {
	private int NOTIFICATION = 12341;

	private NotificationManager mNM;

	private AccessToken accessToken;
	private Uri selectedImage;

	public class LocalBinder extends Binder {
		public UploadService getService() {
			return UploadService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
//		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		showNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.selectedImage = intent.getParcelableExtra("selectedImageUri");
		this.accessToken = intent.getParcelableExtra("accessToken");

		final String title = intent.getStringExtra("title");
		final String desc = intent.getStringExtra("description");
//		Toast.makeText(this, "Service onBind ..." + selectedImage,
//				Toast.LENGTH_LONG).show();

		new CreatePhotoTask(UploadService.this).execute(accessToken, title, desc);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service destroyed ...", Toast.LENGTH_LONG).show();
	}

	// String photoId;
	@Override
	public void success(JSONObject json) {
//		Toast.makeText(this, "success...", Toast.LENGTH_SHORT).show();
		try {
			String photoId = json.getJSONObject("photo").getString("id");

			new ImageUploadTask().execute(photoId,
					json.getString("upload_key"), selectedImage, accessToken);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void fail() {
		// TODO Auto-generated method stub

	}

	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		// CharSequence text = ;//getText(R.string.local_service_started);
		// Set the icon, scrolling text and timestamp
		// Notification notification = new
		// Notification.Builder(this).setTicker(text).build();
		Notification notification = new NotificationCompat.Builder(
				Application.getContext()).setContentTitle("service upload ...")
				.build();
		// .setContentText(subject)
		// .setSmallIcon(R.drawable.new_mail)
		// .setLargeIcon(aBitmap)

		// The PendingIntent to launch our activity if the user selects this
		// notification
		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		// new Intent(this, LocalServiceActivities.Controller.class), 0);

		// Set the info for the views that show in the notification panel.
		// notification.setLatestEventInfo(this,
		// getText(R.string.local_service_label),
		// text, contentIntent);

		// Send the notification.
		mNM.notify(NOTIFICATION, notification);
	}

}

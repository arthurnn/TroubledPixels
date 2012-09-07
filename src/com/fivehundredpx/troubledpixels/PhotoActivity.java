package com.fivehundredpx.troubledpixels;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.fivehundredpx.troubledpixels.services.UploadService;
import com.google.inject.Inject;

@ContentView(R.layout.activity_photo)
public class PhotoActivity extends RoboActivity  {

	private static final String TAG = "PhotoActivity";

	private static final int THUMBNAIL_SIZE = 200;

	@InjectView(R.id.pic)
	ImageView pictureImageView;

	@InjectView(R.id.submit_btn)
	Button submitButton;

	@InjectView(R.id.editText1)
	EditText titleEdit;
	@InjectView(R.id.editText2)
	EditText descEdit;

	@Inject
	User user;

	private Uri selectedImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		selectedImage = getIntent().getData();

		try {

			Bitmap bitmap = getThumbnail(selectedImage);
			pictureImageView.setImageBitmap(bitmap);

		} catch (Exception e) {
			Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
			Log.e("Camera", e.toString());
		}

		Log.w(TAG, user.fullname);

		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doBindService();
//				Intent i = new Intent(PhotoActivity.this,UploadService.class);
//				i.putExtra("selectedImageUri", selectedImage);
//				startService(i);
				
//				new CreatePhotoTask(PhotoActivity.this).execute(
//						user.accessToken, titleEdit.getText().toString(),
//						descEdit.getText().toString());

			}
		});

	}

	public Bitmap getThumbnail(Uri uri) throws FileNotFoundException,
			IOException {
		InputStream input = getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1)
				|| (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;

		double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE)
				: 1.0;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = this.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}

	@Override
	protected void onDestroy() {

		pictureImageView.setImageDrawable(null);
		// bitmap.recycle();

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_photo, menu);
		return true;
	}

//	private UploadService mBoundService;
//
//	private ServiceConnection mConnection = new ServiceConnection() {
//	    public void onServiceConnected(ComponentName className, IBinder service) {
//	        // This is called when the connection with the service has been
//	        // established, giving us the service object we can use to
//	        // interact with the service.  Because we have bound to a explicit
//	        // service that we know is running in our own process, we can
//	        // cast its IBinder to a concrete class and directly access it.
//	        mBoundService = ((UploadService.LocalBinder)service).getService();
//
//	        // Tell the user about this for our demo.
////	        Toast.makeText(PhotoActivity.this, R.string.local_service_connected,
////	                Toast.LENGTH_SHORT).show();
//	    }
//
//	    public void onServiceDisconnected(ComponentName className) {
//	        // This is called when the connection with the service has been
//	        // unexpectedly disconnected -- that is, its process crashed.
//	        // Because it is running in our same process, we should never
//	        // see this happen.
//	        mBoundService = null;
//	        Toast.makeText(PhotoActivity.this, R.string.local_service_disconnected,
//	                Toast.LENGTH_SHORT).show();
//	    }
//	};

	private boolean mIsBound;
	void doBindService() {

		Intent i = new Intent(PhotoActivity.this, UploadService.class);
		i.putExtra("selectedImageUri", selectedImage);
		i.putExtra("accessToken", user.accessToken);
		i.putExtra("title",titleEdit.getText().toString());
		i.putExtra("description",descEdit.getText().toString());
		
		
//		bindService(i, mConnection, Context.BIND_AUTO_CREATE);
		
		
		startService(i);
	    mIsBound = true;
	    onFinishTask();
	}

//	void doUnbindService() {
//	    if (mIsBound) {
//	        // Detach our existing connection.
//	        unbindService(mConnection);
//	        mIsBound = false;
//	    }
//	}


	public void onFinishTask() {
		Intent i = new Intent(PhotoActivity.this, ConfirmationActivity.class);
//		i.putExtra("photoId", photoId);
		startActivity(i);
		finish();
	}

}

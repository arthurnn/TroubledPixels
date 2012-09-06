package com.fivehundredpx.troubledpixels;

import java.io.File;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fivehundredpx.troubledpixels.controller.User;
import com.fivehundredpx.troubledpixels.tasks.ImageDownloadTask;
import com.google.inject.Inject;

@ContentView(R.layout.activity_profile)
public class ProfileActivity extends RoboActivity implements
		ImageDownloadTask.Delegate {
	private static final String TAG = "ProfileActivity";

	private static final int TAKE_PICTURE = 2 << 2;

	@InjectView(R.id.profile_image) ImageView profileImage;
	@InjectView(R.id.camera_btn) ImageButton cameraBtn;

	@Inject User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (null == savedInstanceState)
			new ImageDownloadTask(this).execute(user.userpic_url);

		cameraBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePhoto(v);
			}
		});

	}

	private Uri imageUri;

	public void takePhoto(View view) {
	    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
	    intent.putExtra(MediaStore.EXTRA_OUTPUT,
	            Uri.fromFile(photo));
	    imageUri = Uri.fromFile(photo);
	    startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			if (resultCode == Activity.RESULT_OK) {
				final Uri selectedImage = imageUri;
				getContentResolver().notifyChange(selectedImage, null);

				Intent i = new Intent(ProfileActivity.this, PhotoActivity.class);
				i.setData(selectedImage);
				startActivity(i);

			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void success(Drawable d) {
		profileImage.setImageDrawable(d);
	}

	@Override
	public void fail() {
		// TODO Auto-generated method stub

	}

}

package com.fivehundredpx.troubledpixels;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.troubledpixels.controller.User;
import com.fivehundredpx.troubledpixels.tasks.UserDetailTask;
import com.fivehundredpx.troubledpixels.tasks.XAuth500pxTask;
import com.google.inject.Inject;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity implements
		XAuth500pxTask.Delegate, UserDetailTask.Delegate {
	private static final String TAG = "MainActivity";

	@InjectView(R.id.login_password) EditText passText;
	@InjectView(R.id.login_email) EditText loginText;
	@InjectView(R.id.login_btn) Button loginBtn;

	@Inject User user;

	private XAuth500pxTask loginTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loginTask = new XAuth500pxTask(this);

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				 loginTask.execute(loginText.getText().toString(),passText.getText().toString());
				// TODO
				loginTask.execute("arthurnn", "password=a");
			}
		});
	}
	

	@Override
	public void success(AccessToken result) {
		Log.w(TAG, "success");
		user.accessToken = result;

		new UserDetailTask(this).execute(result);

	}

	@Override
	public void fail() {

	}

	@Override
	public void success(JSONObject user) {
		Log.w(TAG, user.toString());
		try {
			this.user.userpic_url = user.getString("userpic_url");
			this.user.fullname = user.getString("fullname");
		} catch (JSONException e) {
			Log.e(TAG, "", e);
		}

		startActivity(new Intent(MainActivity.this, ProfileActivity.class));
		finish();
	}

}

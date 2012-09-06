package com.fivehundredpx.troubledpixels;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fivehundredpx.api.FiveHundredException;
import com.fivehundredpx.api.PxApi;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.tasks.UserDetailTask;
import com.fivehundredpx.api.tasks.XAuth500pxTask;
import com.fivehundredpx.troubledpixels.controller.User;
import com.google.inject.Inject;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity implements
		XAuth500pxTask.Delegate, UserDetailTask.Delegate {
	private static final String TAG = "MainActivity";

	@InjectView(R.id.login_password) EditText passText;
	@InjectView(R.id.login_email) EditText loginText;
	@InjectView(R.id.login_btn) Button loginBtn;

	@Inject User user;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final XAuth500pxTask  loginTask = new XAuth500pxTask(MainActivity.this);
				loginTask.execute(getString(R.string.px_consumer_key),
						getString(R.string.px_consumer_secret), loginText
								.getText().toString(), passText.getText()
								.toString());
			}
		});
	}

	@Override
	public void onSuccess(AccessToken result) {
		Log.w(TAG, "success");
		user.accessToken = result;

		final PxApi api = new PxApi(user.accessToken,
				getString(R.string.px_consumer_key),
				getString(R.string.px_consumer_secret));

		new UserDetailTask(this).execute(api);

	}

	@Override
	public void onSuccess(JSONObject user) {
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

	@Override
	public void onFail() {
		;
	}

	@Override
	public void onFail(FiveHundredException e) {
		onFail();
	}

}

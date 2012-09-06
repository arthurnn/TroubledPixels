package com.fivehundredpx.troubledpixels.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fivehundredpx.api.FiveHundredException;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.auth.OAuthAuthorization;
import com.fivehundredpx.api.auth.XAuthProvider;
import com.fivehundredpx.troubledpixels.Application;
import com.fivehundredpx.troubledpixels.R;

public class XAuth500pxTask extends AsyncTask<String, Void, AccessToken> {
	private static final String TAG = "XAuth500pxTask";
	
	public interface Delegate {
		public void success(AccessToken result);
		public void fail();
	}

	private Delegate _d;
	public XAuth500pxTask(Delegate delegate) {
		this._d = delegate;
	}

	@Override
	protected AccessToken doInBackground(String... params) {
		final String _user = params[0];
		final String _password = params[1];

		final Context context = Application.getContext();

		try {

			final OAuthAuthorization oauth = new OAuthAuthorization.Builder()
					.consumerKey(context.getString(R.string.px_consumer_key))
					.consumerSecret(
							context.getString(R.string.px_consumer_secret))
					.build();

			final AccessToken accessToken = oauth
					.getAccessToken(new XAuthProvider(_user, _password));

			return accessToken;

		} catch (FiveHundredException e) {
			final String msg = String.format(
					"error %d for username[%s] and password[%s]",
					e.getStatusCode(), _user, _password);
			Log.e(TAG, msg, e);
		}

		return null;

	}

	@Override
	protected void onPostExecute(AccessToken result) {
		if (null != result)
			_d.success(result);
		else
			_d.fail();

	}
}

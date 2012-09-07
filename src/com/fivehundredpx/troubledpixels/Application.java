package com.fivehundredpx.troubledpixels;

import android.content.Context;

public class Application extends android.app.Application {

	private static Context _applicationContext;
	
	public static final String PREF_TOKEN_SECRET = "Troubled.tokenSecret";
	public static final String PREF_ACCES_TOKEN = "Troubled.accesToken";
	public static final String SHARED_PREFERENCES = "TroubledSharedPreferences";
	
	@Override
	public void onCreate() {

	
		_applicationContext = getApplicationContext();
	}

	public static Context getContext() {
		return _applicationContext;
	}


}


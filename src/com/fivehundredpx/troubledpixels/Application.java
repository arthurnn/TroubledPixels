package com.fivehundredpx.troubledpixels;

import android.content.Context;

public class Application extends android.app.Application {

	private static Context _applicationContext;
	
	
	@Override
	public void onCreate() {

	
		_applicationContext = getApplicationContext();
	}

	public static Context getContext() {
		return _applicationContext;
	}


}


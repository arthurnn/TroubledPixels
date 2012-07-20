package com.fivehundredpx.troubledpixels.controller;

import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.troubledpixels.Application;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class User {

	@Inject Application application;
	
	
	public AccessToken accessToken;
	
	public String fullname;
	public String userpic_url;
	
}

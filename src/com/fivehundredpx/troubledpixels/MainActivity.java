package com.fivehundredpx.troubledpixels;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fivehundredpx.troubledpixels.tasks.XAuth500pxTask;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

	
	@InjectView(R.id.login_password) EditText passText;
	@InjectView(R.id.login_email) EditText loginText;
	@InjectView(R.id.login_btn) Button loginBtn;
	
	private XAuth500pxTask loginTask ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        loginTask = new XAuth500pxTask();
        
        loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginTask.execute(loginText.getText().toString(),passText.getText().toString());
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}

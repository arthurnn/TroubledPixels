package com.fivehundredpx.troubledpixels;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@ContentView(R.layout.activity_confirmation)
public class ConfirmationActivity extends RoboActivity {

	protected static final String TAG = "ConfirmationActivity";
	
	@InjectView(R.id.button1) Button openBtn;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        openBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri intentUri = Uri.parse("http://500px.com/photo/" + getIntent().getStringExtra("photoId"));
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				Log.w(TAG, intentUri.toString());
				intent.setData(intentUri);

				startActivity(intent);
				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_confirmation, menu);
        return true;
    }

    
}

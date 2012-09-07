package com.fivehundredpx.troubledpixels;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@ContentView(R.layout.activity_confirmation)
public class ConfirmationActivity extends RoboActivity {

	protected static final String TAG = "ConfirmationActivity";
	
	@InjectView(R.id.startAgainBtn) Button startAgainBtn;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        startAgainBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        
//        openBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Uri intentUri = Uri.parse("http://500px.com/photo/" + getIntent().getStringExtra("photoId"));
//				Intent intent = new Intent();
//				intent.setAction(Intent.ACTION_VIEW);
//				Log.w(TAG, intentUri.toString());
//				intent.setData(intentUri);
//
//				startActivity(intent);
//				
//			}
//		});
    }

    
}

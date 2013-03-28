package com.bpd.smilemorph;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class ActivitySplashScreen extends Activity implements OnCompletionListener{
	
	private static final long SPLASHTIME = 3000;
	MediaPlayer mp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_splash);
	    
	    String path = "android.resource://" + getPackageName()+"/raw/splash";
	    VideoView video = (VideoView) findViewById(R.id.videoView);
        //video.setVideoPath("android.resource://" +getPackageName()+ "raw/splash");
	    video.setVideoURI(Uri.parse(path));
	    video.setVideoPath(path);
        video.start();
        video.setOnCompletionListener(this);
	}
	
	//@Override
    public void onCompletion(MediaPlayer mp)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
	
}

package com.bpd.smilemorph;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;
import com.bpd.smilemorph.R;

public class ActivitySplashScreen extends Activity implements OnCompletionListener{
	
	private static final long SPLASHTIME = 3000;
	MediaPlayer mp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_splash);
	    Log.i("test0","test0");
	    String path = "android.resource://" + getPackageName()+"/raw/splash";
	    VideoView video = (VideoView) findViewById(R.id.videoView);
        //video.setVideoPath("android.resource://" +getPackageName()+ "raw/splash");
	    video.setVideoURI(Uri.parse(path));
	    video.setVideoPath(path);
	    Log.i("test","test");
        video.start();
        Log.i("tests","tests");
        video.setOnCompletionListener(this);
	}
	
	//@Override
    public void onCompletion(MediaPlayer mp)
    {
        Intent intent = new Intent(this, MainActivity.class);
        Log.i("test1","test1");
        startActivity(intent);
        finish();
    }
	
}

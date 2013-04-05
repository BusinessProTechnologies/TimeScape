package com.bpd.smilemorph;


import java.io.File;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bpd.facebook.AsyncFacebookRunner;
import com.bpd.facebook.BaseRequestListener;
import com.bpd.facebook.DialogError;
import com.bpd.facebook.Facebook;
import com.bpd.facebook.Facebook.DialogListener;
import com.bpd.facebook.FacebookError;
import com.bpd.facebook.SessionStore;
import com.bpd.facebook.Util;
import com.bpd.twitter.TwitterUtils;

public class MainActivity extends Activity implements OnClickListener {

	ImageView imgView1,imgView2,imgView3,imgView4;
	private Facebook mFacebook;
	private ProgressDialog mProgress;
	private Handler mRunOnUi = new Handler();
	private final Handler mTwitterHandler = new Handler();
	private TextView loginStatus;
	private SharedPreferences prefs;
	static String TWITTER_CONSUMER_KEY = "GMlGXTXf4WvSGSuydoOaQ";
    static String TWITTER_CONSUMER_SECRET = "V9FNz3tH7r4OS2JOsodNqBKALwnVdeGBMCTT2TCRuAQ";
 
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
 
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
 
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
 // Twitter
    public static Twitter twitter;
    private static RequestToken requestToken ;
 // Shared Preferences
    private static SharedPreferences mSharedPreferences;
    static boolean flag =false;
    // Internet Connection detector
    //private ConnectionDetector cd;
    final Runnable mUpdateTwitterNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG).show();
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		File nfile=new File(Environment.getExternalStorageDirectory()+"/SmileMorph");
	    nfile.mkdir();
	    Log.i("DIR name=",nfile+"");
		imgView1 = (ImageView) findViewById(R.id.imgView_1);
		imgView1.setOnClickListener(this);
		imgView2 = (ImageView) findViewById(R.id.imgView_2);
		imgView2.setOnClickListener(this);
		imgView3 = (ImageView) findViewById(R.id.imgView_3);
		imgView3.setOnClickListener(this);
		imgView4 = (ImageView) findViewById(R.id.imgView_4);
		imgView4.setOnClickListener(this);
		mFacebook = new Facebook(Util.APP_ID);
	    mProgress = new ProgressDialog(this);
	    this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
	 // Shared Preferences
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);
	    //cd = new ConnectionDetector(getApplicationContext());
	    if (!isTwitterLoggedInAlready()) {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
                Log.i("verifier = ", verifier);
 
                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            requestToken, verifier);
 
                    // Shared Preferences
                    Editor e = mSharedPreferences.edit();
 
                    // After getting access token, access token secret
                    // store them in application preferences
                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                    e.putString(PREF_KEY_OAUTH_SECRET,
                            accessToken.getTokenSecret());
                    // Store login status - true
                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                    e.commit(); // save changes
                    Log.i("preferencees", "below");
                    Log.i("PREF_KEY_OAUTH_TOKEN", PREF_KEY_OAUTH_TOKEN);
                    Log.i("PREF_KEY_OAUTH_SECRET", PREF_KEY_OAUTH_SECRET);
                    Log.i("PREF_KEY_TWITTER_LOGIN", PREF_KEY_TWITTER_LOGIN);
                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
 
                    // Hide login button
                   // btnLoginTwitter.setVisibility(View.GONE);
 
                    // Show Update Twitter
                    //lblUpdate.setVisibility(View.VISIBLE);
                    //txtUpdate.setVisibility(View.VISIBLE);
                    //btnUpdateStatus.setVisibility(View.VISIBLE);
                    //btnLogoutTwitter.setVisibility(View.VISIBLE);
 
                    // Getting user details from twitter
                    // For now i am getting his name only
                    int userID = accessToken.getUserId();
                    //User user = twitter.showUser(userID);
                    User user = twitter.showUser(userID);
                    String username = user.getName();
 
                    // Displaying in xml ui
                    //lblUserName.setText(Html.fromHtml("<b>Welcome " + username + "</b>"));
                    //lv.setVisibility(TextView.VISIBLE);
                    //Log.i("flag =", ""+flag);
                    //reload();
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.imgView_1) {
			Intent intent = new Intent(MainActivity.this, SetProjectNameActivity.class);
			startActivity(intent);
		}else if(v.getId() == R.id.imgView_2) {
			Intent intent = new Intent(MainActivity.this, ExistingMorphActivity.class);
			startActivity(intent);
		}else if(v.getId() == R.id.imgView_3) {
			String review = "test for link";
			SessionStore.restore(mFacebook, MainActivity.this);
			if (mFacebook.isSessionValid()) {
				postToFacebook(review);
			} else {
				Log.e("isSessionValid","false");
				mFacebook.authorize(MainActivity.this, Util.PERMISSIONS, -1, new FbLoginDialogListener(review));
			}
		}else if(v.getId() == R.id.imgView_4) {
			/*Url url = as("o_55po3npkt3", "R_2094206a52e97f156c194d055ff72018").call(shorten("http://rosaloves.com/stories/view/11"));
    		Log.i("url....mithun...",""+url.getShortUrl());
    		new Twitter_Dialog(MainActivity.this,"http://twitter.com/?status="+"Tweeting from TimeScape : "+url.getShortUrl()).show();*/
			//new Twitter_Dialog(MainActivity.this,"http://twitter.com/?status="+Uri.encode("Twitter Post")).show();
			//loginToTwitter();
			 /*String token ="361697332-piFuFdyvqHIdaXJYmxzHOAGdaIFha1JctJAvJCQN"; 
	            String secret = "ECa0ebPP3FCq0OIvzU1o32hEqFh6G7GHIKkRZ7kLY";
	            AccessToken a = new AccessToken(token,secret);
	            Twitter twitter = new TwitterFactory().getInstance();
	            twitter.setOAuthConsumer("GMlGXTXf4WvSGSuydoOaQ", "V9FNz3tH7r4OS2JOsodNqBKALwnVdeGBMCTT2TCRuAQ");
	            twitter.setOAuthAccessToken(a);
	            try {
	                twitter.updateStatus("test twt");
	            	//Log.i("","");
	            } catch (TwitterException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }*/
			//loginToTwitter();
		}
	}
	
	/***********************************Start For Fb****************************************/
	
	private void postToFacebook(String post_message) {	
		
		mProgress.setMessage("Posting ...");
		mProgress.show();
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
		
		Bundle params = new Bundle();
		params.putString("message", post_message);
		mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener());
	}
	
	private class FbLoginDialogListener implements DialogListener {
    	String _review = "";
    	public FbLoginDialogListener(String review) {
			_review = review;
		}
        public void onComplete(Bundle values) {
            SessionStore.save(mFacebook, MainActivity.this);
			Toast.makeText(MainActivity.this, "Facebook connection successful", Toast.LENGTH_SHORT).show();
            //getFbName();
			postToFacebook(_review);
        }

        public void onFacebookError(FacebookError error) {
           Toast.makeText(MainActivity.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();
           
        }
        
        public void onError(DialogError error) {
        	Toast.makeText(MainActivity.this, "Facebook connection failed", Toast.LENGTH_SHORT).show(); 
        	
        }

        public void onCancel() {
        	Toast.makeText(MainActivity.this, "Facebook Login cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    
	private  class WallPostListener extends BaseRequestListener {
		public void onComplete(final String response) {
        	
			mRunOnUi.post(new Runnable() {
        		@Override
        		public void run() {
        			mProgress.cancel();
        			Toast.makeText(MainActivity.this, "Posted to Facebook", Toast.LENGTH_SHORT).show();
        		}
        	});
        }
    }
	/**********************************End for Fb************************************************/
	/******************************Start For Twitter***********************************/
	public void updateLoginStatus() {
		loginStatus.setText("Logged into Twitter : " + TwitterUtils.isAuthenticated(prefs));
	}
	

	private String getTweetMsg(String _paramSend) {
		return "Tweeting from TimeScape : " + _paramSend;
	}	
	
	public void sendTweet(final String _paramSend) {
		Thread t = new Thread() {
	        public void run() {
	        	
	        	try {
	        		TwitterUtils.sendTweet(prefs,getTweetMsg(_paramSend));
	        		mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
	        }

	    };
	    t.start();
	}
	
	 /**
     * Function to login twitter
     * */
    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            twitter4j.conf.Configuration configuration = builder.build();
 
            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();
            //twitter.
            Log.i("test", "in login");
            try {
            	//atz = twitter.getAuthorization();
                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
                Log.i("requesttoken =", ""+requestToken);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(requestToken.getAuthenticationURL())));
                flag = true;
                /*String user = twitter.verifyCredentials().getScreenName(); 
                statuses = twitter.getUserTimeline(user);*/
                
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            Toast.makeText(getApplicationContext(),
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
        
        
    }
	private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }
	/******************End For Twitter******************/
}

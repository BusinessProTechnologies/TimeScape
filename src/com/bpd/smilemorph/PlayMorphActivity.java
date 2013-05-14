package com.bpd.smilemorph;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.bpd.utils.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PlayMorphActivity extends Activity implements OnClickListener{

	private Handler mHandler;
	private String imageString,projectName;
	private String[] separated;
	ImageView closeView,playView,pouseView,shareView,image,image_nxt;
	ImageView prevImageView,nextImageView;
	final Context context = this;
	private Integer count_img_play=0;
	Thread newThread;
	MyThread play_img_thread;
	Boolean flag,start_chk;
	View[] vw;
	ArrayList<Bitmap> bitmapArray;
	Bitmap _bitmapScaled;
	FileOutputStream fo;
	private File projPath;
	ViewSwitcher switcher;
	BitmapDrawable drawables[];
	int mCurrentDrawable = 0;
	int imgHeight,imgWidth;
	DisplayMetrics displaymetrics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createvideo);
		flag = false;
		start_chk = false;
		mHandler = new Handler();
		play_img_thread = new MyThread();
		newThread = new Thread(play_img_thread);
		newThread.start();
		play_img_thread.start();
		play_img_thread.pause();
		
		Bundle extras = getIntent().getExtras();
		imageString = extras.getString("imageString");
		projectName = extras.getString("projectName");
		separated = imageString.replace("|", ",").split(",");
		
		
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		imgHeight = displaymetrics.heightPixels;
		imgWidth = displaymetrics.widthPixels;
		Log.i("width+height",imgWidth+"-"+imgHeight);
		prevImageView = (ImageView) findViewById(R.id.prevImageView);
		nextImageView = (ImageView) findViewById(R.id.nextImageView);
		//prevImageView.setBackgroundColor(Color.TRANSPARENT);
		//nextImageView.setBackgroundColor(Color.TRANSPARENT);
		prevImageView.animate().setDuration(1000);
		nextImageView.animate().setDuration(1000);
		switcher = (ViewSwitcher) findViewById(R.id.switcher);
		
		closeView = (ImageView) findViewById(R.id.closeBtn);
		closeView.setOnClickListener(this);
		playView = (ImageView) findViewById(R.id.playBtn);
		playView.setOnClickListener(this);
		pouseView = (ImageView) findViewById(R.id.pouseBtn);
		pouseView.setOnClickListener(this);
		shareView = (ImageView) findViewById(R.id.shareBtn);
		shareView.setOnClickListener(this);
		
		bitmapArray = new ArrayList<Bitmap>();
		projPath = new File(Environment.getExternalStorageDirectory()
				+ "/SmileMorph/" + projectName);
		/*drawables = new BitmapDrawable[separated.length];
		for(count_img_play = 0; count_img_play < separated.length-1; count_img_play++){*/
			
		BitmapFactory.Options bfo = new BitmapFactory.Options();  
	    bfo.inSampleSize = 1;   
	    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[count_img_play],bfo); 
	    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
	    /*drawables[count_img_play] = new BitmapDrawable(getResources(),ThumbImage); 
		}
	    prevImageView.setImageDrawable(drawables[0]);
	    nextImageView.setImageDrawable(drawables[1]);*/
	    //Log.i("drawableImage", drawableImage+"");
	    prevImageView.setImageDrawable(drawableImage);
		//}
	    //count_img_play++;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.closeBtn){
			/*Intent intent = new Intent(PlayMorphActivity.this,
					SelectedImageActivity.class);
			startActivity(intent);*/
			finish();
		}else if (v.getId() == R.id.playBtn) {
			//count_img_play =0;
			
			if(count_img_play>=separated.length){
				count_img_play = 0;
			}
			//flag = true;
			//startAnimation();
			play_img_thread.resume();
		}else if (v.getId() == R.id.pouseBtn) {
			//flag = false;
			//play_img_thread = null;
			play_img_thread.pause();
		}else if (v.getId() == R.id.shareBtn) {
			
			// custom dialog
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_record_share);
			//dialog.setTitle("Record and Share Video");

			ImageView fbBtn = (ImageView) dialog
					.findViewById(R.id.fbBtn);
			fbBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(PlayMorphActivity.this,
							createVideoActivity.class);
					//Log.i("morphname", _morphName);
					intent.putExtra("imageString", imageString);
					intent.putExtra("projectName", projectName); 
					
					startActivity(intent);
				}
			});
			ImageView utubeBtn = (ImageView) dialog
					.findViewById(R.id.utubeBtn);
			utubeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);*/
				}
			});
			/*ImageView twitterBtn = (ImageView) dialog
			.findViewById(R.id.twitterBtn);
			twitterBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);
				}
			});*/
			ImageView saveVdoBtn = (ImageView) dialog
			.findViewById(R.id.saveVdoBtn);
			saveVdoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(CreateMorphActivity.this,
						PhotoIntentActivity.class);
				intent.putExtra("morphName", projectName);
				startActivity(intent);*/
			}
			});
			ImageView emailBtn = (ImageView) dialog
			.findViewById(R.id.emailBtn);
			emailBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);*/
				}
			});
			/*ImageView msgBtn = (ImageView) dialog
			.findViewById(R.id.msgBtn);
			msgBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);
				}
			});*/
			ImageView dialogCancel = (ImageView) dialog
					.findViewById(R.id.dialogCancel);
			// if button is clicked, close the custom dialog
			dialogCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
			
		}
	}
	
	
	class MyThread implements Runnable {
	    private boolean isPaused = false;
	    
	    @Override
	    public void run() {
	    	//flag = true;
	     
	        try {
	            while (flag) {
	            	Thread.sleep(1000);
	            	
	            	 if(!isPaused){	
	            		 /*prevImageView.animate().alpha(0).withLayer();
	            		 nextImageView.animate().alpha(1).withLayer();*/
                   mHandler.post(new Runnable() {    // withEndAction

                        @Override
                        public void run() {
                        	/*mCurrentDrawable = (mCurrentDrawable + 1) % drawables.length;
                        	int nextDrawableIndex = (mCurrentDrawable + 1) % drawables.length;
                        	prevImageView.setImageDrawable(drawables[mCurrentDrawable]);
                        	nextImageView.setImageDrawable(drawables[nextDrawableIndex]);
                        	nextImageView.setAlpha(0f);
                        	prevImageView.setAlpha(1f);*/
                        	//String index = separated[0];
                         if(count_img_play < separated.length){	
                        	 //Log.i("count_img_play", count_img_play+"");
                        	
                        	BitmapFactory.Options bfo = new BitmapFactory.Options();  
                        	//bfo.inJustDecodeBounds = true;
                		    bfo.inSampleSize = Utils.calculateInSize(bfo, imgWidth, imgHeight);
                		    Log.i("SampleSize",bfo.inSampleSize+"");
                		    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[count_img_play],bfo); 
                		    //bfo.inJustDecodeBounds = false;
                		    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
                		    /*prevImageView.setImageDrawable(drawables[count_img_play]);
                		    nextImageView.setImageDrawable(drawables[count_img_play+1]);*/
                		    if (switcher.getDisplayedChild() == 0) {
                		    	nextImageView.setImageDrawable(drawableImage);
                                switcher.showNext();
                            } else {
                            	prevImageView.setImageDrawable(drawableImage);
                                switcher.showPrevious();
                            }
                		    
                         }
                         count_img_play++;
                        }
                        
                    });
	            	
	            	 }
	                if (isPaused) {
	                    synchronized (this) {
	                        // wait for resume() to be called
	                        wait();
	                        isPaused = false;
	                        //start_chk = true;
	                        
	                    }
	                }
	            }
	        } catch (Exception ex) {
	            Log.getStackTraceString(ex);
	        }
	        
	      
	    }
	    
	    private void withEndAction(Runnable runnable) {
			// TODO Auto-generated method stub
			
		}

		public void start() {
	    	flag = true;
	    }
	    
	    public void stop() {
	    	flag = false;
	    }

	    public void pause() {
	    	isPaused = true;
	    	//start_chk = true;
	    }

	    public synchronized void resume() {
	    	
	        notify();
	    }
	}
	
	 public static Bitmap captureViewToBitmap(View view)
	  {
	    Bitmap result = null;

	    try
	    {
	      result = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
	      view.draw(new Canvas(result));
	    }
	    catch(Exception e)
	    {
	      //Logger.e(e.toString());
	    }
	    
	    return result;
	  }
	 public static int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
	

}

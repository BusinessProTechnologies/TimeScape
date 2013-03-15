package com.bpd.smilemorph;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class PlayMorphActivity extends Activity implements OnClickListener{

	private Handler mHandler;
	private String imageString;
	private String[] separated;
	ImageView closeView,playView,pouseView,shareView,image,image_nxt;
	final Context context = this;
	private Integer count_img_play=0;
	Thread newThread;
	MyThread play_img_thread;
	Boolean flag,start_chk;
	
	ViewSwitcher switcher;
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
		separated = imageString.replace("|", ",").split(",");
		
		image = (ImageView) findViewById(R.id.image);
		image_nxt = (ImageView) findViewById(R.id.image1);
		switcher = (ViewSwitcher) findViewById(R.id.switcher);
		
		closeView = (ImageView) findViewById(R.id.closeBtn);
		closeView.setOnClickListener(this);
		playView = (ImageView) findViewById(R.id.playBtn);
		playView.setOnClickListener(this);
		pouseView = (ImageView) findViewById(R.id.pouseBtn);
		pouseView.setOnClickListener(this);
		shareView = (ImageView) findViewById(R.id.shareBtn);
		shareView.setOnClickListener(this);
		
		
		BitmapFactory.Options bfo = new BitmapFactory.Options();  
	    bfo.inSampleSize = 8;   
	    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[count_img_play],bfo); 
	    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
	    Log.i("drawableImage", drawableImage+"");
	    image.setImageDrawable(drawableImage);
	    count_img_play++;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.closeBtn){
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

					/*Intent intent = new Intent(CreateMorphActivity.this,
							MultiPhotoSelectActivity.class);
					//Log.i("morphname", _morphName);
					intent.putExtra("morphName", projectName);
					startActivity(intent);*/
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
			ImageView msgBtn = (ImageView) dialog
			.findViewById(R.id.msgBtn);
			msgBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);*/
				}
			});
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
	/*private void startAnimation() {
		
		play_img_thread = new Thread(new Runnable() {
	        @Override
	        public void run() {
	            // TODO Auto-generated method stub
	            while (flag) {
	                try {
	                    Thread.sleep(1000);
	                    mHandler.post(new Runnable() {

	                        @Override
	                        public void run() {
	                         if(count_img_play < separated.length){	
	                        	 overridePendingTransition(R.anim.fadein, R.anim.fadeout); 
	                        	
	                        	BitmapFactory.Options bfo = new BitmapFactory.Options();  
	                		    bfo.inSampleSize = 8;   
	                		    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[count_img_play],bfo); 

	                		    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
	                		    Log.i("drawableImage", separated[count_img_play]);
	                		    image.setImageDrawable(drawableImage);
	                		    
	                		    
	                         }
	                         count_img_play++; //count_img_play = ++count_img_play % separated.length;
	                        }
	                        
	                    });
	                } catch (Exception e) {
	                    // TODO: handle exception
	                }
	            }
	        }
	    });
		//count_img_play = ++count_img_play % separated.length;
		play_img_thread.start();
	}*/
	
	class MyThread implements Runnable {
	    //private boolean keepRunning = false;
	    private boolean isPaused = false;
	    
	    @Override
	    public void run() {
	    	//flag = true;
	     
	        try {
	            while (flag) {
	            	Thread.sleep(1500);
	            	
	            	 if(!isPaused){	
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                         if(count_img_play < separated.length){	
                        	 Log.i("count_img_play", count_img_play+"");
                        	
                        	BitmapFactory.Options bfo = new BitmapFactory.Options();  
                		    bfo.inSampleSize = 8;   
                		    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[count_img_play],bfo); 

                		    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
                		    Log.i("drawableImage", separated[count_img_play]);
                		    //image_nxt.setImageDrawable(drawableImage);
                		    Log.i("position", switcher.getDisplayedChild()+"");
                		    //ImageViewAnimatedChange(context,image,drawableImage);
                		    //switcher.showNext();
                		    
                		    if (switcher.getDisplayedChild() == 0) {
                		    	image_nxt.setImageDrawable(drawableImage);
                                switcher.showNext();
                            } else {
                            	image.setImageDrawable(drawableImage);
                                switcher.showPrevious();
                            }
                		    
                		    
                		    Log.i("position11", switcher.getDisplayedChild()+"");
                		    
                		    
                         }
                         count_img_play++; //count_img_play = ++count_img_play % separated.length;
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
	
	
	/*public static void ImageViewAnimatedChange(Context c, final ImageView v, final Drawable new_image) {
	    final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out); 
	    final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in); 
	    anim_out.setAnimationListener(new AnimationListener()
	    {
	        @Override public void onAnimationStart(Animation animation) {}
	        @Override public void onAnimationRepeat(Animation animation) {}
	        @Override public void onAnimationEnd(Animation animation)
	        {
	            v.setImageDrawable(new_image); 
	            anim_in.setAnimationListener(new AnimationListener() {
	                @Override public void onAnimationStart(Animation animation) {}
	                @Override public void onAnimationRepeat(Animation animation) {}
	                @Override public void onAnimationEnd(Animation animation) {}
	            });
	            v.startAnimation(anim_in);
	        }
	    });
	    v.startAnimation(anim_out);
	}*/

}

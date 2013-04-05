package com.bpd.smilemorph;

import static com.googlecode.javacv.cpp.avcodec.AV_CODEC_ID_MPEG4;
import static com.googlecode.javacv.cpp.avutil.AV_PIX_FMT_YUV420P;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.File;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.bpd.smilemorph.PlayMorphActivity.MyThread;
import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class createVideoActivity extends Activity {

	private String imageString,projectName;
	private String[] separated;
	opencv_core.IplImage[] img;
	opencv_core.IplImage imgIpl;
	private Integer count_img_play;
	private Handler mHandler;
	private Boolean flag;
	private FFmpegFrameRecorder recorder;
	private ViewSwitcher switcher;
	private ImageView image,image_nxt;
	private File projPath;
	ByteBuffer[] buffer;
	private static final int COLOUR_DEPTH = IPL_DEPTH_8U;
	private static final int COLOUR_CHANNELS = 4;
	Bitmap bMap;
	Thread newThread;
	MyThread play_img_thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordvideo);
        
        image = (ImageView) findViewById(R.id.image);
		image_nxt = (ImageView) findViewById(R.id.image1);
		switcher = (ViewSwitcher) findViewById(R.id.switcher);
		
        Bundle extras = getIntent().getExtras();
		imageString = extras.getString("imageString");
		Log.i("imageString",imageString);
		projectName = extras.getString("projectName");
		Log.i("projectName",projectName);
		
		separated = imageString.replace("|", ",").split(",");
		
		//imgIpl = IplImage.create(200, 150, 10, 4);
		img = new opencv_core.IplImage[separated.length];
		for(int i=0;i<separated.length;i++){
			Log.i("separated[i]",separated[i]);
        //opencv_core.IplImage img = cvLoadImage("/mnt/sdcard/SmileMorph/jjj/IMG001.jpg");
		  img[i] = cvLoadImage(separated[i]);
		}
		projPath = new File(Environment.getExternalStorageDirectory()
				+ "/SmileMorph/" + projectName);

		int ht;
		int wt;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		ht = displaymetrics.heightPixels;
		wt = displaymetrics.widthPixels;
        recorder = new FFmpegFrameRecorder(projPath +"/video.mp4",wt,ht);
        flag = false;
        count_img_play=0;
        play_img_thread = new MyThread();
		newThread = new Thread(play_img_thread);
		newThread.start();
        play_img_thread.start();
        mHandler =new Handler();
        
		
            /*try {
               //recorder.setCodecID(AV_CODEc);
               recorder.setAudioCodec(AV_CODEC_ID_MPEG4);
               recorder.setFormat("mp4");
               recorder.setFrameRate(1);
               recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
               recorder.start();
               
               for (int i=0;i < separated.length;i++)
               {
            	   
            	  overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                  recorder.record(img[i]);
               }
               
               recorder.stop();
            }catch (Exception e){
               e.printStackTrace();
            }*/
    }
   
    class MyThread implements Runnable {
	    //private boolean keepRunning = false;
	    private boolean isPaused = false;
	    
	    @Override
	    public void run() {
	    	//flag = true;
	    	Log.i("switcher",switcher+" "+flag+" "+isPaused+" "+" "+count_img_play+" "+separated.length);
	        try {
	        	recorder.setFormat("mp4");
                recorder.setFrameRate(1);
                recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
                recorder.start();
	            while (flag) {
	            	Thread.sleep(1000);
            		 //try {
	            	 recorder.setAudioCodec(AV_CODEC_ID_MPEG4);
	                 
	                 mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                         for (int count_img_play=0;count_img_play < separated.length;count_img_play++){
                        	 
                    // if(count_img_play < separated.length){	
                    	 //Log.i("count_img_play", count_img_play+"");
                       //overridePendingTransition(R.anim.fadein,R.anim.fadeout);	 
                        	
                    	 Log.i("switcher 2",switcher+" "+flag+" "+isPaused+" "+" "+count_img_play+" "+separated.length);
                    	BitmapFactory.Options bfo = new BitmapFactory.Options();  
            		    bfo.inSampleSize = 8;   
            		    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[count_img_play],bfo); 
            		    Log.i("switcher 3",switcher+" "+flag+" "+isPaused+" "+" "+count_img_play+" "+separated.length);
            		    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
            		    Log.i("switcher 4",switcher+" "+flag+" "+isPaused+" "+" "+count_img_play+" "+separated.length);
            		    final Drawable imgg = drawableImage;
            		    switcher.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (switcher.getDisplayedChild() == 0) {
	                		    	image_nxt.setImageDrawable(imgg);
	                                switcher.showNext();
	                            } else {
	                            	image.setImageDrawable(imgg);
	                                switcher.showPrevious();
	                            }
							}
						});
            		    
            		    Log.i("switcher",switcher+"");
            		    //switcher.getCurrentView()
            		    bMap = captureViewToBitmap(switcher);
               		    Log.i("bMap",bMap+"");
               		    img[count_img_play] = bitmapToIplImage(bMap);
               		    Log.i("img[i]",count_img_play+" "+img[count_img_play]+"");
						try {
								recorder.record(img[count_img_play]);
							} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                     }
                         try {
							recorder.stop();
						} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
                     //count_img_play++;  
                    }
                    
                });
            		 /*} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
							//e.printStackTrace();
							Log.i("IPLIMAGE","ERROR");
					}*/
	            }//flag
	        } catch (Exception ex) {
	            Log.getStackTraceString(ex);
	        }
	    }
	    
	    public void start() {
	    	flag = true;
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
    public IplImage bitmapToIplImage(Bitmap bitmap) {
		//IplImage image = IplImage.create(WIDTH, HEIGHT, COLOUR_DEPTH, COLOUR_CHANNELS);

		IplImage image = IplImage.create(bitmap.getWidth(), bitmap.getHeight(), COLOUR_DEPTH, COLOUR_CHANNELS);
		bitmap.copyPixelsToBuffer(image.getByteBuffer());
		return image;
    }
}

package com.bpd.smilemorph;

import static com.googlecode.javacv.cpp.avcodec.AV_CODEC_ID_MPEG4;
import static com.googlecode.javacv.cpp.avutil.AV_PIX_FMT_YUV420P;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_16U;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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

import com.bpd.utils.Utils;
import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class createVideoActivity extends Activity {

	private String imageString, projectName;
	private String[] separated;
	opencv_core.IplImage[] img;
	opencv_core.IplImage imgIpl;
	private Integer count_img_play;
	private Handler mHandler;
	private Boolean flag;
	private FFmpegFrameRecorder recorder;
	private ViewSwitcher switcher;
	private ImageView image, image_nxt;
	private File projPath;
	ByteBuffer[] buffer;
	private static final int COLOUR_DEPTH = IPL_DEPTH_8U;
	private static final int COLOUR_CHANNELS = 3;
	Bitmap bMap;
	ArrayList<Bitmap> bitmapArray;
	IplImage _iplimage;
	Bitmap _bitmapScaled;
	FileOutputStream fo;
	Thread newThread;
	MyThread play_img_thread;
	int ht,wt;
	DisplayMetrics displaymetrics;

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
		Log.i("imageString", imageString);
		projectName = extras.getString("projectName");
		Log.i("projectName", projectName);
		bitmapArray = new ArrayList<Bitmap>();
		separated = imageString.replace("|", ",").split(",");

		projPath = new File(Environment.getExternalStorageDirectory()
				+ "/SmileMorph/" + projectName);

		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		ht = displaymetrics.heightPixels;
		wt = displaymetrics.widthPixels;
		Log.i("wth+height",wt+"-"+ht);
		recorder = new FFmpegFrameRecorder(projPath + "/video.mp4", wt, ht);
		flag = false;
		count_img_play = 0;
		play_img_thread = new MyThread();
		newThread = new Thread(play_img_thread);
		newThread.start();
		play_img_thread.start();
		mHandler = new Handler();
	}

	class MyThread implements Runnable {
		private boolean isPaused = false;

		@Override
		public void run() {
			try {
				recorder.setFormat("mp4");
				recorder.setFrameRate(3);
				recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
				recorder.setAudioCodec(AV_CODEC_ID_MPEG4);
				recorder.start();
				time = 0;
				while (flag) {
					Thread.sleep(1000 / fps);
					
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							time++;
							if((time % fps) == 0) {
								if (count_img_play < separated.length) {
									Log.i("abc", "1 - >"+time+"");
									BitmapFactory.Options bfo = new BitmapFactory.Options();
									//bfo.inJustDecodeBounds = true;
									bfo.inSampleSize = Utils.calculateInSize(bfo, wt, ht);//8;
									Bitmap ThumbImage = BitmapFactory.decodeFile(separated[count_img_play], bfo);
									Drawable drawableImage = new BitmapDrawable(getResources(), ThumbImage);
									final Drawable imgg = drawableImage;
									switcher.post(new Runnable() {
	
										@Override
										public void run() {
											if (switcher.getDisplayedChild() == 0) {
												image_nxt.setImageDrawable(imgg);
												switcher.showNext();
											} else {
												image.setImageDrawable(imgg);
												switcher.showPrevious();
											}
										}
									});
								}else{
									flag = false;
									Log.i("abc", "3 - >"+time+"");
								}
								count_img_play++;
							}
							Log.i("abc", "2 - >"+time+"");
							if((time % (1000 / 40)) == 0){
								Log.i("abc", "2");
								capturedBitmap = captureViewToBitmap(switcher);
								iplImage = bitmapToIplImage(capturedBitmap);
								try {
									recorder.record(iplImage);
								} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
									e.printStackTrace();
								}
							}
						}

					});
				}
				try {
					recorder.stop();
				} catch (com.googlecode.javacv.FrameRecorder.Exception e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				Log.getStackTraceString(ex);
			}
		}

		public void start() {
			flag = true;
		}
	}
	
	int time = 0;
	Bitmap capturedBitmap;
	opencv_core.IplImage iplImage;
	int fps = 25;
	
	public static Bitmap captureViewToBitmap(View view) {
		Bitmap result = null;
		
		try {
			result = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
					Bitmap.Config.RGB_565);

			view.draw(new Canvas(result));

		} catch (Exception e) {
			
		}

		return result;
	}

	public IplImage bitmapToIplImage(Bitmap bitmap) {
		ht = displaymetrics.heightPixels;
		wt = displaymetrics.widthPixels;
		try
		{
			_iplimage = IplImage.create(wt, ht, COLOUR_DEPTH, COLOUR_CHANNELS);
			//cvCvtColor(iplImage, _iplimage, CV_BGR2HSV);
			bitmap.copyPixelsToBuffer(_iplimage.getByteBuffer());
			//bitmap.copyPixelsFromBuffer(_iplimage.getByteBuffer());
		//return _iplimage;
		}catch (Exception e){
			   //this is the line of code that sends a real error message to the log.
			   Log.e("ERROR", "ERROR in Code: " + e.toString());
			   e.printStackTrace();
		}
		return _iplimage;
	}
}

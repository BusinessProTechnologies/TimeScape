package com.bpd.smilemorph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CapturePhotoActivity extends Activity implements SurfaceHolder.Callback, OnClickListener{

	SurfaceHolder mSurfaceHolder;
	SurfaceView mSurfaceView;
	public Camera mCamera;
	boolean mPreviewRunning;
	ImageView _tgglCmra,_closeCmra,_cpturCmra;
	private static final int CAMERA_REQUEST = 1888;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private ImageView mImageView;
	private Bitmap mImageBitmap;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.capture_photo);
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		_tgglCmra = (ImageView) findViewById(R.id.tgglCmra);
		_closeCmra = (ImageView)findViewById(R.id.closeCmra);
		_closeCmra.setOnClickListener(this);
		_cpturCmra = (ImageView)findViewById(R.id.cpturCmra);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		

		Bundle extras = getIntent().getExtras();
		String morphName = extras.getString("morphName");
		Log.i("MorphNmae", "21= " + morphName);
		//mImageView = (ImageView) findViewById(R.id.imageView1);
		mImageBitmap = null;

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		if (mPreviewRunning) { 
			mCamera.stopPreview();
			}
			Camera.Parameters p = mCamera.getParameters();
			//p.setPreviewSize(w, h);
			mCamera.setParameters(p);
			try { 
			mCamera.setPreviewDisplay(arg0); 
			} catch (IOException e) { 
			e.printStackTrace();
			 }  mCamera.startPreview();
			mPreviewRunning = true; 
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		int  id=findFrontFacingCamera(); 
		Log.i("msg", id+"");
		mCamera = Camera.open(id);
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release(); 
	}

	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {
		 } 
		};
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			mImageBitmap = (Bitmap) data.getExtras().get("data");
			// myOrientationEventListener.disable();
			Intent i = new Intent(CapturePhotoActivity.this,
					ImagePostingActivity.class);
			i.putExtra("imageposting", mImageBitmap);
			startActivity(i);
			finish();

		}
	}

	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY,
				(mImageBitmap != null));
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView
				.setVisibility(savedInstanceState
						.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? ImageView.VISIBLE
						: ImageView.INVISIBLE);

	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	private int findFrontFacingCamera() {
	     int idCamera=0;
	        // Look for front-facing camera, using the Gingerbread API.
	        // Java reflection is used for backwards compatibility with pre-Gingerbread APIs.
	        try {
	            Class<?> cameraClass = Class.forName("android.hardware.Camera");
	            Object cameraInfo = null;
	            Field field = null;
	            int cameraCount = 0;
	            Method getNumberOfCamerasMethod = cameraClass.getMethod( "getNumberOfCameras" );
	            if ( getNumberOfCamerasMethod != null ) {
	                cameraCount = (Integer) getNumberOfCamerasMethod.invoke( null, (Object[]) null );
	            }
	            Class<?> cameraInfoClass = Class.forName("android.hardware.Camera$CameraInfo");
	            if ( cameraInfoClass != null ) {
	                cameraInfo = cameraInfoClass.newInstance();
	            }
	            if ( cameraInfo != null ) {
	                field = cameraInfo.getClass().getField( "facing" );
	            }
	            Method getCameraInfoMethod = cameraClass.getMethod( "getCameraInfo", Integer.TYPE, cameraInfoClass );
	            if ( getCameraInfoMethod != null && cameraInfoClass != null && field != null ) {
	                for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
	                    getCameraInfoMethod.invoke( null, camIdx, cameraInfo );
	                    int facing = field.getInt( cameraInfo );
	                    if ( facing == 1 ) { // Camera.CameraInfo.CAMERA_FACING_FRONT
	                        try {
	                            Method cameraOpenMethod = cameraClass.getMethod( "open", Integer.TYPE );
	                            if ( cameraOpenMethod != null ) {
	                                Log.d("TestLedActivity","Id frontale trovato: "+camIdx);
	                                //camera = (Camera) cameraOpenMethod.invoke( null, camIdx );
	                                idCamera=camIdx;
	                            }
	                        } catch (RuntimeException e) {
	                            Log.e("TestLedActivity", "Camera failed to open: " + e.getLocalizedMessage());
	                        }
	                    }
	                }
	            }
	        }
	        // Ignore the bevy of checked exceptions the Java Reflection API throws - if it fails, who cares.
	        catch ( ClassNotFoundException e        ) {Log.e("TestLedActivity", "ClassNotFoundException" + e.getLocalizedMessage());}
	        catch ( NoSuchMethodException e         ) {Log.e("TestLedActivity", "NoSuchMethodException" + e.getLocalizedMessage());}
	        catch ( NoSuchFieldException e          ) {Log.e("TestLedActivity", "NoSuchFieldException" + e.getLocalizedMessage());}
	        catch ( IllegalAccessException e        ) {Log.e("TestLedActivity", "IllegalAccessException" + e.getLocalizedMessage());}
	        catch ( InvocationTargetException e     ) {Log.e("TestLedActivity", "InvocationTargetException" + e.getLocalizedMessage());}
	        catch ( InstantiationException e        ) {Log.e("TestLedActivity", "InstantiationException" + e.getLocalizedMessage());}
	        catch ( SecurityException e             ) {Log.e("TestLedActivity", "SecurityException" + e.getLocalizedMessage());}

	        if ( mCamera == null ) {
	            Log.d("TestLedActivity","Devo aprire la camera dietro");
	            // Try using the pre-Gingerbread APIs to open the camera.
	            idCamera=0;
	        }

	        return idCamera;
	    }
	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.closeCmra) {
			finish();
		}
		
	}

}
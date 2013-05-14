package com.bpd.smilemorph;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.bpd.smilemorph.R;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CapturePhotoActivity extends Activity implements OnClickListener {
	
	SurfaceHolder mSurfaceHolder;
	SurfaceView mSurfaceView;
	public Camera mCamera;
	boolean mPreviewRunning;
	ImageView _tgglCmra, _closeCmra, _cpturCmra, surface_layout;
	int update, _camId, layno;
	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private ImageView mImageView;
	private Bitmap mImageBitmap;
	private String morphName;
	OutputStream outputStream;
	private CameraPreview _cameraPreview;
	FrameLayout _frameLayoutPreview;
	boolean _isPreviewRunning;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture_photo);
		// mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		_frameLayoutPreview = (FrameLayout) findViewById(R.id.camera_preview);
		_frameLayoutPreview.setForegroundGravity(Gravity.CENTER);

		_camId = 0;

		_cameraPreview = new CameraPreview(CapturePhotoActivity.this);

		if (_frameLayoutPreview.getChildCount() > 0) {
			_frameLayoutPreview.removeAllViews();
		}
		_frameLayoutPreview.addView(_cameraPreview);

		_tgglCmra = (ImageView) findViewById(R.id.tgglCmra);
		_tgglCmra.setOnClickListener(this);
		_closeCmra = (ImageView) findViewById(R.id.closeCmra);
		_closeCmra.setOnClickListener(this);
		_cpturCmra = (ImageView) findViewById(R.id.cpturCmra);
		_cpturCmra.setOnClickListener(this);
		surface_layout = (ImageView) findViewById(R.id.surface_layout);

		Bundle extras = getIntent().getExtras();
		morphName = extras.getString("morphName");
		Log.i("MorphNmae", "21= " + morphName);
		update = extras.getInt("update");
		Log.i("update", "12= " + update);
		mImageBitmap = null;
		if (update == 0) {
			layno = extras.getInt("layoutNo");
			switch (layno) {
			case 1:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.mouth));
				break;
			case 2:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.profile_shoulder_front));
				break;
			case 3:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.profile_upper_side));
				break;
			case 4:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.profile_middle_side));
				break;
			case 5:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.profile_full_front));
				break;
			case 6:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.profile_upper_front));
				break;
			case 7:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.profile_full_side));
				break;
			case 8:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.profile_lower_back));
				break;
			case 9:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.profile_middle_front));
				break;

			default:
				surface_layout.setImageDrawable(getResources().getDrawable(
						R.drawable.mouth));
			}
		}
	}

	class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
		private SurfaceHolder mSurfaceHolder;

		public CameraPreview(Context context) {
			super(context);
			this.mSurfaceHolder = this.getHolder();
			this.mSurfaceHolder.addCallback(this);
			this.mSurfaceHolder
					.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera = Camera.open(_camId);
			} catch (Exception e) {
				finish();
				return;
			}

			// _camera.setDisplayOrientation(90);

			try {
				mCamera.setPreviewDisplay(mSurfaceHolder);
			} catch (Throwable ignored) {

			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			if (_isPreviewRunning) {
				mCamera.stopPreview();
			}
			try {
				mCamera.startPreview();
			} catch (Exception e) {

			}
			_isPreviewRunning = true;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			if (_isPreviewRunning && mCamera != null) {
				if (mCamera != null) {
					mCamera.stopPreview();
					mCamera.release();
					mCamera = null;
				}
				_isPreviewRunning = false;
			}
		}
	}

	

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.closeCmra) {
			finish();
		} else if (v.getId() == R.id.cpturCmra) {
			mCamera.takePicture(myShutterCallback, myPictureCallback_RAW,
					myPictureCallback_JPG);
		} else if (v.getId() == R.id.tgglCmra) {
			if (Camera.getNumberOfCameras() > 1) {
				if (_camId == 0) {
					_camId = 1;
					_cameraPreview = new CameraPreview(
							CapturePhotoActivity.this);
					if (_frameLayoutPreview.getChildCount() > 0) {
						_frameLayoutPreview.removeAllViews();
					}
					_frameLayoutPreview.addView(_cameraPreview);
				} else if (_camId == 1) {
					_camId = 0;
					_cameraPreview = new CameraPreview(
							CapturePhotoActivity.this);
					if (_frameLayoutPreview.getChildCount() > 0) {
						_frameLayoutPreview.removeAllViews();
					}
					_frameLayoutPreview.addView(_cameraPreview);
				}
			} else {
				Toast.makeText(CapturePhotoActivity.this,
						"You have only one camera present!", Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	ShutterCallback myShutterCallback = new ShutterCallback() {

		@Override
		public void onShutter() {
		}
	};

	PictureCallback myPictureCallback_RAW = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub

		}
	};
	PictureCallback myPictureCallback_JPG = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// BitmapFactory.Options opts = new BitmapFactory.Options();
			outputStream = new ByteArrayOutputStream();
			Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0,
					arg0.length);
			// outputStream = new FileOutputStream(file);
			bitmapPicture
					.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
			byte[] byteArray = ((ByteArrayOutputStream) outputStream)
					.toByteArray();
			// bitmapPicture = Bitmap.createScaledBitmap(bitmapPicture, 480,
			// 480, false);
			Log.i("i m here",byteArray+"");
			Intent intent = new Intent(CapturePhotoActivity.this,
					ImagePostingActivity.class);
			intent.putExtra("imageposting", byteArray);
			intent.putExtra("morphName", morphName);
			intent.putExtra("update", update);
			finish();
			startActivity(intent);
			
		}
	};

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

	

}
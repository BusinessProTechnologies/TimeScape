package com.bpd.smilemorph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class PhotoIntentActivity extends Activity {

	private static final int CAMERA_REQUEST = 1888;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private ImageView mImageView;
	private Bitmap mImageBitmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture_photo);

		Bundle extras = getIntent().getExtras();
		String morphName = extras.getString("morphName");
		Log.i("MorphNmae", "21= " + morphName);
		mImageView = (ImageView) findViewById(R.id.imageView1);
		mImageBitmap = null;

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			mImageBitmap = (Bitmap) data.getExtras().get("data");
			// myOrientationEventListener.disable();
			Intent i = new Intent(PhotoIntentActivity.this,
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

}
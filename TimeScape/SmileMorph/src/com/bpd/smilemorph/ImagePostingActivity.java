package com.bpd.smilemorph;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class ImagePostingActivity extends Activity {
	ImageView imageView;
	Bitmap bmp_imgposting;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_photo);
		imageView = (ImageView) findViewById(R.id.showPhoto);
		Bundle ex_intent = getIntent().getExtras();
        bmp_imgposting = ex_intent.getParcelable("imageposting");
        
        imageView.setImageBitmap(bmp_imgposting);
	}

}

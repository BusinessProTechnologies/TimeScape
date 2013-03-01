package com.bpd.smilemorph;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	TextView imgView1,imgView2,imgView3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		File nfile=new File(Environment.getExternalStorageDirectory()+"/SmileMorph");
	    nfile.mkdir();
	    Log.i("DIR name=",nfile+"");
		imgView1 = (TextView) findViewById(R.id.imgView_1);
		imgView1.setOnClickListener(this);
		imgView2 = (TextView) findViewById(R.id.imgView_2);
		imgView2.setOnClickListener(this);
		/*imgView3 = (TextView) findViewById(R.id.imgView_3);
		imgView3.setOnClickListener(this);*/
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.imgView_1) {
			Intent intent = new Intent(MainActivity.this, CreateMorphActivity.class);
			startActivity(intent);
		}else if(v.getId() == R.id.imgView_2) {
			Intent intent = new Intent(MainActivity.this, ExistingMorphActivity.class);
			startActivity(intent);
		}/*else if(v.getId() == R.id.imgView_3) {
			Intent intent = new Intent(MainActivity.this, MultiPhotoSelectActivity.class);
			startActivity(intent);
		}*/
	}


}

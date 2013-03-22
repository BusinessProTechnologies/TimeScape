package com.bpd.smilemorph;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateMorphActivity extends Activity implements OnClickListener {
	TextView addMorph;
	ImageView imgView1, imgView2,imgView3, imgView4,imgView5, imgView6,imgView7, imgView8,imgView9,_cancel;
	String _morphName;
	String projectName;
	final Context context = this;
	AlertDialog.Builder builder;
	byte[] byteArray = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createmorph);

		/*
		 * addMorph = (TextView)findViewById(R.id.newMorph);
		 * addMorph.setOnClickListener(this);
		 */
		
		imgView1 = (ImageView) findViewById(R.id.smileView1);
		imgView1.setTag(1);
		imgView1.setOnClickListener(this);
		imgView2 = (ImageView) findViewById(R.id.smileView2);
		imgView2.setTag(2);
		imgView2.setOnClickListener(this);
		imgView3 = (ImageView) findViewById(R.id.smileView3);
		imgView3.setTag(3);
		imgView3.setOnClickListener(this);
		imgView4 = (ImageView) findViewById(R.id.smileView4);
		imgView4.setTag(4);
		imgView4.setOnClickListener(this);
		imgView5 = (ImageView) findViewById(R.id.smileView5);
		imgView5.setTag(5);
		imgView5.setOnClickListener(this);
		imgView6 = (ImageView) findViewById(R.id.smileView6);
		imgView6.setTag(6);
		imgView6.setOnClickListener(this);
		imgView7 = (ImageView) findViewById(R.id.smileView7);
		imgView7.setTag(7);
		imgView7.setOnClickListener(this);
		imgView8 = (ImageView) findViewById(R.id.smileView8);
		imgView8.setTag(8);
		imgView8.setOnClickListener(this);
		imgView9 = (ImageView) findViewById(R.id.smileView9);
		imgView9.setTag(9);
		imgView9.setOnClickListener(this);
		
		_cancel = (ImageView) findViewById(R.id.cancel);
		_cancel.setOnClickListener(this);
		/*
		 * imgView2 = (ImageView) findViewById(R.id.smileView2);
		 * imgView2.setOnClickListener(this);
		 */

		//showalertdialog(0);
		Bundle extras = getIntent().getExtras();
		projectName = extras.getString("projectName");

	}

	private void showalertdialog(int check) {

		builder = new AlertDialog.Builder(this);
		builder.setTitle("Name Your Project");

		if (check == 1) {
			builder.setMessage(R.string.input_empty);
		}
		// builder.setMessage("Add item name");

		final EditText savedText = new EditText(this);
		builder.setView(savedText);
		// builder.setIcon(0);
		builder.setCancelable(false);
		builder.setPositiveButton("Save", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				_morphName = savedText.getText().toString().trim();

				if (_morphName.length() == 0) {
					// Toast.makeText(CreateMorphActivity.this,
					// R.string.input_empty, Toast.LENGTH_SHORT).show();
					showalertdialog(1);
				} else {
					_morphName = savedText.getText().toString().trim();
				}

			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						finish();
						// dialog.cancel();
					}
				});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	
	@Override
	public void onClick(View v) {
		/*
		 * editTxt = (EditText) findViewById(R.id.morphName); _morphName =
		 * editTxt.getText().toString();
		 */
		if (v.getId() == R.id.smileView1 || v.getId() == R.id.smileView2 
				|| v.getId() == R.id.smileView3 || v.getId() == R.id.smileView4 ||
				v.getId() == R.id.smileView5 || v.getId() == R.id.smileView6 || 
				v.getId() == R.id.smileView7 || v.getId() == R.id.smileView8 || 
				v.getId() == R.id.smileView9) {
			Log.i("id",v.getTag()+"");
			launchDialog((Integer) v.getTag());
		}else if(v.getId() == R.id.cancel){
			finish();
		}
	}
	
	public void launchDialog(int id) {
		Log.i("id",id+"");
		final int layid = id;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_add_morph);
		// dialog.setTitle("Title...");

		ImageView addFrmGallery = (ImageView) dialog
				.findViewById(R.id.addFrmGallery);
		addFrmGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(CreateMorphActivity.this,
						MultiPhotoSelectActivity.class);
				//Log.i("morphname", _morphName);
				intent.putExtra("morphName", projectName);
				intent.putExtra("update", 0);
				dialog.dismiss();
				finish();
				startActivity(intent);
			}
		});
		ImageView addFrmCamera = (ImageView) dialog
				.findViewById(R.id.addFrmCamera);
		addFrmCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(CreateMorphActivity.this,
						CapturePhotoActivity.class);
				intent.putExtra("morphName", projectName);
				intent.putExtra("layoutNo", layid);
				//intent.putExtra("byteArray", byteArray);
				intent.putExtra("update", 0);
				dialog.dismiss();
				finish();
				startActivity(intent);
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
		/*if(byteArray != null){
			Intent intent = new Intent(CreateMorphActivity.this,
					ImagePostingActivity.class);
			intent.putExtra("imageposting", byteArray);
			intent.putExtra("morphName", projectName);
			intent.putExtra("update", 0);
			startActivity(intent);
		}*/
		dialog.show();
	}

}

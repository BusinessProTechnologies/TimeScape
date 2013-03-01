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
	ImageView imgView1, imgView2,_cancel;
	String _morphName;
	final Context context = this;
	AlertDialog.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createmorph);

		/*
		 * addMorph = (TextView)findViewById(R.id.newMorph);
		 * addMorph.setOnClickListener(this);
		 */

		imgView1 = (ImageView) findViewById(R.id.smileView1);
		imgView1.setOnClickListener(this);
		_cancel = (ImageView) findViewById(R.id.cancel);
		_cancel.setOnClickListener(this);
		/*
		 * imgView2 = (ImageView) findViewById(R.id.smileView2);
		 * imgView2.setOnClickListener(this);
		 */

		showalertdialog(0);

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

	/*
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 */

	@Override
	public void onClick(View v) {
		/*
		 * editTxt = (EditText) findViewById(R.id.morphName); _morphName =
		 * editTxt.getText().toString();
		 */
		if (v.getId() == R.id.smileView1) {
			// custom dialog
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_add_morph);
			// dialog.setTitle("Title...");

			Button addFrmGallery = (Button) dialog
					.findViewById(R.id.addFrmGallery);
			addFrmGallery.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(CreateMorphActivity.this,
							MultiPhotoSelectActivity.class);
					Log.i("morphname", _morphName);
					intent.putExtra("morphName", _morphName);
					startActivity(intent);
				}
			});
			Button addFrmCamera = (Button) dialog
					.findViewById(R.id.addFrmCamera);
			addFrmCamera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", _morphName);
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

			dialog.show();
		}else if(v.getId() == R.id.cancel){
			finish();
		}
	}

}

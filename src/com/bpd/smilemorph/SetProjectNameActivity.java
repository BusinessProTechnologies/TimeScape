package com.bpd.smilemorph;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bpd.database.DatabaseHandler;

public class SetProjectNameActivity extends Activity implements OnClickListener {
	ImageView nextBtn,cancelBtn;
	EditText projectName;
	String projName;
	AlertDialog.Builder builder;
	private DatabaseHandler myDbHelper;
	SQLiteDatabase db;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createproject);
		projectName = (EditText)findViewById(R.id.projectName);
		nextBtn = (ImageView) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
		cancelBtn = (ImageView) findViewById(R.id.cancelProjName);
		cancelBtn.setOnClickListener(this);
		
		/****start in app pruchase*******/
		/*myDbHelper = new DatabaseHandler(this);
		myDbHelper.initializeDataBase();
		db = myDbHelper.getWritableDatabase();
		projName = projectName.getText().toString(); 
		Cursor cursor = db.rawQuery("SELECT * FROM " + ProjectEntity.TABLE_NAME + ";", null);
		
		if(cursor.moveToFirst()){ 
			
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_inapp_purchase);
			dialog.setTitle("Project limit reached!");
			TextView myMsg = new TextView(this);
			myMsg.setText("The free version of this app" +
					"allows only 1 project,upgrade" +
					"and create unlimited projects!");
			//builder.setMessage("Project name can not be blank.");
			 
			ImageView inAppContinue = (ImageView) dialog
					.findViewById(R.id.inAppContinue);
			inAppContinue.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(SetProjectNameActivity.this,
							MultiPhotoSelectActivity.class);
					//intent.putExtra("morphName", projectName);
					//intent.putExtra("imageString", imageString);
					//intent.putExtra("update", 1);
					//startActivity(intent);
					dialog.dismiss();
					startActivity(intent);
					//startActivityForResult(intent, 2);
				}
			});
			ImageView inAppCancel = (ImageView) dialog
					.findViewById(R.id.inAppCancel);
			inAppCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//Intent intent = new Intent(SetProjectNameActivity.this,CapturePhotoActivity.class);
					//intent.putExtra("morphName", projectName);
					//intent.putExtra("update", 1);
					//startActivity(intent);
					dialog.dismiss();
					finish();
					//startActivityForResult(intent, 1);
				}
			});
			dialog.show();
			
		}
		myDbHelper.close();
		db.close();*/
		/****End in app pruchase*******/
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.nextBtn) {
			myDbHelper = new DatabaseHandler(this);
			myDbHelper.initializeDataBase();
			db = myDbHelper.getWritableDatabase();
			projName = projectName.getText().toString(); 
			Cursor cursor = db.rawQuery("SELECT * FROM " + ProjectEntity.TABLE_NAME + " WHERE morphname = '"+ projName + "';", null);
			
			if(cursor.moveToFirst()){ 
				//if (cursor.getString(cursor.getColumnIndex("morphname")).equals(projName)){
				Toast.makeText(SetProjectNameActivity.this, "Oops Name already Exist", Toast.LENGTH_SHORT).show();
		        //finish();
				
			}else{
			if (projName.trim().length()>0){
			Intent intent = new Intent(SetProjectNameActivity.this, CreateMorphActivity.class);
			intent.putExtra("projectName", projName);
			startActivity(intent);
			}else{
				builder = new AlertDialog.Builder(this);
				 builder.setMessage("Project name can not be blank.");
				
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						projectName.requestFocus();

					}
				});
				
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
		//}
			}
			myDbHelper.close();
			db.close();
		}else if(v.getId() == R.id.cancelProjName) {
			finish();
		}
	}
}

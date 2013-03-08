package com.bpd.smilemorph;

import com.bpd.database.DatabaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SetProjectNameActivity extends Activity implements OnClickListener {
	ImageView nextBtn,cancelBtn;
	EditText projectName;
	String projName;
	AlertDialog.Builder builder;
	private DatabaseHandler myDbHelper;
	SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createproject);
		projectName = (EditText)findViewById(R.id.projectName);
		nextBtn = (ImageView) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
		cancelBtn = (ImageView) findViewById(R.id.cancelProjName);
		cancelBtn.setOnClickListener(this);
		myDbHelper = new DatabaseHandler(this);
		myDbHelper.initializeDataBase();
		db = myDbHelper.getWritableDatabase();
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.nextBtn) {
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
		}else if(v.getId() == R.id.cancelProjName) {
			finish();
		}
	}
}

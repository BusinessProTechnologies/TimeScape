package com.bpd.smilemorph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bpd.database.DatabaseHandler;

public class ImagePostingActivity extends Activity implements OnClickListener {
	
	private DatabaseHandler myDbHelper;
	SQLiteDatabase db;
	ImageView imageView;
	Bitmap bmp_imgposting;
	ImageView saveBtn,deleteBtn;
	String morphName,imageString;
	int update;
	byte[] byteArray;
	private File outputPath = null;
	private Context context = this;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_photo);
		imageView = (ImageView) findViewById(R.id.showPhoto);
		saveBtn = (ImageView) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(this);
		deleteBtn = (ImageView) findViewById(R.id.deleteBtn);
		deleteBtn.setOnClickListener(this);
		Bundle extras = getIntent().getExtras();
        //bmp_imgposting = ex_intent.getParcelable("imageposting");
		byteArray = getIntent().getByteArrayExtra("imageposting");
		bmp_imgposting = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		morphName = extras.getString("morphName");
		Log.i("MorphNmae", "21= " + morphName);
		update = extras.getInt("update");
		Log.i("update", "12= " + update);
        imageView.setImageBitmap(bmp_imgposting);
	}
	/*@Override
	protected void onResume() {
		super.onResume();
		if(update == 1){
		myDbHelper = new DatabaseHandler(ImagePostingActivity.this);
		myDbHelper.initializeDataBase();
		db = myDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " +  ProjectEntity.TABLE_NAME + " WHERE morphname = '"+ morphName + "';", null);
		if(cursor.moveToFirst()){ 
		//Log.i("morphName",cursor.getString(cursor.getColumnIndex("morphname")));
		//morphName = cursor.getString(cursor.getColumnIndex("morphname"));
		imageString = cursor.getString(cursor.getColumnIndex("imagestring"));
		}else{
	        Toast.makeText(ImagePostingActivity.this, "Oops we did not find Morph, detail activity was closed", Toast.LENGTH_SHORT).show();
	        finish();
	    }
		myDbHelper.close();
		db.close();
		}
	}*/
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.saveBtn){
			Log.i("update", "12= " + update);
			outputPath = new File(Environment.getExternalStorageDirectory()
					+ "/SmileMorph/" + morphName);
			if (!outputPath.exists()) {
		           if (!outputPath.mkdirs()) {
		               Log.d("TimeScape", "failed to create directory.");
		               //return null;
		           }
		       }
			//outputPath.mkdir();
			File file = byteArrayToFile(outputPath,byteArray);
			try {
			FileOutputStream fos = new FileOutputStream(file);
	           fos.write(byteArray);
	           fos.close();
			} catch (FileNotFoundException e) {
		       	Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_LONG).show();
		       } catch (IOException e) {
		       	Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
		       }
		       myDbHelper = new DatabaseHandler(ImagePostingActivity.this);
				myDbHelper.initializeDataBase();
				db = myDbHelper.getWritableDatabase();
			if(update == 1){
				
				Cursor cursor = db.rawQuery("SELECT * FROM " +  ProjectEntity.TABLE_NAME + " WHERE morphname = '"+ morphName + "';", null);
				if(cursor.moveToFirst()){ 
				imageString = cursor.getString(cursor.getColumnIndex("imagestring"));
				}else{
			        Toast.makeText(ImagePostingActivity.this, "Oops we did not find Morph, detail activity was closed", Toast.LENGTH_SHORT).show();
			        finish();
			    }
				
				imageString = imageString + file.getAbsolutePath() + "|";
				Log.i("imageString",imageString);
				String[] separated = imageString.replace("|", ",").split(",");
				ContentValues value=new ContentValues();
            	value.put("morphname", morphName);
		        value.put("imagestring",imageString);
		        value.put("noimges",separated.length);
		        db.update(ProjectEntity.TABLE_NAME, value,"morphName = ?", new String[] { morphName });
		        Log.i("update_query",db.update(ProjectEntity.TABLE_NAME, value,"morphName = ?", new String[] { morphName })+"");
			}else{
				imageString = "";
				imageString = imageString + file.getAbsolutePath() + "|";
				Log.i("imageString",imageString);
				String[] separated = imageString.replace("|", ",").split(",");
				db.execSQL("INSERT INTO " + ProjectEntity.TABLE_NAME + " (morphname,imagestring,noimges)" + " VALUES ('" + morphName.trim() + "','"+ imageString + "',"+ separated.length +") ;");
			}
			myDbHelper.close();
			db.close();
			Intent intent = new Intent(ImagePostingActivity.this,
					SelectedImageActivity.class);
			intent.putExtra("imageString", imageString);
			intent.putExtra("projectName", morphName);
			setResult(RESULT_OK, intent);
			startActivity(intent);
			this.finish();
		}else if (v.getId() == R.id.deleteBtn) {
			/*if(update == 1){
			Intent intent = new Intent(ImagePostingActivity.this,
					SelectedImageActivity.class);
			intent.putExtra("imageString", imageString);
			intent.putExtra("projectName", morphName); 
			setResult(RESULT_OK, intent);
			startActivity(intent);
			}else{*/
			finish();
			//}
		}
	}
	public File byteArrayToFile(File outputPath,byte[] byteArray)
	{
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mFile= new File(outputPath.getPath() + File.separator +
        	        "IMG_"+ timeStamp + ".jpg");
            return mFile;
		
	}

}

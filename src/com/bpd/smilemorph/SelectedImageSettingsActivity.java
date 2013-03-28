package com.bpd.smilemorph;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bpd.database.DatabaseHandler;

public class SelectedImageSettingsActivity extends Activity implements OnClickListener{

	private DatabaseHandler myDbHelper;
	private SQLiteDatabase db;
	private String imageString,projectName;
	private ArrayList<String> arrylst_seperator;
	private String[] separated;
	private EditText editProjectName;
	private TextView noImages;
	private ImageView cancelBtn,delImg,saveBtn;
	private int id;
	private MyPagerAdapter adapter;
	private ViewPager myPager;
	private String imageStg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectedimagesttngs);
		
		
		arrylst_seperator =new ArrayList<String>();
		Bundle extras = getIntent().getExtras();
		imageString = extras.getString("imageString");
		projectName = extras.getString("projectName");
		Log.i("MorphNmae", "00= " + imageString);
		Log.i("projectName=", projectName);
		editProjectName  = (EditText) findViewById(R.id.editProjectName);
		editProjectName.setGravity(Gravity.CENTER);
		editProjectName.setText(projectName);
		noImages = (TextView)findViewById(R.id.noImgs);
		cancelBtn = (ImageView) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
		saveBtn = (ImageView) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(this);
		
		
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		separated = imageString.replace("|", ",").split(",");
		
		for(int i=0;i<separated.length;i++){
		   arrylst_seperator.add(separated[i]);
		   Log.i("value",arrylst_seperator+""); 
		}
		
		myDbHelper = new DatabaseHandler(this);
		myDbHelper.initializeDataBase();
		db = myDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " +  ProjectEntity.TABLE_NAME + " WHERE morphname = '"+ projectName + "';", null);
		if(cursor.moveToFirst()){ 
		//Log.i("morphName",cursor.getString(cursor.getColumnIndex("morphname")));
		id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
		
		//imageString = cursor.getString(cursor.getColumnIndex("imagestring"));
		}
		myDbHelper.close();
		db.close();
		Log.i("select_id",id+"");
		noImages.setText(String.valueOf(arrylst_seperator.size()) + " image(s)");
		adapter = new MyPagerAdapter();
	    myPager = (ViewPager) findViewById(R.id.delimagepanelpager);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(0);
	    myPager.setHorizontalFadingEdgeEnabled(true);
	    myPager.setOffscreenPageLimit(3);
	}
	class MyPagerAdapter extends PagerAdapter {
		
		int pos;
		 @Override
			public int getCount() {
				return arrylst_seperator.size();
			}
		
	    public Object instantiateItem(ViewGroup collection, int position) {
	    	
	        LayoutInflater inflater = (LayoutInflater) collection.getContext()
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View view = null;
	        view =
	            getLayoutInflater().inflate(R.layout.deleteimg_viewpager, collection, false);
	        //view = inflater.inflate(R.layout.deleteimg_viewpager, null);
	        
	        ImageView imageViewPager = (ImageView) view.findViewById(R.id.imageViewPager);
			delImg = (ImageView) view.findViewById(R.id.delImg);
			//pos = position;
			delImg.setTag(position);
			final int len = arrylst_seperator.size();
			 //Log.i("imageUri", separated[position]);
			Log.i("position", position+"");
			delImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							SelectedImageSettingsActivity.this);
					adb.setTitle("ListView OnClick");
					pos = Integer.parseInt(v.getTag().toString());
					adb.setMessage("Selected Item is = " + pos+""+ arrylst_seperator.get(pos));
					Log.i("getTag", v.getTag()+"");
					adb.setPositiveButton("Ok", new Dialog.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            	imageString = "";
			            	//pos = Integer.parseInt(v.getTag().toString());
			            	/*for (int i = 0; i < len; i++) {
			            		if(pos == i){*/
			            			//adapter.myPager.remove(separated[i]);
			            			Log.i("pos", pos+" "+arrylst_seperator.get(pos));
			            			arrylst_seperator.remove(pos);
			            			Log.i("pos11", pos+" "+arrylst_seperator.size());
			            			noImages.setText(String.valueOf(arrylst_seperator.size()) + " image(s)");
			            			myPager.setAdapter(adapter);
			            		    adapter.notifyDataSetChanged();
			            		    //myPager.invalidate();
			            		    //adapter.notifyDataSetInvalidated();
			            		//}else{
			            		    for (int pos = 0; pos < arrylst_seperator.size(); pos++) {
			            		    	imageString = imageString + arrylst_seperator.get(pos) + "|";
			            		    Log.i("imageStg", imageString);
			            		}
			            	//}
			            }
			        });
					adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        	  public void onClick(DialogInterface dialog, int which) {
			        	    // Canceled.
			        	  }
			        	});
					adb.show(); 
					
					
				}
			});
			
	       
	        
	        BitmapFactory.Options bfo = new BitmapFactory.Options();  
		    bfo.inSampleSize = 4;   
		    Log.i("arrylst_seperator", arrylst_seperator.get(position));
		    Bitmap ThumbImage = BitmapFactory.decodeFile(arrylst_seperator.get(position),bfo); 
		    //ThumbImage = Bitmap.createScaledBitmap(ThumbImage, 200, 200, false);

		    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
		    //Log.i("drawableImage", drawableImage+"");
		    imageViewPager.setImageDrawable(drawableImage);

	        //imageViewPager.setImageURI( Uri.parse(separated[position]));
	        
	        //Bitmap bitmap = getThumbnail(Uri.parse(separated[position]));
	        //imageViewPager.setImageBitmap(bitmap);
	        
	        ((ViewPager) collection).addView(view);
	        return view;
	    }
	    protected void notifyDataSetInvalidated() {
			// TODO Auto-generated method stub
			
		}

		@Override
	    public void destroyItem(View arg0, int arg1, Object arg2) {
	        ((ViewPager) arg0).removeView((View) arg2);
	    }
		@Override
	    public float getPageWidth(int position) {
	      return(0.8f);
	    }
	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1) {
	        return arg0 == ((View) arg1);
	    }
	    @Override
	    public Parcelable saveState() {
	        return null;
	    }
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.cancelBtn) {
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}else if (v.getId() == R.id.saveBtn) {
			myDbHelper = new DatabaseHandler(this);
			myDbHelper.initializeDataBase();
			db = myDbHelper.getWritableDatabase();
			ContentValues value=new ContentValues();
		        //value.put("site_id",site_id);
			//Log.i("dbmorphname",editProjectName.getText().toString().trim());
		        value.put("morphname", editProjectName.getText().toString().trim());
		        
		        value.put("imagestring",imageString);
		        value.put("noimges",arrylst_seperator.size());
		        int noimges = arrylst_seperator.size();
		       /* value.put("template_cntrltype",controltype.toString().trim());
		        value.put("template_action", actiontype.toString().trim());
		        value.put("template_request",confirm_checked);
		        value.put("template_section",section_checked);*/
		    	
		    	/*if(check_template_insert==0){
		    		
			        lastInsertId = db.insert(SMSControlEntity.TABLE_NAME,null,value);
			        check_template_insert=1;
		    	}else{*/
		        //Log.i("use_id",id+"");
		        	//Log.i("update_query",db.update(ProjectEntity.TABLE_NAME, value,"id = ?", new String[] { String.valueOf(id) })+"");
		    		db.update(ProjectEntity.TABLE_NAME, value,"id = ?", new String[] { String.valueOf(id) });
		    		
		    	//}

			myDbHelper.close();
			db.close();
			Intent intent = new Intent(this,
					SelectedImageActivity.class);
			//Log.i("dbimagestring",imageString);
			intent.putExtra("imageString", imageString);
			intent.putExtra("projectName", editProjectName.getText().toString().trim()); 
			//Log.i("noimges",noimges+"");
			intent.putExtra("noimges", String.valueOf(noimges));
			setResult(RESULT_OK, intent);
			finish();
		}
		
	}

}

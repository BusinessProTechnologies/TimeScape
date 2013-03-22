package com.bpd.smilemorph;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bpd.database.DatabaseHandler;
import com.bpd.utils.Utils;

public class ExistingMorphActivity extends Activity implements OnClickListener {

	private DatabaseHandler myDbHelper;
	SQLiteDatabase db;
	ListView listView;
	TextView exProjTxt,txtNoProj;
	public ProjectAdapter adpt;
	ArrayList<ProjectEntity> projectList;
	ProjectEntity project;
	boolean showDelete = false;
	String morphName,imageString;
	ImageView editBtn,backBtn,doneBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_existmorph);
		
		projectList = new ArrayList<ProjectEntity>();
		
		backBtn = (ImageView) findViewById(R.id.cancelExProj);
		backBtn.setOnClickListener(this);
		editBtn = (ImageView) findViewById(R.id.editExProjBtn);
		editBtn.setOnClickListener(this);
		doneBtn = (ImageView) findViewById(R.id.doneExProjBtn);
		doneBtn.setOnClickListener(this);
		exProjTxt = (TextView) findViewById(R.id.txtExProj);
		exProjTxt.setText("SELECT A PROJECT");
		txtNoProj = (TextView)findViewById(R.id.txtNoProj);
		ImageView startProj = (ImageView) findViewById(R.id.strtProj);
		//startProj.setImageBitmap(Utils.decodeBitmapFromResource(getResources(), R.drawable.startprojselector, 200, 90));
		startProj.setOnClickListener(this);
		
		
		listView = (ListView) findViewById(R.id.projectList);
		/*if(projectList.isEmpty()){
			listView.setVisibility(View.INVISIBLE);
			txtNoProj.setVisibility(View.VISIBLE);
		}*/
		
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
		public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
		{
			//Log.i("pos",position+"");
			/*AlertDialog.Builder adb = new AlertDialog.Builder(
					ExistingMorphActivity.this);
			adb.setTitle("ListView OnClick");
			adb.setMessage("Selected Item is = "
			+ v.getTag());
			adb.setPositiveButton("Ok", null);
			adb.show(); */ 
			myDbHelper = new DatabaseHandler(ExistingMorphActivity.this);
			myDbHelper.initializeDataBase();
			db = myDbHelper.getWritableDatabase();
			Integer projId = (Integer) v.getTag();
			Cursor cursor = db.rawQuery("SELECT * FROM " +  ProjectEntity.TABLE_NAME + " WHERE id = "+ projId + ";", null);
			if(cursor.moveToFirst()){ 
			morphName = cursor.getString(cursor.getColumnIndex("morphname"));
			imageString = cursor.getString(cursor.getColumnIndex("imagestring"));
			}else{
		        Toast.makeText(ExistingMorphActivity.this, "Oops we did not find Morph, detail activity was closed", Toast.LENGTH_SHORT).show();
		        finish();
		    }
			myDbHelper.close();
			db.close();
			Intent intent = new Intent(ExistingMorphActivity.this, SelectedImageActivity.class);
			intent.putExtra("imageString", imageString);
			intent.putExtra("projectName", morphName); 
			finish();
			startActivity(intent);
		 }
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		projectList.removeAll(projectList);
		try{
			myDbHelper = new DatabaseHandler(this);
			myDbHelper.initializeDataBase();
			db = myDbHelper.getWritableDatabase();
			String selectProjs = "SELECT  * FROM " + ProjectEntity.TABLE_NAME + ";";
			//Log.i("query",selectEmps);
			Cursor cursor = db.rawQuery(selectProjs, null);
			if(cursor.moveToFirst()){
				do{
					
					project = new ProjectEntity();
					project.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));//c.getColumnIndex("FirstName")
					project.setName(cursor.getString(cursor.getColumnIndex("morphname")));
					project.setImgString(cursor.getString(cursor.getColumnIndex("imagestring")));
					project.setImgNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex("noimges"))));
		            // Adding contact to list
		            projectList.add(project);
					//Log.i("name",name);
				}while(cursor.moveToNext());
			}
			myDbHelper.close();
			db.close();
			if(projectList.isEmpty()){
				listView.setVisibility(View.INVISIBLE);
				txtNoProj.setVisibility(View.VISIBLE);
			}
			adpt = new ProjectAdapter(this,projectList);
			listView.setAdapter(adpt);
			cursor.close();
		}catch(Exception ex){
			ex.printStackTrace();
		} 
	}
	
	public class ProjectAdapter extends ArrayAdapter<ProjectEntity> {

		private Context context;
		private ArrayList<ProjectEntity> values;
		public ProjectAdapter(Context context,
				ArrayList<ProjectEntity> projectList) {
			super(context,  R.layout.projlistadapter);
			this.values = projectList;
			this.context = context;
			/*final View layout = View.inflate(context, R.layout.edittext, null);
		    final EditText savedText = ((EditText) layout.findViewById(R.id.myEditText));*/
		}

		@Override
		public int getCount() {
			return values.size();
		}

		/*@Override
		public ProjectEntity getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}*/

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = convertView;
		    final int pos = position;
		    if(rowView == null){
		    	rowView = inflater.inflate(R.layout.projlistadapter, parent, false);
		    }
		    TextView cv = (TextView) rowView.findViewById(R.id.projName);
		    ImageView projImg = (ImageView) rowView.findViewById(R.id.projImage);
		    TextView img_no = (TextView) rowView.findViewById(R.id.imgNo);
		    //Log.i("imgstring", values.get(position).getImgString());
		    final int id = values.get(position).getID();
		    String imgString = values.get(position).getImgString();
		    String[] separated = imgString.replace("|", ",").split(",");
		    BitmapFactory.Options bfo = new BitmapFactory.Options();  
		    bfo.inSampleSize = 8;   
		    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[0],bfo); 
		    ThumbImage = Bitmap.createScaledBitmap(ThumbImage, 70, 70, false);

		    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
		    //Log.i("drawableImage", drawableImage+"");
		    projImg.setImageDrawable(drawableImage);
		    rowView.setTag(values.get(position).getID());
		    cv.setText(values.get(position).getName());
		    final File projPath = new File(Environment.getExternalStorageDirectory()
					+ "/SmileMorph/" + values.get(position).getName());
		    img_no.setText(String.valueOf(values.get(position).getImgNumber() + " image(s)"));
		    ImageView imgArr = (ImageView) rowView.findViewById(R.id.imgArrow);
		    ImageView imgDel = (ImageView) rowView.findViewById(R.id.projDel);
		    imgDel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
					ExistingMorphActivity.this);
					adb.setTitle("ListView OnClick");
					adb.setMessage("Selected Item is = "
					+ id);
					//adb.setPositiveButton("Ok", null);
					adb.setPositiveButton("Ok", new Dialog.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            	//Log.i("Query","DELETE FROM " +  ProjectEntity.TABLE_NAME + " WHERE id = '"+ id + "';");
			            	//db.rawQuery("DELETE FROM " +  ProjectEntity.TABLE_NAME + " WHERE id = '"+ id + "';", null);
							
							 	myDbHelper = new DatabaseHandler(ExistingMorphActivity.this);
				         		myDbHelper.initializeDataBase();
				         		db = myDbHelper.getWritableDatabase();
				            	
				            	String table_name = ProjectEntity.TABLE_NAME;
				    			String where = "id = '"+id+"'";
				    			//db.delete(table_name, where, null);
				    			if(db.delete(table_name, where, null) == 1){
				    			DeleteRecursive(projPath);
				    			}
				    			myDbHelper.close();
				    			db.close();
				    			projectList.remove(pos);
				    			if(projectList.isEmpty()){
				    				listView.setVisibility(View.INVISIBLE);
				    				txtNoProj.setVisibility(View.VISIBLE);
				    			}
								adpt.notifyDataSetChanged();
								adpt.notifyDataSetInvalidated();
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
		    
		    if(showDelete){
		    	imgDel.setVisibility(View.VISIBLE);
		    	imgArr.setVisibility(View.GONE);
		    	img_no.setVisibility(View.GONE);
		    	exProjTxt.setText("EDIT PROJECTS");
		    }else{
		    	imgDel.setVisibility(View.GONE);
		    	imgArr.setVisibility(View.VISIBLE);
		    	img_no.setVisibility(View.VISIBLE);
		    	exProjTxt.setText("SELECT A PROJECT");
		    }
			return rowView;
		}
		
	}

	void DeleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            DeleteRecursive(child);

	    fileOrDirectory.delete();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.cancelExProj) {
			finish();
		}else if(v.getId() == R.id.strtProj) {
			Intent intent = new Intent(ExistingMorphActivity.this, SetProjectNameActivity.class);
			finish();
			startActivity(intent);
		}else if(v.getId() == R.id.editExProjBtn){
			showDelete = true;
			editBtn.setVisibility(View.GONE);
			doneBtn.setVisibility(View.VISIBLE);
			adpt = new ProjectAdapter(this,projectList);
			listView.setAdapter(adpt);
			//adpt.notifyDataSetChanged();
		}else if(v.getId() == R.id.doneExProjBtn){
			showDelete = false;
			doneBtn.setVisibility(View.GONE);
			editBtn.setVisibility(View.VISIBLE);
			adpt = new ProjectAdapter(this,projectList);
			listView.setAdapter(adpt);
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("HA", "Finishing");
		if (isTaskRoot()&&(keyCode == KeyEvent.KEYCODE_BACK)) {
			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					//.setTitle("")
					.setMessage("Do you want to exit?")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Stop the activity
									System.exit(0);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Stop the activity
									dialog.cancel();
								}
							}).show();
			return true;

		}else {
			return super.onKeyDown(keyCode, event);
        }
		
	}
}

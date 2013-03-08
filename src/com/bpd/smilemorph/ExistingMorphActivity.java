package com.bpd.smilemorph;

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
import android.util.Log;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_existmorph);
		
		projectList = new ArrayList<ProjectEntity>();
		
		ImageView backBtn = (ImageView) findViewById(R.id.cancelExProj);
		backBtn.setOnClickListener(this);
		Button editBtn = (Button) findViewById(R.id.editExProjBtn);
		editBtn.setOnClickListener(this);
		exProjTxt = (TextView) findViewById(R.id.txtExProj);
		exProjTxt.setText("SELECT A PROJECT");
		txtNoProj = (TextView)findViewById(R.id.txtNoProj);
		ImageView startProj = (ImageView) findViewById(R.id.strtProj);
		startProj.setOnClickListener(this);
		startProj.setImageBitmap(Utils.decodeBitmapFromResource(getResources(), R.drawable.startproj2, 200, 90));
		
		listView = (ListView) findViewById(R.id.projectList);
		/*if(projectList.isEmpty()){
			listView.setVisibility(View.INVISIBLE);
			txtNoProj.setVisibility(View.VISIBLE);
		}*/
		
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
		public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
		{
			Log.i("pos",position+"");
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
			Log.i("morphName",cursor.getString(cursor.getColumnIndex("morphname")));
			morphName = cursor.getString(cursor.getColumnIndex("morphname"));
			imageString = cursor.getString(cursor.getColumnIndex("imagestring"));
			}else{
		        Toast.makeText(ExistingMorphActivity.this, "Oops we did not find Morph, detail activity was closed", Toast.LENGTH_SHORT).show();
		        finish();
		    }
			myDbHelper.close();
			db.close();
			Log.i("morphName",morphName);
			Intent intent = new Intent(ExistingMorphActivity.this, SelectedImageActivity.class);
			intent.putExtra("imageString", imageString);
			intent.putExtra("projectName", morphName); 
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
					//Log.i("sss", cursor.getString(cursor.getColumnIndex("id"))+"");
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
		    Log.i("imgstring", values.get(position).getImgString());
		    final int id = values.get(position).getID();
		    String imgString = values.get(position).getImgString();
		    String[] separated = imgString.replace("|", ",").split(",");
		    Log.i("1stimg", separated[0]);
		    /*String imageName = separated[0].substring(
					separated[0].lastIndexOf("/") + 1,
					separated[0].length());
		    Log.i("1stimgsize", imageName.getBytes()+"");*/
		    //Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(separated[0]), 60, 60);
		    
		    BitmapFactory.Options bfo = new BitmapFactory.Options();  
		    bfo.inSampleSize = 8;   
		    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[0],bfo); 
		    ThumbImage = Bitmap.createScaledBitmap(ThumbImage, 100, 100, false);

		    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
		    Log.i("drawableImage", drawableImage+"");
		    projImg.setImageDrawable(drawableImage);
		    rowView.setTag(values.get(position).getID());
		    cv.setText(values.get(position).getName());
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
			            	Log.i("Query","DELETE FROM " +  ProjectEntity.TABLE_NAME + " WHERE id = '"+ id + "';");
			            	//db.rawQuery("DELETE FROM " +  ProjectEntity.TABLE_NAME + " WHERE id = '"+ id + "';", null);
							
							 	myDbHelper = new DatabaseHandler(ExistingMorphActivity.this);
				         		myDbHelper.initializeDataBase();
				         		db = myDbHelper.getWritableDatabase();
				            	
				            	String table_name = ProjectEntity.TABLE_NAME;
				    			String where = "id = '"+id+"'";
				    			db.delete(table_name, where, null);
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
					/*Cursor cursor = db.rawQuery("SELECT morphname FROM " +  ProjectEntity.TABLE_NAME + " WHERE id = "+ id + ";", null);
					if(cursor.moveToFirst()){ 
					Log.i("morphName",cursor.getString(cursor.getColumnIndex("morphname")));
					morphName = cursor.getString(cursor.getColumnIndex("morphname"));
					imageString = cursor.getString(cursor.getColumnIndex("imagestring"));
					}*/
					
				}
			});
		    
		    if(showDelete){
		    	imgDel.setVisibility(View.VISIBLE);
		    	imgArr.setVisibility(View.GONE);
		    	img_no.setVisibility(View.GONE);
		    	exProjTxt.setText("EDIT PROJECTS");
		    }
			return rowView;
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.cancelExProj) {
			finish();
		}else if(v.getId() == R.id.strtProj) {
			Intent intent = new Intent(ExistingMorphActivity.this, SetProjectNameActivity.class);
			startActivity(intent);
		}else if(v.getId() == R.id.editExProjBtn){
			showDelete = true;
			//exProjTxt.setText("EDIT PROJECTS");
			adpt = new ProjectAdapter(this,projectList);
			listView.setAdapter(adpt);
			//adpt.notifyDataSetChanged();
		}/*else if(v.getId() == R.id.projDel){
			AlertDialog.Builder adb = new AlertDialog.Builder(
			ExistingMorphActivity.this);
			adb.setTitle("ListView OnClick");
			adb.setMessage("Selected Item is = "
			+ v.getTag());
			adb.setPositiveButton("Ok", null);
			adb.show(); 
		}*/
	}
}

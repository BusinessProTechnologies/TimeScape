package com.bpd.smilemorph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MultiPhotoSelectActivity extends Activity implements
		OnClickListener {

	private int count;
	private ArrayList<Bitmap> thumbnails;
	private boolean[] thumbnailsselection;
	private String[] arrPath;
	private ImageAdapter imageAdapter;
	Uri uriImagePath;
	String mSelectedImagePath;
	String morphName;
	String inputPath = null;
	File outputPath = null;
	String imageName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_grid);
		
		Bundle extras = getIntent().getExtras();
		morphName = extras.getString("morphName");

		Log.i("MorphNmae", "11= " + morphName);

		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);

		Button selectBtn = (Button) findViewById(R.id.selectBtn);
		selectBtn.setOnClickListener(this);
		
		this.thumbnails = new ArrayList<Bitmap>();
		
		GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
		imageAdapter = new ImageAdapter();
		imagegrid.setAdapter(imageAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media._ID;
		Cursor imagecursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy);
		int image_column_index = imagecursor
				.getColumnIndex(MediaStore.Images.Media._ID);
		this.count = imagecursor.getCount();
		
		this.arrPath = new String[this.count];
		this.thumbnailsselection = new boolean[this.count];
		if(!thumbnails.isEmpty()) {
			thumbnails.clear();
		}
		for (int i = 0; i < this.count; i++) {
			imagecursor.moveToPosition(i);
			int id = imagecursor.getInt(image_column_index);
			int dataColumnIndex = imagecursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			// Log.i("Image",dataColumnIndex+"");
			thumbnails.add(MediaStore.Images.Thumbnails.getThumbnail(
					getApplicationContext().getContentResolver(), id,
					MediaStore.Images.Thumbnails.MICRO_KIND, null));
			// Log.i("ThumB",thumbnails[i]+"");
			arrPath[i] = imagecursor.getString(dataColumnIndex);
		}
		
		//imagecursor.close();
	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return thumbnails.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.row_multiphoto_item,
						null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.thumbImage);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.itemCheckBox);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.checkbox.setId(position);
			holder.imageview.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					if (thumbnailsselection[id]) {
						cb.setChecked(false);
						thumbnailsselection[id] = false;
					} else {
						cb.setChecked(true);
						thumbnailsselection[id] = true;
					}
				}
			});
			holder.imageview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = v.getId();
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse("file://" + arrPath[id]),
							"image/*");
					startActivity(intent);
				}
			});
			holder.imageview.setImageBitmap(thumbnails.get(position));
			holder.checkbox.setChecked(thumbnailsselection[position]);
			holder.id = position;
			return convertView;
		}
	}

	class ViewHolder {
		ImageView imageview;
		CheckBox checkbox;
		int id;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancelBtn) {
			finish();
		} else if (v.getId() == R.id.selectBtn) {
			final int len = thumbnailsselection.length;
			int cnt = 0;
			String selectImages = "";

			outputPath = new File(Environment.getExternalStorageDirectory()
					+ "/SmileMorph/" + morphName);
			outputPath.mkdir();
			Log.i("DIR name=", outputPath + "");

			for (int i = 0; i < len; i++) {
				if (thumbnailsselection[i]) {
					cnt++;
					selectImages = selectImages + arrPath[i] + "|";

				}
			}

			if (cnt == 0) {
				Toast.makeText(getApplicationContext(),
						"Please select at least one image", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getApplicationContext(),
						"You've selected Total " + cnt + " image(s).",
						Toast.LENGTH_LONG).show();
				// Log.d("SelectedImages", selectImages);
				inputPath = selectImages;
				Log.i("inputPath=", inputPath);
				new copyImage(inputPath, outputPath).execute();
				// Log.i("SelectedImagesName", selectImages);

				/*
				 * File path = Environment.getDataDirectory(); StatFs stat = new
				 * StatFs(path.getPath()); long blockSize = stat.getBlockSize();
				 * long availableBlocks = stat.getAvailableBlocks();
				 * Log.i("MEMORY",Formatter.formatFileSize(this, availableBlocks
				 * * blockSize));
				 */
			}
		}
	}

	private class copyImage extends AsyncTask<String, File, String> {

		public copyImage(String inputPath, File outputPath) {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String doInBackground(String... arg0) {
			InputStream in = null;
			OutputStream out = null;
			String finalPath = "";
			// Log.i("inPUT",inputPath);
			Log.i("outPUT", outputPath + "");
			String outputPathStr = outputPath.toString() + "/";

			// String imgUri = inputPath.substring(0,inputPath.length() - 1);
			// Log.i("imgUri",imgUri);

			String[] separated = inputPath.replace("|", ",").split(",");
			//Log.i("1st", separated[0].toString());

			for (int i = 0; i < separated.length; i++) {
				// if(!separated[1].equals(null)){
				// String s = separated[0];
				imageName = separated[i].substring(
						separated[i].lastIndexOf("/") + 1,
						separated[i].length());
				Log.i("imageName", imageName);
				Log.i(i + "nd", separated[i]);
				try {
					in = new FileInputStream(separated[i]);
					out = new FileOutputStream(outputPathStr + imageName);
					finalPath = finalPath + outputPathStr + imageName + "|";
					Log.i("FinalFolder", finalPath);
					byte[] buffer = new byte[1024];
					int read;
					while ((read = in.read(buffer)) != -1) {
						out.write(buffer, 0, read);
					}
					in.close();
					in = null;

					// write the output file (You have now copied the file)
					out.flush();
					out.close();
					out = null;

				} catch (FileNotFoundException fnfe1) {
					Log.e("tag", fnfe1.getMessage());
				} catch (Exception e) {
					Log.e("tag", e.getMessage());
				}
			}
			// }
			// separated = separated[1];
			return finalPath;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Intent intent = new Intent(MultiPhotoSelectActivity.this,
					SelectedImageActivity.class);
			Log.i("imageString", result);
			intent.putExtra("imageString", result);
			intent.putExtra("projectName", morphName);
			startActivity(intent);
		}
	}// end of copyImage( )

	/*
	 * public void slectImages(){
	 * 
	 * 
	 * }
	 */
}

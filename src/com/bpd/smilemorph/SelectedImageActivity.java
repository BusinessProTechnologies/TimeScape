package com.bpd.smilemorph;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectedImageActivity extends Activity implements OnClickListener{

	String imageString;
	String[] separated;
	Uri[] imageUri;
	String projectName;
	TextView projName,noImages;
	ImageView imageAdder,homeView,playView,addView,shareView;
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectedimage);
		
		Bundle extras = getIntent().getExtras();
		imageString = extras.getString("imageString");
		projectName = extras.getString("projectName");
		Log.i("MorphNmae", "00= " + imageString);
		Log.i("projectName=", projectName);
		
		projName = (TextView) findViewById(R.id.projectName);
		projName.setText(projectName);
		noImages = (TextView)findViewById(R.id.noImgs);
		imageAdder = (ImageView) findViewById(R.id.imageAdder);
		imageAdder.setOnClickListener(this);
		homeView = (ImageView) findViewById(R.id.homeBtn);
		homeView.setOnClickListener(this);
		playView = (ImageView) findViewById(R.id.playBtn);
		playView.setOnClickListener(this);
		addView = (ImageView) findViewById(R.id.addBtn);
		addView.setOnClickListener(this);
		shareView = (ImageView) findViewById(R.id.shareBtn);
		shareView.setOnClickListener(this);
		
		separated = imageString.replace("|", ",").split(",");
		noImages.setText(String.valueOf(separated.length) + " image(s)");
		MyPagerAdapter adapter = new MyPagerAdapter();
	    ViewPager myPager = (ViewPager) findViewById(R.id.imagepanelpager);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(0);
	    myPager.setHorizontalFadingEdgeEnabled(true);
	}
	
	class MyPagerAdapter extends PagerAdapter {
		
		 @Override
			public int getCount() {
				return separated.length;
			}
		
	    public Object instantiateItem(View collection, int position) {
	        LayoutInflater inflater = (LayoutInflater) collection.getContext()
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View view = null;
	        view = inflater.inflate(R.layout.show_selectedimage, null);
	        
	        ImageView imageViewPager = (ImageView) view.findViewById(R.id.imageViewPager);
	        Log.i("imageUri", separated[position]);
	        
	        BitmapFactory.Options bfo = new BitmapFactory.Options();  
		    bfo.inSampleSize = 4;   
		    Bitmap ThumbImage = BitmapFactory.decodeFile(separated[position],bfo); 
		    //ThumbImage = Bitmap.createScaledBitmap(ThumbImage, 200, 200, false);

		    Drawable drawableImage = new BitmapDrawable(getResources(),ThumbImage); 
		    Log.i("drawableImage", drawableImage+"");
		    imageViewPager.setImageDrawable(drawableImage);

	        //imageViewPager.setImageURI( Uri.parse(separated[position]));
	        
	        //Bitmap bitmap = getThumbnail(Uri.parse(separated[position]));
	        //imageViewPager.setImageBitmap(bitmap);
	        
	        ((ViewPager) collection).addView(view, 0);
	        return view;
	    }
	    @Override
	    public void destroyItem(View arg0, int arg1, Object arg2) {
	        ((ViewPager) arg0).removeView((View) arg2);
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
		if (v.getId() == R.id.imageAdder) {
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

					Intent intent = new Intent(SelectedImageActivity.this,
							MultiPhotoSelectActivity.class);
					Log.i("morphname", projectName);
					intent.putExtra("morphName", projectName);
					intent.putExtra("imageString", imageString);
					startActivity(intent);
				}
			});
			Button addFrmCamera = (Button) dialog
					.findViewById(R.id.addFrmCamera);
			addFrmCamera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SelectedImageActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
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
		}else if (v.getId() == R.id.homeBtn) {
			Intent intent = new Intent(SelectedImageActivity.this,
					MainActivity.class);
			//intent.putExtra("morphName", projectName);
			startActivity(intent);
		}else if (v.getId() == R.id.playBtn) {
			
		}else if (v.getId() == R.id.addBtn) {
			
		}else if (v.getId() == R.id.shareBtn) {
			
		}
		
		
	}

}

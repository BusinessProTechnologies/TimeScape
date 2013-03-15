package com.bpd.smilemorph;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectedImageActivity extends Activity implements OnClickListener{

	String imageString;
	String[] separated;
	Uri[] imageUri;
	String projectName;
	TextView projName,noImages;
	ImageView imageAdder,homeView,playView,addView,shareView,settingsBtn;
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
		settingsBtn = (ImageView) findViewById(R.id.settingsBtn);
		settingsBtn.setOnClickListener(this);
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
		    bfo.inSampleSize = 8;   
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
		if (v.getId() == R.id.imageAdder || v.getId() == R.id.addBtn ) {
			// custom dialog
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_add_morph);
			// dialog.setTitle("Title...");

			ImageView addFrmGallery = (ImageView) dialog
					.findViewById(R.id.addFrmGallery);
			addFrmGallery.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(SelectedImageActivity.this,
							MultiPhotoSelectActivity.class);
					Log.i("morphname", projectName);
					intent.putExtra("morphName", projectName);
					intent.putExtra("imageString", imageString);
					intent.putExtra("update", 1);
					//startActivity(intent);
					startActivityForResult(intent, 2);
				}
			});
			ImageView addFrmCamera = (ImageView) dialog
					.findViewById(R.id.addFrmCamera);
			addFrmCamera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SelectedImageActivity.this,
							CapturePhotoActivity.class);
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
		}else if (v.getId() == R.id.settingsBtn) {
			Intent intent = new Intent(SelectedImageActivity.this,
					SelectedImageSettingsActivity.class);
			intent.putExtra("imageString", imageString);
			intent.putExtra("projectName", projectName); 
			startActivityForResult(intent, 1);
		}else if (v.getId() == R.id.homeBtn) {
			Intent intent = new Intent(SelectedImageActivity.this,
					MainActivity.class);
			//intent.putExtra("morphName", projectName);
			startActivity(intent);
		}else if (v.getId() == R.id.playBtn) {
			Intent intent = new Intent(SelectedImageActivity.this,
					PlayMorphActivity.class);
			intent.putExtra("imageString", imageString);
			//intent.putExtra("morphName", projectName);
			startActivity(intent);
			
		}else if (v.getId() == R.id.shareBtn) {
			
			// custom dialog
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_record_share);
			//dialog.setTitle("Record and Share Video");

			ImageView fbBtn = (ImageView) dialog
					.findViewById(R.id.fbBtn);
			fbBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					/*Intent intent = new Intent(CreateMorphActivity.this,
							MultiPhotoSelectActivity.class);
					//Log.i("morphname", _morphName);
					intent.putExtra("morphName", projectName);
					startActivity(intent);*/
				}
			});
			ImageView utubeBtn = (ImageView) dialog
					.findViewById(R.id.utubeBtn);
			utubeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);*/
				}
			});
			/*ImageView twitterBtn = (ImageView) dialog
			.findViewById(R.id.twitterBtn);
			twitterBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);
				}
			});*/
			ImageView saveVdoBtn = (ImageView) dialog
			.findViewById(R.id.saveVdoBtn);
			saveVdoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(CreateMorphActivity.this,
						PhotoIntentActivity.class);
				intent.putExtra("morphName", projectName);
				startActivity(intent);*/
			}
			});
			ImageView emailBtn = (ImageView) dialog
			.findViewById(R.id.emailBtn);
			emailBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);*/
				}
			});
			ImageView msgBtn = (ImageView) dialog
			.findViewById(R.id.msgBtn);
			msgBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent = new Intent(CreateMorphActivity.this,
							PhotoIntentActivity.class);
					intent.putExtra("morphName", projectName);
					startActivity(intent);*/
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
			
		}
		
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	super.onActivityResult(requestCode, resultCode, data);
		//if(data.getExtras().containsKey("widthInfo")){
	if (requestCode == 1) {
		if(resultCode == RESULT_OK){
		noImages.setText(data.getStringExtra(String.valueOf("noimges"))+ " image(s)");
		projectName = data.getStringExtra("projectName");
		projName.setText(data.getStringExtra("projectName"));
		imageString = data.getStringExtra("imageString");
		separated = imageString.replace("|", ",").split(",");
		//noImages.setText(String.valueOf(separated.length) + " image(s)");
		MyPagerAdapter adapter = new MyPagerAdapter();
	    ViewPager myPager = (ViewPager) findViewById(R.id.imagepanelpager);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(0);
	    myPager.setHorizontalFadingEdgeEnabled(true);
		}if (resultCode == RESULT_CANCELED) {    
	         //Write your code on no result return 
	     }
	}else if (requestCode == 2) {
		if(resultCode == RESULT_OK){
			noImages.setText(data.getStringExtra(String.valueOf("noimges"))+ " image(s)");
			projectName = data.getStringExtra("projectName");
			projName.setText(data.getStringExtra("projectName"));
			imageString = data.getStringExtra("imageString");
			separated = imageString.replace("|", ",").split(",");
			//noImages.setText(String.valueOf(separated.length) + " image(s)");
			MyPagerAdapter adapter = new MyPagerAdapter();
		    ViewPager myPager = (ViewPager) findViewById(R.id.imagepanelpager);
		    myPager.setAdapter(adapter);
		    myPager.setCurrentItem(0);
		    myPager.setHorizontalFadingEdgeEnabled(true);
			}if (resultCode == RESULT_CANCELED) {    
		         //Write your code on no result return 
		     }
		}
	
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("HA", "Finishing");
		// Toast.makeText(ActivityResturantList.this,"hiiiiiiiii",Toast.LENGTH_LONG).show();

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

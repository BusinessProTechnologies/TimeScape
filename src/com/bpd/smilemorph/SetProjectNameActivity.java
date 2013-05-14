package com.bpd.smilemorph;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bpd.database.DatabaseHandler;
import com.bpd.utils.IabHelper;
import com.bpd.utils.IabResult;
import com.bpd.utils.Inventory;
import com.bpd.utils.Purchase;

public class SetProjectNameActivity extends Activity implements OnClickListener {
	ImageView nextBtn,cancelBtn;
	EditText projectName;
	String projName;
	AlertDialog.Builder builder;
	private DatabaseHandler myDbHelper;
	public static SharedPreferences myPrefs;
	SQLiteDatabase db;
	final Context context = this;
	// The helper object
    IabHelper mHelper;
    static final String SKU_GAS = "time.scape";
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createproject);
		projectName = (EditText)findViewById(R.id.projectName);
		nextBtn = (ImageView) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
		cancelBtn = (ImageView) findViewById(R.id.cancelProjName);
		cancelBtn.setOnClickListener(this);
		
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzze2c5Ny9nYU+GPAMqNIJ+JoxhtVkMFd2DuPMbP2xzem0UDCy0NH7zTrwjqgS07t7OqoQ9eoi6997aPkhV92D1Vabjt6m/oL+rH47UVxV3mLqzoIBspsmXSLyN4Tn/kBosZUtMExei0/ZzxOMGp3ZzelBFQNlt98bT9iVE0TWKk9nsRNSpt0F43yHhZLSBhJ2btP5Ex1fR4DrFTtMseTsTEkZxWDJ7Yj2lO04YHW/4+WQylg8MPIM8CgDKnf9nYKtVYfPexUNMTOuO1hPSWC3GggeWJjmqRtxJLQnZbjvUAVYPiqzAmVi7NCrnpgZhf5cLZ23k+1qzIJCcNXKfE9BwIDAQAB";
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		// enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d("inapp", "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d("inapp", "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d("inapp", "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
		
		/****start in app pruchase*******/
        myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		boolean enable = myPrefs.getBoolean("enable", false);
		
		myDbHelper = new DatabaseHandler(this);
		myDbHelper.initializeDataBase();
		db = myDbHelper.getWritableDatabase();
		projName = projectName.getText().toString(); 
		Cursor cursor = db.rawQuery("SELECT * FROM " + ProjectEntity.TABLE_NAME + ";", null);
		Log.i("cursor",cursor.moveToFirst()+"");
		if(cursor.moveToFirst() && !enable){ 
			
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_inapp_purchase);
			TextView dlogTl = (TextView) dialog.findViewById(R.id.dlogTl);
			dlogTl.setText(R.string.limit_msg);//"Project limit reached!");
			TextView myMsg = (TextView) dialog.findViewById(R.id.dlogMsg);
			myMsg.setText(R.string.in_app_msg);//"The free version of this app" +
					//"allows only 1 project,upgrade" +
					//"and create unlimited projects!");
			//builder.setMessage("Project name can not be blank.");
			dialog.setCancelable(false);
			ImageView inAppContinue = (ImageView) dialog
					.findViewById(R.id.inAppContinue);
			inAppContinue.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					String payload = ""; 
			        Log.i("SKU_GAS",SKU_GAS);
			        Log.i("RC_REQUEST",RC_REQUEST+"");
			        //Log.i("SKU_GAS",SKU_GAS);
			        mHelper.launchPurchaseFlow(SetProjectNameActivity.this, SKU_GAS, RC_REQUEST, 
			                mPurchaseFinishedListener, payload);
			        dialog.dismiss();
				}
			});
			ImageView inAppCancel = (ImageView) dialog
					.findViewById(R.id.inAppCancel);
			inAppCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					finish();
				}
			});
			dialog.show();
			
			myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor prefsEditor = myPrefs.edit();

			prefsEditor.putBoolean("enable", true); // value to store
			prefsEditor.commit();
			
		}
		myDbHelper.close();
		db.close();
		
		
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
	 // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            /*Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }*/

            Log.d("inapp", "Purchase successful.");

            if (purchase.getSku().equals(SKU_GAS)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d("inapp", "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
            /*else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");
                mIsPremium = true;
                updateUi();
                setWaitScreen(false);
            }
            else if (purchase.getSku().equals(SKU_INFINITE_GAS)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing to infinite gas!");
                mSubscribedToInfiniteGas = true;
                mTank = TANK_MAX;
                updateUi();
                setWaitScreen(false);
            }*/
        }
    };
 // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d("inapp", "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d("inapp", "Consumption successful. Provisioning.");
               // mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                alert("You are done");
            }
            else {
                complain("Error while consuming: " + result);
            }
            //updateUi();
            //setWaitScreen(false);
            Log.d("inapp", "End consumption flow.");
        }
    };
    void complain(String message) {
        Log.e("inapp", "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d("inapp", "Showing alert dialog: " + message);
        bld.create().show();
    }
 // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d("inapp", "Query inventory finished.");
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d("inapp", "Query inventory was successful.");
            
            
             /** Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().*/
             
            
            // Do we have the premium upgrade?
           /* Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));*/
            
            // Do we have the infinite gas plan?
            /*Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
            mSubscribedToInfiniteGas = (infiniteGasPurchase != null && 
                    verifyDeveloperPayload(infiniteGasPurchase));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE") 
                        + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;*/

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d("inapp", "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
                return;
            }

            //updateUi();
            //setWaitScreen(false);
            Log.d("inapp", "Initial inventory query finished; enabling main UI.");
        }
    };
    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        
        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         * 
         * WARNING: Locally generating a random string when starting a purchase and 
         * verifying it here might seem like a good approach, but this will fail in the 
         * case where the user purchases an item on one device and then uses your app on 
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         * 
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         * 
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on 
         *    one device work on other devices owned by the user).
         * 
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */
        
        return true;
    }

}

package com.bpd.smilemorph;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import com.bpd.smilemorph.R;

public class NewMorphActivity extends Activity{

	AlertDialog.Builder builder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newmorph);
		
		builder = new AlertDialog.Builder(this);
		builder.setTitle("Name Your Project");
    	builder.setMessage("Add item name");

    	final EditText savedText = new EditText(this);
    	builder.setView(savedText);
        //builder.setIcon(0);

        builder.setPositiveButton("Save", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            
               String val = savedText.getText().toString().trim();
               
        	   
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	  public void onClick(DialogInterface dialog, int which) {
        	    // Canceled.
        	  }
        	});
        builder.show();
		
	}
}

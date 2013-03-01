package com.bpd.smilemorph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ExistingMorphActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_existmorph);
		
		Button backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		
		Button addBtn = (Button) findViewById(R.id.addMorphBtn);
		addBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn) {
			finish();
		}else if(v.getId() == R.id.addMorphBtn) {
			Intent intent = new Intent(ExistingMorphActivity.this, CreateMorphActivity.class);
			startActivity(intent);
		}
	}
}

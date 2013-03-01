package com.bpd.smilemorph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SetProjectNameActivity extends Activity implements OnClickListener {
	Button nextBtn,cancelBtn;
	EditText projectName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createproject);
		projectName = (EditText)findViewById(R.id.projectName);
		nextBtn = (Button) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.cancelProjName);
		cancelBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.nextBtn) {
			Intent intent = new Intent(SetProjectNameActivity.this, CreateMorphActivity.class);
			startActivity(intent);
		}else if(v.getId() == R.id.cancelProjName) {
			finish();
		}
	}
}

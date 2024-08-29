package com.sparkdevteam.khrakovrun;

import com.sparkdevteam.khrakovrun.Game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
	}
	
	public void play(View v)
	{
		Intent i=new Intent(this,Game.class);
		startActivity(i);
	}
	public void instructions(View v)
	{
		Intent i=new Intent(this,instructions.class);
		startActivity(i);
	}
	public void about(View v)
	{
		Intent i=new Intent(this,about.class);
		startActivity(i);
	}
	public void exit(View v)
	{
		System.exit(0);
	}
}

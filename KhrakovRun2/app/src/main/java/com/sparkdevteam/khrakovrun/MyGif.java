package com.sparkdevteam.khrakovrun;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.Log;


public class MyGif{
	public static final String TAG = "test";
	Movie movie;
	InputStream is = null;
	boolean once;
	long moviestart;
	int id, id1, id2, id3, id4=-1000;

	//public MyGif()
	
	public MyGif( int ID, boolean Once, Context Context)
	{
		changeGif (ID, Once,Context);
	}
	
	public MyGif( int ID1, int ID2, int ID3, int ID4, boolean Once, Context Context)
	{
		id1=ID1; id2=ID2;id3=ID3;id4=ID4; once = Once;
		changeGif(Context, 1);
	}
	public void changeGif (Context Context, int State) //redndant
	{
		int ID=State==1?id1:State==2?id2:State==3?id3:id4;		
		changeGif ( ID, once, Context);
	}
	public void changeGif (Context Context, int State, boolean Once)
	{
		int ID=State==1?id1:State==2?id2:State==3?id3:id4;		
		changeGif ( ID, Once, Context);
	}
	public void changeGif ( int ID, boolean Once, Context Context)
	{
		if(id==ID)
		{
			return;
		}
			
		id=ID; 
		once = Once;
		is = null;
		movie = null;
		gifView(Context);	
	}
	
	public void gifView(Context Context)
	{
		is=Context.getResources().openRawResource(id);
		movie = Movie.decodeStream(is);
		moviestart = 0;
	}
	
	public void gifDraw(Canvas Canvas, float X, float Y)
	{
		long now=android.os.SystemClock.uptimeMillis();
		int relTime=0;
		if(moviestart == 0){moviestart = now;}	//first time if 0

		if (movie==null)
		{
			Log.e("MyGif", "movie is null");
			return;
		}
		if(movie.duration()>0) { //*******
			if (movie==null)
			{
				Log.e("MyGif", "movie is null");
				return;
			}
			relTime = (int) ((now - moviestart) % movie.duration());
		}
		if (movie==null)
		{
			Log.e("MyGif", "movie is null");
			return;
		}
		if(now - moviestart >= movie.duration() && once)
		{
			if (movie==null)
			{
				Log.e("MyGif", "movie is null 1");
				return;
			}
			movie.setTime(movie.duration());
		}
		else {
			if (movie==null)
			{
				Log.e("MyGif", "movie is null 1");
				return;
			}
			movie.setTime(relTime);
		}
		if (movie==null)
		{
			Log.e("MyGif", "movie is null 2");
			return;
		}
		movie.draw(Canvas, X, Y);

	}

	public void pause(boolean b) {
		if (b)
		movie.setTime(movie.duration());
		else {
			long now = android.os.SystemClock.uptimeMillis();
			int relTime = (int) ((now - moviestart) % movie.duration());
			movie.setTime(relTime);
		}
	}
}
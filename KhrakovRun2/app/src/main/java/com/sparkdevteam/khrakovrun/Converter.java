package com.sparkdevteam.khrakovrun;

import android.content.Context;
import android.util.DisplayMetrics;

public class Converter {
	
	public int dpToPx(int dp, Context Context) {
	    DisplayMetrics displayMetrics = Context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
	public int pxToDp(int px, Context Context) {
	    DisplayMetrics displayMetrics = Context.getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	    return dp;
	}
	public int width(Context Context) {
	    DisplayMetrics displayMetrics = Context.getResources().getDisplayMetrics();
	    return displayMetrics.widthPixels;}
}

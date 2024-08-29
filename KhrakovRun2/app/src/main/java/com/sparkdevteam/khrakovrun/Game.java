package com.sparkdevteam.khrakovrun;
import com.sparkdevteam.khrakovrun.GameLoop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("DrawAllocation")
public class Game extends Activity{
	
	@Override
	protected void onCreate (Bundle SaveInstanceState)
	{
		super.onCreate(SaveInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(new GameView(this));
	}

//----------------------------------------------------------
	  	
	public class GameView extends View
	{
		
		GameLoop gameLoopThread = new GameLoop(this);
		Context contextt;

		private boolean intro = true;	
		public int level=3, score=0, time=0, count=201; //, radius, cHeight, bar;
		
		DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
		int screenWidth=displayMetrics.widthPixels;
		int screenHeight=displayMetrics.heightPixels;

		private int radius = pxToDp(300), cHeight=screenHeight-10, bar = radius*2;
		
		Bitmap pl;
		Bitmap bk;
		Bitmap introBk;
		
		Human h;
	
		platform p_1;
		platform p_2;
		
		enemies eB1;      //back most
		enemies eF1;  	//runner
		enemies eG1;//this.getWidth()); //front liner
		
		items i;
		items iS;
		items iH1;

		Paint myPaint = new Paint();

		boolean isPaused = false;

		private GameView(Context context){
			super(context) ;
			contextt = context;
									
			//Paint p = new Paint();
			myPaint.setAntiAlias(true);
			setLayerType(LAYER_TYPE_SOFTWARE, myPaint);

			//sets plateform and background according to game level
			switch(level)
			{
				case 1: pl=BitmapFactory.decodeResource(getResources(), R.drawable.p1);
						bk=BitmapFactory.decodeResource(getResources(), R.drawable.abk1);
						introBk = BitmapFactory.decodeResource(getResources(), R.drawable.map1);
						break;
				case 2: pl=BitmapFactory.decodeResource(getResources(), R.drawable.p2);
						bk=BitmapFactory.decodeResource(getResources(), R.drawable.abk2);
						introBk = BitmapFactory.decodeResource(getResources(), R.drawable.map2);
						break;
				case 3: pl=BitmapFactory.decodeResource(getResources(), R.drawable.p3);
						bk=BitmapFactory.decodeResource(getResources(), R.drawable.abk3);
						introBk = BitmapFactory.decodeResource(getResources(), R.drawable.map3);
						break;
				case 4: pl=BitmapFactory.decodeResource(getResources(), R.drawable.p4);
						bk=BitmapFactory.decodeResource(getResources(), R.drawable.abk4);
						introBk = BitmapFactory.decodeResource(getResources(), R.drawable.map4);
						break;
			} 
			h = new Human(this, screenWidth/2);
			p_1= new platform(this, screenHeight - radius*2, pxToDp(0)); //base, weigth, xpos
			p_2= new platform(this, screenHeight/2, pxToDp(-10000));
			eB1=new enemies(this, 1, 50, screenWidth/4);      //back most
			eF1=new enemies(this, 2, 50, screenWidth);  	//runner
			eG1=new enemies(this, 3, 50, screenWidth*3/4);//this.getWidth()); //front liner
			i=new items(this, 1, pxToDp(600));
			iS=new items(this, -1, pxToDp(500));
			iH1=new items(this, 0, pxToDp(-screenWidth));
			
			 gameLoopThread.start();
			 gameLoopThread.run();
			 gameLoopThread.setRunning(true);
	    }
        public void init(Context Context){
            switch(level)
            {
                case 1: pl=BitmapFactory.decodeResource(getResources(), R.drawable.p1);
                    bk=BitmapFactory.decodeResource(getResources(), R.drawable.abk1);
					introBk = BitmapFactory.decodeResource(getResources(), R.drawable.map1);
					break;
                case 2: pl=BitmapFactory.decodeResource(getResources(), R.drawable.p2);
                    bk=BitmapFactory.decodeResource(getResources(), R.drawable.abk2);
					introBk = BitmapFactory.decodeResource(getResources(), R.drawable.map2);
					break;
                case 3: pl=BitmapFactory.decodeResource(getResources(), R.drawable.p3);
                    bk=BitmapFactory.decodeResource(getResources(), R.drawable.abk3);
					introBk = BitmapFactory.decodeResource(getResources(), R.drawable.map3);
					break;
                case 4: pl=BitmapFactory.decodeResource(getResources(), R.drawable.p4);
                    bk=BitmapFactory.decodeResource(getResources(), R.drawable.abk4);
					introBk = BitmapFactory.decodeResource(getResources(), R.drawable.map4);
					break;
            }

            h.init(this, screenWidth/2);

            p_1.init(this, screenHeight - radius*2, pxToDp(0)); //base, weigth, xpos
			p_2.init(this, screenHeight/2, pxToDp(-10000));

            eB1.init(this, 1, 50, screenWidth/4);      //back most
            eF1.init(this, 2, 50, screenWidth);  	//runner
            eG1.init(this, 3, 50, screenWidth*3/4);//this.getWidth()); //front liner

            i.init(this, 1, pxToDp(600));
            iS.init(this, -1, pxToDp(500));
            iH1.init(this, 0, pxToDp(-screenWidth));
        }
        public void update(){
			if (isPaused) {return;}

            if ((h.lose || h.win) && count < 200){intro=true; isPaused=true;}

            //if(intro) count+=1;

            if(intro&&count==200){
                if(h.lose)eF1.enimies.moviestart=0;
                if(h.win) {score+=eF1.deadcount; level+=1;}
                init(contextt);
            }
	/*
            if(intro&&count==400){
                //intro=false; count=0; //h.lose=false; h.win=false;
            }
	*/
            if(!intro)
            {
                p_1.updateOtherNiggas(h, eF1, eB1, eG1, p_2, i, iS, iH1, this);
                p_2.updateOtherNiggas(h, eF1, eB1, eG1, p_1, i, iS, iH1, this);

                i.update(this);
                iS.update(this);
                iH1.update(this);

                eF1.update(this);
            }

            if(!intro||intro&&count<200){
                eB1.update(this);
                eG1.update(this);
                h.update(this, eF1, eB1, eG1, i, iS);
            }
            time+=1;
        }

		@Override protected void onDraw(Canvas canvas){
			canvas.drawColor(Color.TRANSPARENT);
			super.onDraw(canvas);
			

			String powUp;
			//Paint myPaint = new Paint(),	xPaint = new Paint();
		    myPaint.setColor(Color.WHITE); 	
		    myPaint.setStrokeWidth(10);		
		    myPaint.setAntiAlias(true);		
		    myPaint.setFakeBoldText(true);	
		    myPaint.setTextSize(radius/2);
		  		    
			if(intro&&count>100)
			{
				canvas.drawBitmap(introBk,- (introBk.getWidth()-screenWidth)/2,- (introBk.getHeight()-screenHeight)/2, null);
				int year = h.win?level+1:level;
				myPaint.setTextAlign(Align.CENTER);
				myPaint.setColor(Color.DKGRAY);

				myPaint.setTextSize(pxToDp(200));
				canvas.drawText("YEAR " + year, screenWidth / 2, screenHeight * 2/10, myPaint);

				myPaint.setTextSize(pxToDp(100));
				String message= year==1?"You are a foreign student in Ukraine. You do not know anyone except for Mr Charles, the scary man that got you into the National university of Ukraine. Unfortunately, Mr Charles charges you money for his assistance and you do not have anymore money to give. Your only chance of survivor is to make friends and confine in your university peers.":
								year==2?"Congratulations, you made it to second year. You now know your way around the university. Your confidence is sky high. It feels like a great time to take a break from intense studying and have some fun. Who knows, maybe you will meet someone special.":
								year==3?"Attention! Attention! A civil war as broken out in Ukraine. All major cities including Kharkiv are affected. Student safety has been compromised. When all hell brakes loose, neither your University nor the government is responsible for your safety, but perhaps you can still find refuge in the church":
										"Rejoice! You are a survivor. The war is over, and just in time for your fourth and final year studies. There is no time to waste! You must deal will you postwar  traumas, get your head in the game, and finish what you started.";

			    //canvas.drawText(message, screenWidth/2, screenHeight*4/10 , myPaint);

				{
					//String text = "This is some text. This is some text. This is some text. This is some text. This is some text. This is some text.";

					TextPaint myTextPaint = new TextPaint();
					myTextPaint.setAntiAlias(true);
					myTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
					//myTextPaint.setColor(0x00FF0000);
					myTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
					myTextPaint.setStrokeWidth(3);
					myTextPaint.setColor(0xFF000000);
					//int padding = pxToDp(16);
					int padding = screenWidth/50;
					int width = screenWidth - (2*padding);
					int top = screenHeight*3/10;
					Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
					float spacingMultiplier = 1;
					float spacingAddition = 0;
					boolean includePadding = true;
					StaticLayout myStaticLayout = new StaticLayout(message, myTextPaint, width, alignment, spacingMultiplier, spacingAddition, includePadding);
					float height = myStaticLayout.getHeight();

					//myPaint.setStyle(Paint.Style.FILL);
					//canvas.drawRect(0, top, screenWidth, height, myPaint);

					canvas.translate(padding, top);
					myStaticLayout.draw(canvas);
					canvas.restore();
				}

			     //if(intro&&count>200) {
					 myPaint.setColor(Color.RED);
					 canvas.drawCircle(screenWidth/2, screenHeight*8/10, screenHeight * 1/10 , myPaint);
					 myPaint.setColor(Color.WHITE);
					 myPaint.setTextSkewX(-0.25f);
					 canvas.drawText ("START",screenWidth/2,  screenHeight*8/10, myPaint);
					 myPaint.setTextSkewX(0);
				 //}
				
			}	
			else //if (intro && count==100)
			{	
				canvas.drawBitmap(bk,(pxToDp(bk.getWidth())-screenWidth)*h.humanX/screenWidth,0, null);
				/*float scale = screenHeight/pxToDp(bk.getHeight());
				float left = (pxToDp(bk.getWidth())-screenWidth)*h.humanX/screenWidth;
				RectF rect = new RectF(left,pxToDp(bk.getWidth())*scale,0,pxToDp(bk.getHeight())*scale);
				canvas.drawBitmap(bk,null,rect,null);
				*/
				Log.d("Game", "item(H) gifDraw");
				iH1.items.gifDraw(canvas,iH1.getItemsX(), iH1.getItemsY());
				
				canvas.drawBitmap(pl, p_1.getXPos(), p_1.getYPos()- 40, null);
				canvas.drawBitmap(pl, p_2.getXPos(), p_2.getYPos()- 40, null);

				if (!isPaused) {
					Log.d("Game", "item gifDraw");
					i.items.gifDraw(canvas, i.getItemsX(), i.getItemsY());
					Log.d("Game", "item(S) gifDraw");
					iS.items.gifDraw(canvas, iS.getItemsX(), iS.getItemsY());

					Log.d("Game", "enimie(F) gifDraw");
					eF1.enimies.gifDraw(canvas, eF1.getEnimiesX(), eF1.getEnimiesY());
					Log.d("Game", "enimie(B) gifDraw");
					eB1.enimies.gifDraw(canvas, eB1.getEnimiesX(), eB1.getEnimiesY());
					Log.d("Game", "enimie(G) gifDraw");
					eG1.enimies.gifDraw(canvas, eG1.getEnimiesX(), eG1.getEnimiesY());

					Log.d("Game", "human gifDraw");
					h.human.gifDraw(canvas, h.getHumanX(), h.getHumanY());
				}

				if (isPaused && intro)
				{
					eB1.enimies.gifDraw(canvas, eB1.getEnimiesX(), eB1.getEnimiesY());
					Log.d("Game", "enimie(G) gifDraw");
					eG1.enimies.gifDraw(canvas, eG1.getEnimiesX(), eG1.getEnimiesY());
				}
				//------------------------------------------------------------------------
				int left = screenWidth/2 - radius/2;
				int top = cHeight- radius*3/2;
				int width = screenWidth/2 - radius/4;
				int hieght = cHeight-radius/2;

				myPaint.setFakeBoldText(true);
				//myPaint.setTextSize(radius);
				myPaint.setTextAlign(Align.LEFT);
				int circleCenter = screenWidth / 2 + 3*radius/4;
				myPaint.setColor(Color.WHITE);
				canvas.drawRect(circleCenter, cHeight - radius * 3 / 2, (screenWidth / 2) + (2*radius) + bar, cHeight - radius / 2, myPaint);
				myPaint.setColor(Color.BLACK);
				canvas.drawText("   CONTINUE", circleCenter, cHeight-radius*3/4, myPaint);

				if (intro){
					//continue button ***********************************************************
					//------------------------------------------------------------------------
					myPaint.setColor(Color.RED);
					canvas.drawCircle(screenWidth/2, cHeight - radius, radius, myPaint);
					myPaint.setColor(Color.WHITE);
					canvas.drawLine(screenWidth / 2 - radius / 2, cHeight - radius * 3 / 2, screenWidth / 2 + radius / 2, cHeight - radius, myPaint );
					canvas.drawLine(screenWidth / 2 - radius / 2, cHeight - radius / 2, screenWidth / 2 + radius / 2, cHeight - radius , myPaint);
					//canvas.drawRect(screenWidth/2 + radius/2, cHeight-radius*3/2, screenWidth/2 + radius/4, cHeight-radius/2, myPaint);
				} else {
					//pause button ***********************************************************
					//------------------------------------------------------------------------
					myPaint.setColor(Color.WHITE);
					canvas.drawCircle(screenWidth / 2, cHeight - radius, radius, myPaint);
					myPaint.setColor(Color.BLACK);
					canvas.drawRect(screenWidth / 2 - radius / 2, cHeight - radius * 3 / 2, screenWidth / 2 - radius / 4, cHeight - radius / 2, myPaint);
					canvas.drawRect(screenWidth / 2 + radius / 2, cHeight - radius * 3 / 2, screenWidth / 2 + radius / 4, cHeight - radius / 2, myPaint);
				}

				//------------------------------------------------------------------------
				//powerup*****************************************************************
				//------------------------------------------------------------------------
				myPaint.setColor(Color.WHITE);//+ 3*radius/4
				canvas.drawRect(5*radius/4, cHeight-radius-radius/2, (5*radius/2) + bar, cHeight-radius+radius/2, myPaint);

				switch(h.getPrevstate()){
				  case 3:  myPaint.setColor(Color.GREEN); 	powUp=" SHAWAMA"; break;
				  case 6:  myPaint.setColor(Color.BLUE);	powUp="  VODKA"; break;
				  case 9:  myPaint.setColor(Color.CYAN);	powUp="  MANKA"; break;
				  case 12: myPaint.setColor(Color.MAGENTA);	powUp=" BYCICLE"; break;
				  case 15: myPaint.setColor(Color.YELLOW);	powUp="  METRO"; break;
				  default: myPaint.setColor(Color.GRAY);	powUp="POWER UP"; break;
				  }

				if(h.powerout>0){
					canvas.drawRect(radius, cHeight-radius-radius/3, 2*radius+h.powerout*bar/300, cHeight-radius+radius/3, myPaint);
				}

				canvas.drawCircle(radius, cHeight-radius, radius, myPaint);

				//---------------------------------------

				if(h.powerout<=0) {
					myPaint.setColor(Color.GRAY);
				} else {
					myPaint.setColor(Color.WHITE);
				}



				canvas.drawText(powUp, radius * 2, cHeight-radius*3/4, myPaint);

				//--------------------------------------

				myPaint.setColor(Color.RED);

				canvas.drawLine(radius, cHeight-radius, radius+20, cHeight-radius+20, myPaint);
				canvas.drawLine(radius, cHeight-radius, radius-20, cHeight-radius+20, myPaint);
				canvas.drawLine(radius, cHeight-radius, radius-20, cHeight-radius-20, myPaint);
				canvas.drawLine(radius, cHeight-radius, radius+20, cHeight-radius-20, myPaint);

			}

			this.invalidate();
		}
        @Override public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_DOWN)
            {
            	final int x = (int)event.getX();
				final int y = (int)event.getY();
                //if(2*radius > (int)event.getX() && cHeight-2*radius<(int)event.getY()) //PRECISE
				if(intro && !isPaused){//&&count==400){
					intro=false; count=0; //h.lose=false; h.win=false;
				}
                if(4*radius+bar > (int)event.getX() &&  cHeight-4*radius<(int)event.getY()) {
                    //TODO : POWER OUT SOUND
                    h.powerout=0;
                }
                else if (x < screenWidth/2 + radius && x > screenWidth/2 - radius && y > cHeight - (2*radius)){
                	isPaused = !isPaused;
					if(intro) count = 200;
					else if (isPaused) {

					}
				}
                else {
                    h.setJump();
                }
            }
            return true;
        }

        public int dpToPx(int dp) {
		    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
			   return px;
		}
        public int pxToDp(int px) {
            int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return dp;
        }
	}
}



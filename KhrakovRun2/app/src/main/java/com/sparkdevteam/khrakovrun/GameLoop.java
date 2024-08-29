package com.sparkdevteam.khrakovrun;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

import com.sparkdevteam.khrakovrun.Game.GameView;

public class GameLoop  extends Thread {

   GameView view;
   static final long FPS = 10;
   private boolean running = false;
   boolean isPaused = false;

   public GameLoop(GameView view) {this.view = view; }

   public void setRunning(boolean run) {running = run; }

   public void setPause(int i){ isPaused = i > 0 ;}

   @SuppressLint("WrongCall")
   @Override
   public void run() {
	   long ticksPS =  20 - view.level*5;
	   long startTime = 0;
	   long sleepTime;
	   while (running) {
		  //pause and resume
			if (isPaused) {
				  try {
					Thread.sleep(50);
				  }
				  catch (InterruptedException e) {
					e.printStackTrace();
				  }
			}
			else{
				 Canvas c = null;
				 startTime = System.currentTimeMillis();
				 try {
					 view.update();
				 }
				 finally {
					 if (c != null) {
					 }
				 }
			}

			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);

			try {sleep((sleepTime > 10) ? sleepTime : 10);}
			catch (Exception e) {}

	  }
   }
}

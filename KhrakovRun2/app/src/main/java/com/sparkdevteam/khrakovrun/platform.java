package com.sparkdevteam.khrakovrun;

import java.util.Random;

import android.graphics.Bitmap;

public class platform {
	public Bitmap platform;
	private int xPos, yPos,sectionEdge,sectionBase, sections;
	private int metroNeedAndHieght;
	public int sectionWeigth, otherSectionBase,otherXPos;
	Random rand = new Random(); 
	
	public platform(Game.GameView mc ,  int SectionBase, int XPos){
		init(mc ,  SectionBase,  XPos);
	}
	public void init(Game.GameView mc ,  int SectionBase, int XPos){
	
		sectionBase = SectionBase;
		yPos = sectionBase;    //actual stage base
		otherSectionBase = sectionBase;
		metroNeedAndHieght=0;

		platform = mc.pl; 
		xPos = XPos;
	//	sectionWeigth=900;///guess !!!!!!!!!!!!!!!!!!!!!!
		sectionEdge= xPos + sectionWeigth;
		otherXPos=sectionEdge;
		sections = 1;
	}
	
	public void updateOtherNiggas(Human H, enemies EF1, enemies EB1,  enemies EG1, platform P, items I,items IS, items IH, Game.GameView mc){
		//picking correct plateform image according to update.
		sectionWeigth=platform.getWidth();
		otherSectionBase = P.sectionBase;
		otherXPos = P.xPos;
		
		moveSection(P.xPos + P.sectionWeigth + 5*20, P.sectionBase, mc, P.sections );

		yPos = sectionBase;  //
		
		H.PlatformToHuman(xPos, sectionEdge, sectionBase, sectionWeigth); //int PxPos, int PsectionEdge, int PsectionBase, int PsectionWeigth
		EF1.PlatformToEnimies(xPos, sectionEdge, sectionBase, otherSectionBase,otherXPos);
		EB1.PlatformToEnimies(xPos, sectionEdge, sectionBase, otherSectionBase,otherXPos);
		EG1.PlatformToEnimies(xPos, sectionEdge, sectionBase, otherSectionBase,otherXPos);
		I.PlatformToitems(xPos, sectionEdge, sectionBase, sectionWeigth, metroNeedAndHieght);
		IS.PlatformToitems(xPos, sectionEdge, sectionBase, sectionWeigth, metroNeedAndHieght);
		IH.PlatformToitems(xPos, sectionEdge, sectionBase, sectionWeigth, P.sectionBase);
		//newly added
		
	}

	public int getYPos(){return yPos;}
	public int getXPos(){return xPos;}
	
	public void moveSection(int NextSectionEdge, int SectionBase, Game.GameView mc, int Sections){

		xPos-=5;
		sectionEdge= xPos + sectionWeigth;

		if(sectionEdge <= 0)  //the one offscreen
		{
			MakeNewSection(mc);
			
			metroNeedAndHieght=(sections+2<Sections)?SectionBase:0;
				
			xPos = (sectionBase > SectionBase)? NextSectionEdge+sectionBase-SectionBase:NextSectionEdge;
		}
	}

	private void MakeNewSection(Game.GameView mc){
		 
		sections = rand.nextInt(7);
		//sectionBase = (sections%2==0)? 300+sections*25 : 300-sections*25;  //*****find a better condition here
		//yPos = sectionBase;
		
		switch (sections){   //each level rotates between three sections. ***hieght=section*20
			case 0: sectionBase = mc.screenHeight/2;
				//	platform = mc.p3; 	//
					break;
			case 1:  sectionBase = mc.screenHeight/2 + (mc.screenHeight/2)*1/7;
				//	platform = mc.p3; 	//runing gif
					break;
			case 2: sectionBase = mc.screenHeight/3 +  (mc.screenHeight/2)*2/7;
				//platform = mc.p3;  	//runing gif
					break;
			case 3: sectionBase = mc.screenHeight/2 +  (mc.screenHeight/2)*3/7;
			//		platform =mc.p3; 	//runing gif
					break;
			case 4:  sectionBase = mc.screenHeight/2 + (mc.screenHeight/2)*4/7;
				//	platform = mc.p3; 	//runing gif
					break;
			case 5:  sectionBase = mc.screenHeight/2 + (mc.screenHeight/2)*5/7;
				//	platform = mc.p3;  	//runing gif
					break;
			case 6:  sectionBase =  mc.screenHeight/2 +(mc.screenHeight/2)*6/7;
				//	platform =mc.p3;  	//runing gif
					break;
			default: sectionBase = mc.screenHeight;// platform =  mc.p3;  	//runing gif
		}
		
	}
}

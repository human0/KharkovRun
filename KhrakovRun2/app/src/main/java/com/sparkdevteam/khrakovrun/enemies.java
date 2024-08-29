package com.sparkdevteam.khrakovrun;

public class enemies {

	public MyGif enimies;
	private int enimiesHeight, enimiesWidth,  enimiesY, count, state, prevState, drunk, screenEdge;
	private boolean attack, airborn, inHole;
	public int stageBase, enimiesX,enimyType,deadcount, ninja;
	private int screenWidth;
	
	public enemies(Game.GameView mc, int EnimyType, int StageBase, int EnimiesX){
		enimies = new MyGif(R.drawable.e_1, R.drawable.e_1f,0,0,false, mc.contextt);
		init( mc,  EnimyType,  StageBase,  EnimiesX);
	}
	public void init(Game.GameView mc, int EnimyType, int StageBase, int EnimiesX){
		inHole=false; ninja=0;
		
		enimyType = EnimyType;
		
		switch(enimyType){
			case(1):enimies = mc.level==1? new MyGif(R.drawable.e_1, R.drawable.e_1f,0,0,false, mc.contextt):
							  mc.level==2? new MyGif(R.drawable.e_2, R.drawable.e_2f,0,0,false, mc.contextt):
							  mc.level==3? new MyGif(R.drawable.e_3, R.drawable.e_3f,0,0,false, mc.contextt):
								  		   new MyGif(R.drawable.e_4, R.drawable.e_4f,0,0,false, mc.contextt);
					break;
			case(2):enimies = mc.level==1? new MyGif(R.drawable.test, R.drawable.test,R.drawable.test,R.drawable.test,false, mc.contextt):
				  mc.level==2? new MyGif(R.drawable.e2, R.drawable.e21d,R.drawable.e22d,R.drawable.e23d,false, mc.contextt):
				  mc.level==3? new MyGif(R.drawable.e3, R.drawable.e31d,R.drawable.e32d,R.drawable.e33d,false, mc.contextt):
					  		   new MyGif(R.drawable.e4, R.drawable.e41d,R.drawable.e42d,R.drawable.e43d,false, mc.contextt);
					break;
			case(3):enimies = mc.level==1? new MyGif(R.drawable.ep1, R.drawable.ep1f,0,0,false, mc.contextt):
				  			  mc.level==2? new MyGif(R.drawable.ep2, R.drawable.ep2f,0,0,false, mc.contextt):
					  		  mc.level==3? new MyGif(R.drawable.ep3, R.drawable.ep3f,0,0,false, mc.contextt):
					  			  		   new MyGif(R.drawable.ep4, R.drawable.ep4f,0,0,false, mc.contextt);
					break;
			default:break;
		}
		
		enimiesX = EnimiesX - enimies.movie.width()/2; //added
		screenEdge= mc.screenWidth;
		
		deadcount=0;
		enimiesHeight =0;
		state=1;prevState=1;
		enimiesY=0; //hieght = 50
		count = 0;
		
		/*
		level = 1;
		if(enimyMoves)speed = level*10; else speed = 0;
		*/
		
		
		state=1;//mc.level;
		prevState = state;
		attack = false;
		airborn= false;
		
		screenWidth = mc.screenWidth;
	}
	
	public void update(Game.GameView mc){
		 enimiesHeight=enimies.movie.height();
		 enimiesWidth=enimies.movie.width();
		 if(!(mc.level==1&&enimyType==2)){
		 	stateEngine(mc);
		 }
	}
	
	public int getEnimiesY(){return enimiesY;}
	public int getEnimiesX(){return enimiesX;}
	
	public boolean move(int JHieght, Game.GameView mc){
		//runing and jumping
		//backward enimies
		if(enimyType == 1){
			if(enimiesX < 0){//-enimiesWidth){
				enimiesX = 0;//-enimiesWidth;
			}
			if(enimiesX > enimiesWidth){
				enimiesX = enimiesWidth;
			}
			else {
				enimiesX+= attack? 1 : (-1);
			}
			
			enimiesY  = stageBase -enimiesHeight; 
			attack=false;
		}
		//forward enimies
		if(enimyType == 3){
			if(enimiesX > screenEdge - enimiesWidth){
				enimiesX = screenEdge - enimiesWidth;
			}
			if(enimiesX < screenEdge-2*enimiesWidth){
				enimiesX = screenEdge-2*enimiesWidth;
			}
			else {
				enimiesX+= attack? (-1) : 1;
			}
				
			enimiesY  = stageBase -enimiesHeight; 
			attack=false;
		}
		
		//runners
		if(enimyType == 2){      // moves the second bad guy
			enimiesX-= state==0 ? 5 : 10;

			if(enimiesX<-10||enimiesX>screenWidth){
				enimiesX= screenWidth;state=1; prevState = 1;
			}  //use random ******** if final stage

			if(!airborn&&attack){
				count+=20;
				if (count>=JHieght){
					attack=false;airborn=true;
				}
				state=-prevState;
				return true;
			}//jumping
			else if ((enimiesY + enimiesHeight< stageBase-20)){
				enimiesY =enimiesY+10;
				state=-prevState;
				airborn=true;
				return true;
			} //freefall
			else if ((enimiesY + enimiesHeight> stageBase-20&&inHole)){
				enimiesY =enimiesY-2;
				return true;
			} //freefall
			else {
				enimiesY  = stageBase -enimiesHeight;
				count=0;
				state=prevState;
				airborn=false;
				return false;
			} //landed
		}

		return true;
	}

	public void stateEngine(Game.GameView mc){
		switch (state){
			//running
			case(0):move(0, mc);
					enimies.changeGif(mc.contextt, drunk+1); //drunk is= touchAndDie=0;  //0 , 1,2 or 3
					break;
					
			case(-1):				//stage1 enimy					//jumping
			case( 1)://speed=5;			
					//if(enimyType==2)
					if(move(enimiesHeight, mc));//else move(0, mc);
					enimies.changeGif(mc.contextt, 1);
				//	enimies = enimyType==1? mc.e_: enimyType==2? mc.e : mc.ep;//if(action())enimies =  mc.getImage(url,"h2A.gif")
					break;
					
			//you lose		
			case(99):
				//if(!move(enimiesHeight, mc)) move(0, mc);
				enimies.changeGif(mc.contextt, 2,true);// enimies= mc.e_f;

				//enimies.changeGif(mc.contextt, 2,true);// enimies= mc.e_f;
				enimiesY  = stageBase - enimies.movie.height();
			break;
			
			//you win
			case(999): enimies.changeGif(mc.contextt, 2,true);//enimies= mc.epf;
			enimiesY  = stageBase - enimies.movie.height(); enimiesX-=1;
			break;
		/*	case(-2):
			case(2):								//normal jump 
					//gainSpeed(true, 1);			//normal freefall
					move(enimiesHeight/2, mc);		
					enimies = mc.getImage(url,"e-1.gif");//if(action())enimies =  mc.getImage(url,"h2A.gif")
		*/		//	break;
		}
	}

	public void PlatformToEnimies(int PxPos, int PsectionEdge, int PsectionBase, int OtherSectionBase, int OtherPxPos) {
		//int PxPos, int PsectionEdge, int PsectionBase, int PsectionWeigth
		//stageSpeed = PstageSpeed;
		if((enimiesX+enimiesWidth>=PxPos) && (enimiesX<PsectionEdge))
			{
			if((enimiesX+enimiesWidth>=PxPos&&enimiesX<=PxPos) || (enimiesX<PsectionEdge&&enimiesX+enimiesWidth>PsectionEdge)) //an edge
				{if(!inHole){ inHole=true; ninja=OtherSectionBase;}}
			else {inHole=false; stageBase = PsectionBase;}
			}
		if(inHole){stageBase=ninja;
		}
	//	else{ if(PsectionBase<OtherSectionBase)stageBase -= 20*OtherSectionBase/PsectionBase; else stageBase += 20*PsectionBase/OtherSectionBase;}
	}

	public boolean HumanToEnimies(int HumanX, int HumanWidth,int HumanY, int HumanHeight,int TouchAndDie) {
	attack=false;
			
	if(((enimyType ==2)&& (enimiesX+enimiesWidth/2>= HumanX) && (enimiesX+enimiesWidth/2<= HumanX+2*HumanWidth))
	||(((enimyType == 1)&& (enimiesX+3*enimiesWidth>= HumanX))
	||((enimyType == 3)&& (enimiesX-3*enimiesWidth<= HumanX))))
		attack=true;     //in range to attack
	
	if((enimyType == 1)&& (enimiesX+enimiesWidth*2/4>HumanX)){state = 99; return true;}
	
	else if((enimyType == 3)&& (enimiesX<HumanX+HumanHeight/4)){state = 999; enimiesX=HumanX; return true;} //won or lost
	
	else if((enimyType == 2 && prevState != 0)&&
	(
	((((enimiesX+enimiesWidth>= HumanX+HumanWidth*3/4) && (enimiesX<= HumanX+HumanWidth/4)
	&& (enimiesY+enimiesHeight>= HumanY+HumanHeight*3/4) && (enimiesY<= HumanY+HumanHeight/4)))  //general test
	||((TouchAndDie==1)&& ((enimiesX+enimiesWidth>= HumanX+HumanWidth*3/4) && (enimiesX<= HumanX+HumanWidth/4)))))  //vodka test
	)	
	{if(TouchAndDie!=0){deadcount+=1; prevState=0; state=0; drunk = TouchAndDie; } else { return true; }
	}
	
	return false; //player was not hurt
}

}

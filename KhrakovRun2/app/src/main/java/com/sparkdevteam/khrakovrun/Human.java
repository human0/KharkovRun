package com.sparkdevteam.khrakovrun;

public class Human {
	private int humanHeight, humanWidth,  humanY, count, painCount,  speed, temp, touchAndDie, wait;
	private boolean hurt, inHole, airborn, action, stuck, invulnerable, outOfHole; 
	public int stageBase, humanX, state, prevState, powerout, humanNum;
	public boolean pause, jump, lose, win;
	private int screenWidth;
	public MyGif human;
	
	public Human(Game.GameView mc, int HumanX){
		screenWidth = mc.screenWidth;
		human = new MyGif(R.drawable.h1, false, mc.contextt);  //****
		init(mc, HumanX);
	}

	public void init(Game.GameView mc, int HumanX){
		human.changeGif(R.drawable.h1, false, mc.contextt);	//******
		humanNum=0;
		
		stageBase = 0;		
		action=false;
		painCount=-50;
		invulnerable=false;
	//	humanHeight =0;
		state=0;
		prevState=0;
		humanX=HumanX;
		humanY=60; 
		count = 0;
		speed = 0;
		wait=20;
		pause=false;
		lose=false;
		win=false;
		jump = false;
		hurt = false;
		touchAndDie=0;  //0 , 1,2 or 3
		inHole=false;
		outOfHole = true;
		powerout=0;
	}
	
	public void update(Game.GameView mc, enemies EF1, enemies EB1, enemies EG1, items I, items IS){
		humanHeight=human.movie.height();
		humanWidth=human.movie.width();
		stateEngine(mc);
		inPain();
		wait=20;
		
		temp = I.HumanToitems(humanX, humanWidth, humanY, humanHeight);
		if(temp!=0){prevState = temp;state=temp; powerout=300; }//count=humanHeight;}//count=0;} 
		
		powerout--;
		if(powerout<0&&prevState>0){
			if((prevState==15)){action=true; humanX+=50;}
			else prevState=0;}
		temp = IS.HumanToitems(humanX, humanWidth, humanY, humanHeight);
		if(temp!=0) {
			stuck= true ;
		}
		

		hurt= !hurt? EF1.HumanToEnimies(humanX, humanWidth,humanY, humanHeight, touchAndDie) : hurt;
		if( EB1.HumanToEnimies(humanX, humanWidth, humanY, humanHeight, touchAndDie)) {lose=true; state = 99; prevState = 99;} //mc.transit(0);//dead
		if( EG1.HumanToEnimies(humanX, humanWidth, humanY, humanHeight, touchAndDie)){win=true; state = 99; prevState = 99;} //mc.transit(1);//won		 
	}
	public int getHumanY(){return humanY;}
	public int getHumanX(){return humanX;}
	public void setJump(){
		if(!airborn&&((!stuck&&!hurt)||invulnerable)&&!jump){   //to prevent form jumping while already jumping
			prevState=state;
			jump = true;}
	//TODO :	ply sound......if (shawama)eho d mess
	}

	public boolean move(int JHieght, int UpSpeed, int DownSpeed){
		//runing and jumping
		//UpSpeed = 4;
		//DownSpeed = 4;
		humanX += speed * 2;

		if(jump || action){
			count+=UpSpeed;
			if(jump){
				humanY -=UpSpeed;
			}
			airborn=true;
			if(count>=JHieght){
				jump=false;
				action=false;
			}
			state=prevState+1;
			return true;
		}//jumping
		else if((humanY + humanHeight*5/4< stageBase)){
			humanY +=DownSpeed;
			state=prevState+2;
			airborn=true;
			return true;
		} //freefall
		else {
			humanY  = stageBase -humanHeight;
			count=0;
			state=prevState;
			airborn=false;
			return false;
		} //landed
	}

	public void inPain() {
		if(invulnerable || prevState == 15){
			; //TODO - REFACTOR FUNNY
		}
		else if (inHole){
			if(state !=-3){
				state=-3;
				invulnerable=true;
				outOfHole=false;
			} //play entered hole
			else if(outOfHole) {
				inHole=false;
				invulnerable=false;
				prevState = 0;
				state=prevState;
			}
		//else state = -2;
		}
		else if (stuck )
		{
			if(state == 0){painCount=wait; state=-2; } //play entered spicks
			else if(prevState>0){
				stuck=false;
				if(state==13){
					jump=true;
				}/*burst this bitch*/
				else if(prevState==9){
					jump=true;
					action =false;
				}/*burst this bitch*/
				else {
					painCount=0;
					invulnerable=true;
					powerout=0;
				}
			}
			else if(painCount<=0) {
				stuck=false;
				invulnerable=true;
				prevState=0;
				state=prevState;
			}
			//else state = -2;
		}
		else if (hurt)
		{
			if(state == 0 || state==1 || state==2){
				painCount=wait;
				state=-1;
				humanY=stageBase-humanHeight;
			} //play entered spicks
			else if(state>2){
				hurt=false;
				powerout=0; invulnerable=true;
			}
			else if(painCount<=0) {hurt=false; invulnerable=true; state=0;}
			else ;// hurt=false;//state = -1;
		}

		painCount--;

		if(painCount<-100) {
			invulnerable=false;
		}
	}

	
	public void gainSpeed(boolean gain, int level){
		speed = gain ? +level : -level;
	}
	
	public void stateEngine(Game.GameView mc){
		
		switch (state)
		{
			case(-3):touchAndDie=0;	humanX-=5; humanY-=6; 
						human.changeGif(R.drawable.h_2, false,mc.contextt);
						jump=false;
						break;					//hole
			case(-2):touchAndDie=0;	humanX-=5; 
						human.changeGif(R.drawable.h_1, false,mc.contextt);
						jump=false;
						break;				//spiked
			case(-1):touchAndDie=0;	humanX-=10;
						human.changeGif(R.drawable.h_1, false,mc.contextt);
						break;             //hurt
			case(0):						//normal run
			case(1):								//normal jump 
			case(2):touchAndDie=0;
					gainSpeed(false, 0);					//normal freefall
					move(2*humanHeight, 6, 5);
					if (invulnerable)
					{
						if(state==0) human.changeGif(R.drawable.hh0, false,mc.contextt); 
						else if (state==1)human.changeGif(R.drawable.hh1, false,mc.contextt) ;
						else human.changeGif(R.drawable.hh2, false,mc.contextt);
						hurt=false; stuck=false; inHole=false;
					}
					else 
					{
						if (state==0) human.changeGif(R.drawable.h0, false,mc.contextt);
						else if (state==1) human.changeGif(R.drawable.h1, false,mc.contextt);
						else human.changeGif(R.drawable.h2, false,mc.contextt);
					}
					break;
					
			case(3):						//fart run
			case(4):								//furt jump
			case(5):touchAndDie=0;
					gainSpeed(true, 1);					//fat freefall
					move(2*humanHeight*3/2, 6, 4);
					if (state==3)human.changeGif(R.drawable.h3, false,mc.contextt) ;
					else if(state==4) human.changeGif(R.drawable.h4, false,mc.contextt) ;
					else human.changeGif(R.drawable.h4, false,mc.contextt) ;
					break;
					
			case(7):
			case(8):
			case(6):if(jump){jump=false; action=true;}  gainSpeed(true, 1);					//vodka high 
					stuck = false; inHole=false; 
					touchAndDie=state==7? 1 : 0; 
					move(humanHeight/2, 4, 4);// humanY  = stageBase -humanHeight;
					if (state==6) human.changeGif(R.drawable.h6, false,mc.contextt); 
					else if(state==7) human.changeGif(R.drawable.h7, false,mc.contextt) ;
					else human.changeGif(R.drawable.h6, false,mc.contextt) ;
					break;

			case(9) :
			case(10):
			case(11):if(jump)if(!action){action=true; jump=false;}  //gainSpeed(false, 0);					//manga :jump on people
					gainSpeed(false, 0);			
					move(humanHeight,4, 4);
					touchAndDie= state!=9? 2:0;
					if( state==9) human.changeGif(R.drawable.h9, false,mc.contextt);
					else if(state==10&&!jump) human.changeGif(R.drawable.h10, false,mc.contextt);
					else human.changeGif(R.drawable.h11, false,mc.contextt);
					break;
			case(14):
			case(13):hurt=false;inHole=false;													//bike
			case(12):if(jump)if(!action){action=true; jump=false;}else gainSpeed(true, 1); 
					else if(jump){action=true; jump = false;} 
			touchAndDie= state==12? 0:3;
					if(state==12)gainSpeed(true, 1);else gainSpeed(false, 0);					
					move(humanHeight,4,4); 
					if (state==12) human.changeGif(R.drawable.h12, false,mc.contextt);
					else if (state==13) human.changeGif(R.drawable.h13, false,mc.contextt);
					else human.changeGif(R.drawable.h14, false,mc.contextt);
					break;
			case(15):
			case(16):
			case(17):
				gainSpeed(true,1);
				human.changeGif(R.drawable.train, false,mc.contextt);// humanY=stageBase;
				jump=false; stuck=false; inHole=false; hurt=false; 
				if(action){prevState=0; state=0; invulnerable=true; painCount=0; action=false; jump=true; }
				break;
			case(99): prevState=99; humanY=10000;break; //move(0,0,0	); 
		}
	}

	public void PlatformToHuman(int PxPos, int PsectionEdge, int PsectionBase, int PsectionWeigth) {
		
		if((PxPos<=humanX+humanWidth/4 && humanX+humanWidth/2>=PsectionEdge)&&(humanX<PsectionEdge &&humanX+humanWidth>PsectionEdge) ) {
			//1st edge triger
			if(state==13){
				jump=true;
			}
			if(prevState==9&&!airborn){
				jump=true; action =true;
			}/*burst this bitch*/
			
			if(prevState==15){
				stageBase = PsectionBase;
			}
			else {
				stageBase = PsectionBase+screenWidth;
			}
		}
	
		if(humanX<PxPos &&humanX+humanWidth*3/4>PxPos) {            //second edge triga
			if (humanY + humanHeight * 3 / 4 > PsectionBase){
				inHole = true;
			}
			else if (humanY + humanHeight < PsectionBase && inHole) {
				outOfHole = true;
			}
		}

		if(PxPos<=humanX+humanWidth/2 && humanX+humanWidth/2<=PsectionEdge){
			stageBase = PsectionBase;
		}
	}
	
		
	int getPrevstate(){return prevState;}
		
	void jump(){
		if(!airborn&&((!stuck&&!hurt)||invulnerable))   //to prevent form jumping while already jumping
		{
			prevState=state;
			jump = true;
//				ply sound......if (shawama)eho d mess
		}
	}
}



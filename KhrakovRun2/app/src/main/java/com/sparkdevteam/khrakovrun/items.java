package com.sparkdevteam.khrakovrun;

import java.util.Random;
//TOO MUCH HERE IS PUBLIC
public class items {

	public MyGif items;
	private int itemsHeight, itemsWidth,  itemsY, state;
	private boolean eaten, fixed;

	public int stageBase, itemsX;
	private int screenWidth;

	private int itemindex;
	private boolean isTriggered;

	Random rand2 = new Random(); 
	
	public items(Game.GameView mc, int State, int ItemsX){
		items=new MyGif(R.drawable.i1, false, mc.contextt);
		
		init(mc,  State,  ItemsX);
	}
	
	public void init(Game.GameView mc, int State, int ItemsX){
		//items=new MyGif(R.drawable.i1, false, mc.contextt);
		if(State==1)items.changeGif(R.drawable.i1, false, mc.contextt);
		else if(State==0)
			{
				itemindex =mc.level==1? R.drawable.l1h:mc.level==2? R.drawable.l2h: mc.level==3? R.drawable.l3h: R.drawable.l4h;
				items.changeGif(itemindex, false, mc.contextt);
			}
		else 
			{
				itemindex =mc.level==1? R.drawable.i_1:mc.level==2? R.drawable.i_2: mc.level==3? R.drawable.i_3 : R.drawable.i_4;
				items.changeGif(itemindex, false, mc.contextt);
			}
			
		itemsX = ItemsX;
		
		state=State;
		fixed=true;
		itemsHeight =0;
		
		itemsY=0;
		
		screenWidth = mc.screenWidth;
	}
	public void update( Game.GameView mc){
		itemsHeight=items.movie.height();
		itemsWidth=items.movie.width();
		
		stateEngine(mc);
		
		itemsX-=5;
	    itemsY  =(state==-1)? stageBase -itemsHeight : 
	    		 (state==0&&itemsY+itemsHeight<=mc.screenHeight)? mc.screenHeight-itemsHeight: 
	    	     (state>0&&state<5)? stageBase -itemsHeight*4:
	    	      itemsY;
	}
	public int getItemsY(){return itemsY;}
	public int getItemsX(){return itemsX;}
	
	public void stateEngine(Game.GameView mc){
		switch (state)
		{
			//case(-1): {items = mc.i_1 ;break;}
			//case(0): {items = mc.i_h ;break;}
			case(1): items.changeGif(R.drawable.i1, false,mc.contextt); break;
			case(2): items.changeGif(R.drawable.i2, false,mc.contextt); break;
			case(3): items.changeGif(R.drawable.i3, false,mc.contextt); break;
			case(4): items.changeGif(R.drawable.i4, false,mc.contextt); break;
			case(5): items.changeGif(R.drawable.i0, false,mc.contextt); break;
		}

		if (eaten && itemindex ==  R.drawable.i_3)
		{
			itemindex = R.drawable.i_33;
			items.changeGif(R.drawable.i_33, true, mc.contextt);
		}

		if (!fixed && itemindex ==  R.drawable.i_33)
		{
			itemindex =  R.drawable.i_3;
			items.changeGif(R.drawable.i_3, true, mc.contextt);
		}

		//if (itemindex == R.drawable.i_33 && items.movie.duration() == it)
	}

	public void PlatformToitems(int PxPos, int PsectionEdge, int PsectionBase, int PsectionWeigth, int MetroNeedAndHieght) {
	if((itemsX< -itemsWidth||eaten))
	{
		state=state<=0? state :rand2.nextInt(4)+1; 
		fixed =!(state==0||state==-1)?false :(state==-1&&itemsX<=-itemsWidth)? false: true;
	}
	
	if(state!=0&state!=5&&(itemsX<PsectionEdge-4*itemsWidth && itemsX>PxPos+4*itemsWidth) ) fixed=true;
	if(!fixed){
		itemsX=screenWidth;
	}
	
	if(MetroNeedAndHieght!=0 && (state>0 && state!=5)){state=5; itemsX = PxPos-itemsWidth/2; itemsY = MetroNeedAndHieght+itemsHeight/2; }
	
	if(state==0&&PsectionEdge<screenWidth&&PsectionEdge>-itemsWidth){itemsX=PsectionEdge; itemsY=PsectionBase>MetroNeedAndHieght? PsectionBase:MetroNeedAndHieght;}
	
	if((itemsX>=PxPos) && (itemsX+itemsWidth<PsectionEdge))stageBase = PsectionBase;	

}
	public int HumanToitems(int HumanX, int HumanWidth,int HumanY, int HumanHeight) {
	eaten=false;
			
	if((itemsX+itemsWidth*3/4>= HumanX) && (itemsX+itemsWidth/4<= HumanX+HumanWidth)
	&& (itemsY+itemsHeight>= HumanY) && (itemsY<= HumanY+HumanHeight))
		
	eaten=true;// state=rand itemsX=1500;}
	
	if (eaten) return state*3;
	else {
		return 0;
	}
}

}

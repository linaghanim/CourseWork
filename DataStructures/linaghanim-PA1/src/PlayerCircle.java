

public class PlayerCircle {
	private Player firstplayer;
	private int size;
	public PlayerCircle()
	{
		this.firstplayer=null;
		this.size=0;
	}

	// add the player p to the circle, such that the players are still sitting in a circle. Add them in alphabetical order. 
	
	public void addToCircle(Player p) 
	{
		if(firstplayer==null)//if it is the first time to add
		{
			firstplayer=new Player(p.getName());
			size++;
		}
		else if(this.firstplayer.getNextPlayer()==null)//if it is the second time to add
		{
			firstplayer.setNextPlayer(p);//connect second player with first player from both sides.
			firstplayer.setPrevPlayer(p);
			p.setNextPlayer(firstplayer);
			p.setPrevPlayer(firstplayer);
			if(p.getName().compareTo(this.firstplayer.getName())<0)
			{
				this.firstplayer=this.firstplayer.getNextPlayer();
			}
			size++;
		}
		else
		{
			String n=p.getName();
			Player curr=this.firstplayer;

			while(curr.getNextPlayer()!=firstplayer && n.compareTo(curr.getName())>0)
			{
				curr=curr.getNextPlayer();
			}
			p.setNextPlayer(curr.getNextPlayer());
			curr.getNextPlayer().setPrevPlayer(p);
			curr.setNextPlayer(p);
			p.setPrevPlayer(curr);
			size++;
		}
	}

	//a method that removes player p from the circle.
	
	public void remove(Player p)
	{
		Player curr=this.firstplayer;
		while(curr!=p)
		{
			curr=curr.getNextPlayer();
		}
		p.getPrevPlayer().setNextPlayer(p.getNextPlayer());
		p.getNextPlayer().setPrevPlayer(p.getPrevPlayer());
		size--;
	}

	// returns the first person in the circle.
	
	public Player getFirstPlayer() 
	{
		return this.firstplayer;
	}

	// returns number of players in the circle.
	
	public int getSize() 
	{
		return this.size;
	}

	// print all the players in the circle
	
	public String toString() 
	{
		String s="";
		Player curr=this.firstplayer;
		s+=curr.toString();
		curr=curr.getNextPlayer();
		while(curr!=firstplayer)
		{
			s+="\n"+curr.toString();
			curr=curr.getNextPlayer();
		}
		return s;
	}

	//reverse the circle
	
	public void reverse()
	{
		Player tmp=this.firstplayer;
		Player p=this.firstplayer.getPrevPlayer();

		while(p!=this.firstplayer)
		{
			tmp=p.getNextPlayer();
			p.setNextPlayer(p.getPrevPlayer());
			p.setPrevPlayer(tmp);
			p=p.getNextPlayer();
		}
		tmp=p.getNextPlayer();
		p.setNextPlayer(p.getPrevPlayer());
		p.setPrevPlayer(tmp);
	}

}
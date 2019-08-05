public class Player {
	private String name;
	private Player nextPlayer=null;
	private Player prevPlayer=null;
	private SinglyLinkedList<UnoCard> hand; 

	public Player(String name){
		this.name = name;
		hand=new SinglyLinkedList<UnoCard>();
	}

	//adds the card to the player's hand

	public void addToHand(UnoCard c){
		hand.regularInsert(c);
	}

	//removes the card at place index
	
	public void removeFromHand(int index){
		SinglyLinkedNode <UnoCard> curr=hand.getHead();
		for(int i=0;i<=index;i++)
		{
			curr=curr.getNext();
		}
		hand.remove(curr.getData());//O(n)
	}

	// return true when your hand has nothing left. 
	
	public boolean winner(){
		if(this.hand.size()==0)
		{
			return true;
		}
		return false;
	}

	
	public SinglyLinkedList<UnoCard> getHand()
	{
		return this.hand;
	}


	public String getName()
	{
		return this.name;
	}

	
	public Player getNextPlayer() {
		return nextPlayer;
	}

	
	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	
	public Player getPrevPlayer() {
		return prevPlayer;
	}

	
	public void setPrevPlayer(Player prevPlayer) {
		this.prevPlayer = prevPlayer;
	}

	
	public String toString() {
		return "Player [name=" + name +", hand="+this.hand+"]";
	}


}

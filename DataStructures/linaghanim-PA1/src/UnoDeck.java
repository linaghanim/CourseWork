
import java.util.*;
public class UnoDeck {
	private static final String[] REGULAR_COLORS = {"red", "yellow", "blue", "green"};
	private SinglyLinkedList<UnoCard> deck; 
	private SinglyLinkedList<UnoCard> discard; 
	private UnoCard lastDiscarded;

	
	// There are 108 cards in a Uno deck. 
	// There are four suits, Red, Green, Yellow and Blue, 
	// each consisting of one 0 card, two 1 cards, two 2s, 3s, 4s, 5s, 6s, 7s, 8s and 9s; 
	// two Draw Two cards; two Skip cards; and two Reverse cards. 
	// In addition there are four Wild cards and four Wild Draw Four cards.

	public UnoDeck(){
		// Initialized as having all 108 cards, as described above
		deck=new SinglyLinkedList<UnoCard>();
		discard=new SinglyLinkedList<UnoCard>();
		for (String color : REGULAR_COLORS){
			deck.randomInsert(new UnoCard(color, 0)); // add one of your color in zero
			for (int i = 0; i<2; i++){
				// add numbers 1-9
				for (int cardNumber = 1; cardNumber<=9; cardNumber++){
					deck.randomInsert(new UnoCard(color, cardNumber)); // Add this to deck
				}
				// add 2 of each of the special card for that color
				deck.randomInsert(new UnoCard(color, true, false, false)); // add to deck
				deck.randomInsert(new UnoCard(color, false, true, false)); // add to deck
				deck.randomInsert(new UnoCard(color, false, false, true)); // add to deck
			}

		}
		// add 4 wild cards, and 4 draw 4 wild cards
		for (int i = 0; i<4; i++){
			deck.randomInsert(new UnoCard(false)); // add to deck
			deck.randomInsert(new UnoCard(true)); // add to deck
		}
	}

	// gets the last card which was discarded - use this to compare to the card you’re about to put down.
	
	public UnoCard getLastDiscarded() {
		return this.lastDiscarded;
	}

	// draw a card from the deck. If there are no more cards in the deck, it moves all of the cards from the discard pile to the deck (the discard pile will already be shuffled, as described below).
	
	public UnoCard drawCard() {	
		if(deck.getHead()==null)
		{
			this.deck=discard;
			discard=new SinglyLinkedList<UnoCard>();
			discardCard(this.lastDiscarded);
		}
		UnoCard card=this.deck.getHead().getData();
		this.deck.remove(card);
		return card;	
	}

	// adds c to the discard pile, and sets it as the last discarded card. Will throw an error if an invalid card is placed on the deck. When calling this
	
	public void discardCard(UnoCard c) //throws Exception 
	{

		if(this.lastDiscarded==null||c.canBePlacedOn(this.lastDiscarded)||this.lastDiscarded.isReverse()||this.lastDiscarded.isSkip()||this.lastDiscarded.isDrawTwo()||this.lastDiscarded.isWildDrawFour())//if it is the first time we discard or it can be discarded
		{
			discard.regularInsert(c);
			this.lastDiscarded=c;
		}
		else
		{
			//throw new Exception("not valid card");
			System.out.println("invalid card discarded");
		}
	}

	
	public String toString()
	{
		String s=this.deck.toString();
		return s;
	}
}


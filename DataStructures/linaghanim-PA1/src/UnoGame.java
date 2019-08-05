
import java.util.*;
public class UnoGame {

	//Main method
	
	public static void main(String[] args) {
	

		//player names
		Scanner console=new Scanner (System.in);
		System.out.println("how many players?");
		int n=console.nextInt();
		PlayerCircle circle=new PlayerCircle();//the current players playing 
		Queue<Player> otherplayers;//the queue that has the rest of the players 
		if(n>=5)//if there are 5 or more than 5 players
		{
			otherplayers=new Queue<Player>(n-5);
			playerNames(console,circle,otherplayers,5,n-5);
		}
		else//if there are less than 5 players
		{
			otherplayers=new Queue<Player>(0);
			playerNames(console,circle,otherplayers,n,0);
		}	

		//for each game:
		String startagain="yes";
		while(startagain.equals("yes"))//while players want to play, 
		{
			//draw cards
			Player curr=circle.getFirstPlayer();
			UnoDeck deck=new UnoDeck();
			drawCards(circle,curr,deck);

			//print hands before game
			System.out.println("How hands look at the beginning:");
			System.out.println(circle);

			//play game:
			game(console, circle, curr, deck, otherplayers);

			//start again?
			System.out.println("\ndo you want to start agai? yes/no");
			startagain=console.next();
		}
	}

	//a method that asks the users to enter their names and puts them in circle and queue. 

	public static void playerNames(Scanner console,PlayerCircle circle,Queue<Player> otherplayers,int n,int m)
	{

		for(int i=0;i<n;i++)
		{
			System.out.println("please enter name:");
			String name=console.next();
			circle.addToCircle(new Player(name)); 
		}

		for(int i=0;i<m;i++)
		{
			System.out.println("please enter name:");
			String name=console.next();
			otherplayers.enqueue(new Player(name)); 
		}
	}

	//a method that draws 7 cards for each players. 
	
	public static void drawCards(PlayerCircle circle,Player curr, UnoDeck deck)
	{

		for(int i=0;i<7;i++)//adds cards to first player
		{
			UnoCard c=deck.drawCard();
			curr.addToHand(c);
		}
		System.out.println();
		curr=curr.getNextPlayer();
		while(curr!=circle.getFirstPlayer())//adds cards to the rest of players
		{

			for(int i=0;i<7;i++)
			{
				UnoCard c=deck.drawCard();
				curr.addToHand(c);

			}
			System.out.println();
			curr=curr.getNextPlayer();
		}
	}

	//The game until finding a winner
	
	public static void game(Scanner console, PlayerCircle circle,Player curr, UnoDeck deck,Queue<Player> otherplayers)
	{
		//draw 1 card from deck to discard
		UnoCard drawn=deck.drawCard();
		deck.discardCard(drawn);
		System.out.println();

		boolean play=true;
		int times=0;//how many times played
		UnoCard lastdiscarded;
		while(play==true)//O(n)
		{
			lastdiscarded=deck.getLastDiscarded();//last discarded
			System.out.println("\nlast discarded: "+lastdiscarded+"\n");

			SinglyLinkedList<UnoCard> hand=curr.getHand();//current player's hand
			System.out.print("before play: "+curr.getName()+":");
			System.out.println(hand);//prints current player's hand

			times++;

			playCard(console, lastdiscarded, hand,deck,circle, curr);//player plays a card, O(n)

			System.out.print("after play: "+curr.getName()+":");
			System.out.println(hand);//prints player's hand after playing

			if(curr.winner())//if this player has no more cards, he is the winner
			{
				play=false;
			}
			else
			{
				curr=curr.getNextPlayer();//go to next player
			}
		}

		//print hands
		System.out.println("\nHow hands look at the end:");
		System.out.println(circle);

		//report winner
		System.out.println("\nthe winner is:");
		System.out.println(curr.toString());
		System.out.println("\nwent around circle: "+times/5+" times");

		//report loser
		Player loser=circle.getFirstPlayer();
		curr=loser.getNextPlayer();

		while(curr!=circle.getFirstPlayer())
		{
			if(curr.getHand().size()>loser.getHand().size())
			{
				loser=curr;
			}
			curr=curr.getNextPlayer();
		}
		System.out.println("\nthe loser is: (the first loser found)");
		System.out.println(loser.toString());

		//switch player
		if(!otherplayers.isEmpty())
		{
			circle.remove(loser);
			Player next=otherplayers.dequeue();
			circle.addToCircle(next);
			otherplayers.enqueue(loser);
		}

	}

	//a method that asks the user what card he/she wants to play from the cards that can be played and plays it
	//Running time: O(n)
	public static void playCard(Scanner console,UnoCard lastdiscarded,SinglyLinkedList<UnoCard> hand,UnoDeck deck,PlayerCircle circle,Player curr)
	{
		if(lastdiscarded.isDrawTwo())//if the lastdiscarded is drawtwo card
		{
			UnoCard drawn;
			for(int i=0;i<2;i++)
			{

				drawn=deck.drawCard();//draw card from deck
				curr.addToHand(drawn);//draw a card
				deck.discardCard(drawn);
			}
			System.out.println("((Discard a card from deck to change value of last discarded))");
			drawn=deck.drawCard();
			deck.discardCard(drawn);
		}
		else if(lastdiscarded.isWildDrawFour())//if the lastdiscarded is wilddrawfour card
		{
			UnoCard drawn;
			for(int i=0;i<4;i++)
			{
				drawn=deck.drawCard();//draw card from deck
				curr.addToHand(drawn);//draw a card

			}
			System.out.println("((Discard a card from deck to change value of last discarded))");
			drawn=deck.drawCard();
			deck.discardCard(drawn);
		}
		else if(lastdiscarded.isReverse())//if the lastdiscarded is reverse card
		{
			circle.reverse();//O(n)
			UnoCard drawn=deck.drawCard();//draw card from deck
			deck.discardCard(drawn);
		}
		else if(lastdiscarded.isSkip())//if the lastdiscarded is skip card
		{
			curr=curr.getNextPlayer();
			UnoCard drawn=deck.drawCard();//draw card from deck
			deck.discardCard(drawn);
		}
		else //if it is a normal card , O(n)
		{
			UnoCard u=null;
			SinglyLinkedNode<UnoCard> h=hand.getHead();

			int n=0;
			SinglyLinkedList<UnoCard> cardstoplay=new SinglyLinkedList<UnoCard>();//list of cards that can be played
			System.out.println("cards you can play: ");
			for(int i=0;i<hand.size();i++)//check for every card he has if it works
			{

				if(h.getData().canBePlacedOn(lastdiscarded))//if it can be played
				{	
					n++;
					System.out.println(n+"- "+h);//print it
					cardstoplay.regularInsert(h.getData());//add it to list

				}
				h=h.getNext();
			}

			if(n==0)//if none of his cards work
			{
				System.out.println("No cards available to play");
				curr.addToHand(deck.drawCard());//draw a card

			}
			else
			{
				System.out.println("pick what card you want to play: (write number)");
				int chosennumber=console.nextInt();//player chooses number of card he wants to play
				while(chosennumber>n||chosennumber<=0)//if didn't chose a valid number
				{
					System.out.println("enter again:");
					chosennumber=console.nextInt();
				}
				SinglyLinkedNode<UnoCard> chosencard=cardstoplay.getHead();
				while(chosennumber>1)
				{
					chosencard=chosencard.getNext();
					chosennumber--;
				}
				u=chosencard.getData();
				//plays the card:
				deck.discardCard(u);
				hand.remove(u);
			}
		}
	}
}

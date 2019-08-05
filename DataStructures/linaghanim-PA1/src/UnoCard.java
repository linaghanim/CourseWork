
public class UnoCard{

	// blue, green, red, yellow, wild
	private String color;
	private int number;
	private boolean isSpecialCard = false;
	private boolean isDrawTwo = false;
	private boolean isReverse = false;
	private boolean isSkip = false;
	private boolean isWildDrawFour = false;


	// constructor for normal cards
	public UnoCard(String color, int number){
		this.color = color;
		this.number = number;
	}

	// constructor for colored cards which have special values (so no numbers)
	
	public UnoCard(String color, boolean drawTwo, boolean reverse, boolean skip){

		if (drawTwo && (reverse || skip) || (reverse && skip)){
			
			throw new IllegalStateException("A card can't have more than one special property");
		}
		this.number = -1;
		this.color = color;
		this.isSpecialCard = true;
		this.isDrawTwo = drawTwo;
		this.isReverse = reverse;
		this.isSkip = skip;
	}

	// constructor for wild cards
	public UnoCard(boolean drawFour){
		this.number = -1;
		this.color = "wild";
		this.isSpecialCard = true;
		this.isWildDrawFour = drawFour;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return the isSpecialCard
	 */
	public boolean isSpecial() {
		return isSpecialCard;
	}

	/**
	 * @return the isDrawTwo
	 */
	public boolean isDrawTwo() {
		return isDrawTwo;
	}

	/**
	 * @return the isReverse
	 */
	public boolean isReverse() {
		return isReverse;
	}

	/**
	 * @return the isSkip
	 */
	public boolean isSkip() {
		return isSkip;
	}

	public boolean isWild(){
		return this.getColor().equals("wild");
	}
	public boolean isWildDrawFour() {
		return isWildDrawFour;
	}

	public boolean canBePlacedOn(UnoCard other){
		return (this.getNumber()!=-1 && this.getNumber()==other.getNumber()) ||
				(this.getColor().equals(other.getColor())) ||
				(this.isDrawTwo() && this.isDrawTwo()==other.isDrawTwo()) ||
				(this.isReverse() && this.isReverse()==other.isReverse()) ||
				(this.isSkip()  && this.isSkip()==other.isSkip()) ||
				this.isWild() || other.isWild();
	}

	// toString - prints the type of the card
	public String toString(){
		if (this.isSpecial()){
			if (this.isWild()){
				if (this.isWildDrawFour()){
					return "wild draw four card";
				}
				else{
					return "wild card";
				}
			}
			else if (this.isDrawTwo()){
				return this.getColor()+" draw two";
			}
			else if (this.isReverse()){
				return this.getColor()+" reverse";
			}
			else if (this.isSkip){
				return this.getColor()+" skip";
			}
			else{
				return "this shouldn't happen";
			}
		}
		else{
			return this.getColor()+" "+this.getNumber();
		}
	}

	// tests that the memory addresses are the same
	public boolean equals(UnoCard other){
		return this==other;
	}

}



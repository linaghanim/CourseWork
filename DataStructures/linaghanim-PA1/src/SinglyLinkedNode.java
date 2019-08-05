
public class SinglyLinkedNode<T> {
	private T data;
	private SinglyLinkedNode<T> next;
	public SinglyLinkedNode(T data) //constructor that initializes the node.
	{
		this.data=data;
		this.next=null;
	}
	public T getData() //returns the data in the node 
	{
		return this.data;
	}
	public void setNext(SinglyLinkedNode<T> nextNode) //sets the node as the “next” node in the list, returned by getNext() 
	{
		this.next=nextNode;
	}
	public SinglyLinkedNode<T> getNext() // returns the next node in the list
	{
		return this.next;
	}
	public String toString() // return the string of the data inside it.
	{
		return this.data+"";
	}
}

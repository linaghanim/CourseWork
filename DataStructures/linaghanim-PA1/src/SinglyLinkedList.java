
import java.util.*;
public class SinglyLinkedList<T> {
	private SinglyLinkedNode<T> head;
	private SinglyLinkedNode<T> tail;
	private int size;

	public SinglyLinkedList() //constructor 
	{
		this.head=null;
		this.tail=null;
		this.size=0;
	}

	//returns the first node in the list (null if empty)

	public SinglyLinkedNode<T> getHead() 
	{
		return this.head;
	}

	// insert “data” at the end.

	public void regularInsert(T data) 
	{
		SinglyLinkedNode<T> newnode=new SinglyLinkedNode<T>(data);
		if(this.head==null)
		{
			this.head=newnode;
			this.tail=newnode;
		}
		else
		{
			tail.setNext(newnode);
			tail=newnode;
		}
		size++;
	}

	// insert “data” at a random point in the LinkedList. 
	public void randomInsert(T data) 
	{	
		SinglyLinkedNode<T> newnode=new SinglyLinkedNode<T>(data);
		if(this.head==null)//first time inserting 
		{
			this.head=newnode;
			this.tail=newnode;

		}
		else
		{
			Random rand=new Random();
			int r=rand.nextInt(this.size);
			SinglyLinkedNode<T> currnode=this.head;
			if(r==0)//insert at the beginning 
			{
				newnode.setNext(head);
				this.head=newnode;

			}
			else if(r==this.size-1)//insert at the end
			{
				regularInsert(newnode.getData());
			}
			else
			{
				while((currnode.getNext()!=null)&&r>0)
				{
					currnode=currnode.getNext();
					r--;
				}

				newnode.setNext(currnode.getNext());
				currnode.setNext(newnode);
			}
		}
		size++;
	}

	//delete the “data” node from the list.
	
	public void remove(T data) 
	{
		if(this.size==0) return;
		else if(this.head.getData()==data)//remove the first
		{
			this.head=this.head.getNext();
			size--;
		}
		else if(this.tail.getData()==data)//remove the end
		{
			SinglyLinkedNode<T> currnode=this.head;
			while(currnode.getNext()!=this.tail)
			{
				currnode=currnode.getNext();
			}
			currnode.setNext(null);
			this.tail=currnode;
			size--;
		}
		else//remove from middle
		{
			SinglyLinkedNode<T> currnode=this.head;
			while(currnode.getNext().getData()!=data)
			{
				currnode=currnode.getNext();
			}
			currnode.setNext(currnode.getNext().getNext());
			size--;
		}
	}

	//gets size of linked list.

	public int size() 
	{
		return this.size;
	}

	//return a string of the list
	
	public String toString()
	{
		String s="";

		SinglyLinkedNode<T> currnode=this.head;
		if(currnode==null) return s;
		while(currnode.getNext()!=null)
		{
			s+=currnode.toString()+"-->";
			currnode=currnode.getNext();
		}
		s+=currnode.getData();
		return s;
	}
}

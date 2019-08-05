
public class Queue<T> {
	private T[] q;
	private int front;//start from 1
	private int rear;
	private int size;

	//Constructor that creates the internal array of size “size”, as well as any other variables needed in the queue.
	public Queue(int size) 
	{
		q=(T[])(new Object[size]);
		this.front=1;
		this.rear=1;
		this.size=0;
	}

	//enqueue data
	
	public void enqueue(T data) 
	{
		if(this.size!=q.length)
		{

			this.q[this.rear-1]=data; 
			size++;
			this.rear=this.rear%q.length+1;
		}
	}

	//dequeue first item in the queue
	
	public T dequeue() 
	{
		if(this.isEmpty()) 
		{
			//error

		}

		T data= this.q[this.front-1]; 
		this.front=this.front%q.length +1;
		size--;
		return data;

	}

	//return size of the queue
	
	public int getSize() 
	{
		return size;
	}

	//returns true if queue is empty
	
	public boolean isEmpty() 
	{
		if ((this.front== this.rear) && (! isFull()))
			return true;
		else
			return false;
	}

	//return true if queue is full.
	
	public boolean isFull() 
	{
		if(this.size==q.length&&q.length!=0)
			return true;
		else
			return false;
	}
}
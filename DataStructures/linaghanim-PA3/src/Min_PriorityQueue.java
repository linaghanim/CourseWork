/**
 * Lina Ghanim
 * linaghanim@brandeis.edu
 *
 */
public class Min_PriorityQueue {
	public int size;
	private GraphNode [] queue;
	public HashMap hmap;
	
	public Min_PriorityQueue(int length)
	{
		this.size=0;
		this.queue=new GraphNode[length];
		this.hmap=new HashMap(length);
	}
/**
 * this should insert g into the queue with its priority.
 * @param g= the node
 */
	public void insert(GraphNode g)
	{
		this.size++;
		queue[this.size-1]=g;
		this.hmap.insert(g, this.size-1);
		heapifyUp(this.size-1);		
	}
	/**
	 * this should return and remove from the priority queue the GraphNode with the highest priority in the queue.
	 * @return highest priority 
	 */
	public GraphNode pullHighestPriorityElement()
	{
		if(this.size<1)
		{
			return null;
		}
		GraphNode min=this.queue[0];
		this.queue[0]=this.queue[this.size-1];
		this.hmap.delete(min);
		this.queue[this.size-1]=null;
		this.size--;
		
		heapifyDown(0);
		return min;
	}
	/**
	 * this calls the heapify method
	 * @param g= new node to modify and heapify up
	 */
	public void rebalance(GraphNode g)
	{
		int index=hmap.getValue(g);
		this.queue[index]=g;
		heapify(g, index);
	}
	/**
	 * once you change the priority of the GraphNode, you should be able to restore the heap-like property of the priority queue at g
	 * @param g= the node to heapify up
	 */
	public void heapify(GraphNode g, int index)
	{
		heapifyUp(index);
	}
	/**
	 * 
	 * @param i
	 * @return the index of the parent
	 */
	public int parent(int i)
	{
		int p=(int)Math.abs(Math.ceil((double)i/2-1));
		return p;
	}
	public int left(int i)
	{
		return 2*i+1;
	}
	public int right(int i)
	{
		return 2*i+2;
	}
	
	/**
	 * 
	 * @param i= the index to heapifydown
	 */
	public void heapifyDown(int i)
	{if(this.size==0||this.size==1)
	{
		return;
	}
		int l=left(i);
		int r=right(i);
		int smallest;
		if(l<=this.size-1 && this.queue[l].priority<this.queue[i].priority)
		{
			smallest=l;
		}
		else
		{
			smallest=i;
		}
		if(r<=this.size-1 && this.queue[r].priority<this.queue[smallest].priority)
		{
			smallest=r;
		}
		if(smallest!=i)
		{
			GraphNode g=this.queue[i];
			
			this.queue[i]=this.queue[smallest];
			this.hmap.set(this.queue[i], i);
			
			this.queue[smallest]=g;
			this.hmap.set(g, smallest);
			
			heapifyDown(smallest);
		}
		
	}
	/**
	 * 
	 * @param i= the index to heapifyup
	 */
	public void heapifyUp(int i)
	{
		while(i>0&&this.queue[i].priority<this.queue[parent(i)].priority)
		{
			GraphNode g=this.queue[i];
			this.queue[i]=this.queue[parent(i)];
			this.hmap.set(this.queue[i], i);
			
			this.queue[parent(i)]=g;
			this.hmap.set(g, parent(i));
			
			i=parent(i);
		}
		
	}
	
	/**
	 * 
	 * @param key= node
	 * @return true if the graph contains key else return false
	 */
	public boolean contains(GraphNode key)
	{
		if(this.hmap.hasKey(key)) return true;
		else return false;
	}
}

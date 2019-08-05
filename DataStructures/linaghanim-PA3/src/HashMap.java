/**
 * Lina Ghanim
 * linaghanim@brandeis.edu
 *
 */
public class HashMap {
private Entry [] map;
private int elements;
public HashMap(int length)
{
	this.map=new Entry[length];
	this.elements=0;
}

/**
 * check the hashmap to see if there is an Entry for the GraphNode “key”, if there is, change its value to “value”, otherwise,
 * @param key= node
 * @param value= the index of the key in the queue
 */
public void set(GraphNode key, int value)
{
	int index=this.probingforkey(key);
	if(index!=-1)//found it
	{
		
		this.map[index].value=value;
		
	}

	else//empty spot
	{
		this.insert(key,value);
	}
	
}

/**
 * @return the index of where the key exists
 * @param key
 * 
 */
public int probingforkey(GraphNode key)
{
	int i=0;
	int index=hashFunction(Math.abs(key.getId().hashCode()),i,this.map.length);//the index that it should be in
	
	while(index!=this.map.length&&this.map[index]!=null) //while the place is not empty, prob 
		{
		if(this.map[index].value!=-1)
		{
		if(this.map[index].key.getId().equals(key.getId()))
		{
			return index;
		}
		}
			i++;
			index=hashFunction(Math.abs(key.getId().hashCode()),i,this.map.length);
		}

	return -1;// doesn't exist
}


/**
 * 
 * @param k
 * @param i
 * @param length= length of hash array
 * @return
 */
public int hashFunction(int k, int i,int length)//linear probing
{
	int hash=k%length;
	int index=hash*i%length;
	return index;
}

/**
 * gets the value for the entry with g as the key
 * @param g
 * @return
 */
public int getValue(GraphNode g)
{
	int index=this.probingforkey(g);
	if(index!=-1)
	{
		return this.map[index].value;
	}
	else
		return -1;
}

/**
 * true if the hashmap has that key
 * @param g
 * @return
 */
public boolean hasKey(GraphNode g)
{
	int index=this.probingforkey(g);
	if(index!=-1)
	{
		return true;
	}
	else
		return false;
}

/**
 * 
 * @param key= node to insert
 * @param value= value of the node
 */
public void insert(GraphNode key, int value)
{
	Entry e=new Entry(key,value);
	int i=0;
	int index=hashFunction(Math.abs(key.getId().hashCode()),i,this.map.length);
	
	while(this.map[index]!=null&&this.map[index].value!=-1) {
			i++;
			index=hashFunction(Math.abs(key.getId().hashCode()),i,this.map.length);
		}
	this.map[index]=e;
	this.elements++;
	//rehashing
		double a=(double)this.elements/this.map.length;
		if(a>0.5)
		{
			this.rehashing();
		}
}
public void rehashing()
{
	HashMap hmap=new HashMap(this.map.length*2);
	for(int i=0;i<this.map.length;i++)
	{
		if(this.map[i]!=null)
		{
			hmap.insert(this.map[i].key, this.map[i].value);
		}
	}
	this.map=hmap.map;
}
public void delete(GraphNode key)
{
	int index=this.probingforkey(key);
	if(index!=-1)
	{
		if(this.map[index]!=null)
		{
		 this.map[index]=new Entry(null,-1);
		 this.elements--;
		}
	}
	
}
}

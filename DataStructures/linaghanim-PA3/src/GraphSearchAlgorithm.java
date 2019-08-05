/**
 * Lina Ghanim
 * linaghanim@brandeis.edu
 *
 */

public class GraphSearchAlgorithm {
	public static void main(String[] args) {
		GraphWrapper gw = new GraphWrapper(true);
		GraphNode g=Dijkstra(gw);
		String path="";
		if(g==null) System.out.println("no path");
		//print path
		while(g.previousNode!=null)
		{
			path=g.previousDirection+"\n" +path;
			g=g.previousNode;
		}
		System.out.println(path);
	}
	/**
	 * 
	 * @param g=the last node in the path
	 */
	public static void printPath(GraphNode g)
	{
		if(g.previousNode==null) return;
		g=g.previousNode;
		printPath(g);
		System.out.println(g.previousDirection);
	}
	
	/**
	 * 
	 * @param G= graph
	 * @return the last node in the shortest path
	 */
public static GraphNode Dijkstra(GraphWrapper G)
{
	HashMap set=new HashMap(50);// a set of all the nodes that have min priority
	Min_PriorityQueue q=new Min_PriorityQueue(50);
	G.getHome().priority=0;
	q.insert(G.getHome());
	while(q.size>0)
	{
		GraphNode curr=q.pullHighestPriorityElement();
		set.insert(curr, 0);
		if (curr.isGoalNode())
		{
			return curr;
		}
		else
		{
			
			if(curr.hasEast())
			{
				GraphNode neighbor=curr.getEast();
				if(!set.hasKey(neighbor))
				{
				int x=curr.priority+curr.getEastWeight();
				if(!q.contains(neighbor))
				{
					neighbor.priority=x;
					neighbor.previousNode=curr;
					neighbor.previousDirection="East";
					q.insert(neighbor);
				}
				else
				{
					if(x<neighbor.priority)
					{
						neighbor.priority=x;
						q.rebalance(neighbor);
						neighbor.previousNode=curr;
						neighbor.previousDirection="East";
					}
				}
				}
			}
			
			if(curr.hasNorth())
			{
				
				GraphNode neighbor=curr.getNorth();
				if(!set.hasKey(neighbor))
				{
				int x=curr.priority+curr.getNorthWeight();
				if(!q.contains(neighbor))
				{
					neighbor.priority=x;
					neighbor.previousNode=curr;
					neighbor.previousDirection="North";
					q.insert(neighbor);
				}
				else
				{
					if(x<neighbor.priority)
					{
						neighbor.priority=x;
						q.rebalance(neighbor);
						neighbor.previousNode=curr;
						neighbor.previousDirection="North";
					}
				}
				}
			}
			
			if(curr.hasSouth())
			{
				GraphNode neighbor=curr.getSouth();
				if(!set.hasKey(neighbor))
				{
				int x=curr.priority+curr.getSouthWeight();
				if(!q.contains(neighbor))
				{
					neighbor.priority=x;
					neighbor.previousNode=curr;
					neighbor.previousDirection="South";
					q.insert(neighbor);
				}
				else
				{
					if(x<neighbor.priority)
					{
						neighbor.priority=x;
						q.rebalance(neighbor);
						neighbor.previousNode=curr;
						neighbor.previousDirection="South";
					}
				}
				}
			}
			
			if(curr.hasWest())
			{
				GraphNode neighbor=curr.getWest();
				if(!set.hasKey(neighbor))
				{
				int x=curr.priority+curr.getWestWeight();
				if(!q.contains(neighbor))
				{
					neighbor.priority=x;
					neighbor.previousNode=curr;
					neighbor.previousDirection="West";
					q.insert(neighbor);
				}
				else
				{
					if(x<neighbor.priority)
					{
						neighbor.priority=x;
						q.rebalance(neighbor);
						neighbor.previousNode=curr;
						neighbor.previousDirection="West";
					}
				}
				}
			}
		}
	}
	//no path
	return null;
}
}

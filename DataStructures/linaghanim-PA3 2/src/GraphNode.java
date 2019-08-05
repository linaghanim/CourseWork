import java.util.*;
import java.io.*;
public class GraphNode implements Serializable{
	private boolean hasNorth;
	private GraphNode north;
	private int northWeight;
	private boolean hasSouth;
	private GraphNode south;
	private int southWeight;
	private boolean hasEast;
	private GraphNode east;
	private int eastWeight;
	private boolean hasWest;
	private GraphNode west;
	private int westWeight;
	private boolean isGoalNode;
	private String id;
		
	public int priority;
	public GraphNode previousNode;
	public String previousDirection;
	
	public GraphNode(String id, boolean goal){
		this.id = id;
		hasNorth = false;
		hasSouth = false;
		hasWest = false;
		hasEast = false;
		isGoalNode = goal;
	}
	
	// DONT USE THESE METHODS
	public void setNorthWeight(int northWeight) {
		this.northWeight = northWeight;
	}
	public void setSouthWeight(int southWeight) {
		this.southWeight = southWeight;
	}
	public void setEastWeight(int eastWeight) {
		this.eastWeight = eastWeight;
	}
	public void setWestWeight(int westWeight) {
		this.westWeight = westWeight;
	}

	
	public void setNorth(GraphNode north, int weight) {
		this.north = north;
		this.hasNorth = true;
		this.setNorthWeight(weight);
	}

	public void setSouth(GraphNode south, int weight) {
		this.south = south;
		this.hasSouth = true;
		this.setSouthWeight(weight);
	}

	public void setEast(GraphNode east, int weight) {
		this.east = east;
		this.hasEast = true;
		this.setEastWeight(weight);
	}

	public void setWest(GraphNode west, int weight) {
		this.west = west;
		this.hasWest = true;
		this.setWestWeight(weight);
	}

	// You can use these:
	
	public int getSouthWeight() {
		return southWeight;
	}
	
	public int getWestWeight() {
		return westWeight;
	}


	public int getEastWeight() {
		return eastWeight;
	}

	
	public boolean hasNorth() {
		return hasNorth;
	}
	public GraphNode getNorth() {
		return north;
	}
	public boolean hasSouth() {
		return hasSouth;
	}
	public GraphNode getSouth() {
		return south;
	}
	public boolean hasEast() {
		return hasEast;
	}
	public GraphNode getEast() {
		return east;
	}
	public boolean hasWest() {
		return hasWest;
	}
	public GraphNode getWest() {
		return west;
	}
	public boolean isGoalNode() {
		return isGoalNode;
	}
	public String getId() {
		return id;
	} 
	public int getNorthWeight() {
		return northWeight;
	}
	
	public String toString(){
		String s = "ID: "+this.id;
		if (this.hasNorth){
			s += " - north: "+this.north.getId() +" weight "+this.northWeight;
		}
		
		if (this.hasSouth){
			s += " - south: "+this.south.getId() +" weight "+this.southWeight;
		}
		
		if (this.hasWest){
			s += " - west: "+this.west.getId() +" weight "+this.westWeight;
		}
		
		if (this.hasEast){
			s += " - east: "+this.east.getId() +" weight "+this.eastWeight;
		}
		return s;
		
	}

	
}

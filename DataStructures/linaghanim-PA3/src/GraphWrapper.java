import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.util.*;

// Don't modify this file
public class GraphWrapper {
	private GraphNode home;
	private GraphNode[][] index;
	
	private int homeX = 3;
	private int homeY = 3;
	private int goalX = 76;
	private int goalY = 42;
	
	public GraphWrapper(){
		this(false);
	}
	
	public GraphWrapper(boolean test){
		Scanner console = new Scanner(System.in);
		Path path = Paths.get("node_ids.txt");
		Path edgePaths = Paths.get("node_edge_weights.txt");
		if (test){
			System.out.println("Enter the name of the IDs text file, then press enter.");
			path = Paths.get(console.nextLine());
			System.out.println("Enter the name of the edge paths text file, then press enter.");
			edgePaths = Paths.get(console.nextLine());
			System.out.println("Enter the homeX, homeY, goalX, and goalY, then press enter. ");
			homeX = console.nextInt();
			homeY = console.nextInt();
			goalX = console.nextInt();
			goalY = console.nextInt();
		}
		
		List<String> l;
		try {
			l = Files.readAllLines(path);
			String[] s = l.get(0).split(" ");
			index = new GraphNode[l.size()] [s.length];
			for (int i = 0; i<index.length; i++){
				s = l.get(i).split(" ");
				for (int j = 0; j< index[0].length; j++){
					boolean isGoal = (i==goalY && j==goalX);
					boolean isHome = (i==homeY && j==homeX);
					index[i][j] = new GraphNode(s[j], isGoal);
					if (isHome){
						this.home = index[i][j];
					}
					
				}
			}
			
			
			
			l = Files.readAllLines(edgePaths);
			
			for (int i = 0; i<index.length; i++){
				String currentLine = l.get(i);
				Scanner input = new Scanner(currentLine);
				for (int j = 0; j< index[0].length; j++){
					GraphNode current = index[i][j];
					int currentNorthWeight = input.nextInt();
					if (currentNorthWeight!=-1){
						current.setNorth(index[i-1][j], currentNorthWeight);
					}
					
					int currentSouthWeight = input.nextInt();
					if (currentSouthWeight!=-1){
						current.setSouth(index[i+1][j], currentSouthWeight);
					}
					
					int currentWestWeight = input.nextInt();
					if (currentWestWeight!=-1){
						current.setWest(index[i][j-1], currentWestWeight);
					}
					
					int currentEastWeight = input.nextInt();
					if (currentEastWeight!=-1){
						current.setEast(index[i][j+1], currentEastWeight);
					}
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Missing file");
		}
		
		
	}
	
	public GraphNode getHome() {
		return home;
	}
	
}

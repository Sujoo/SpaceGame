package sujoo.games.spacegame;

import java.util.List;
import java.util.Random;

import sujoo.games.spacegame.datatypes.Star;

import com.google.common.collect.Lists;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class StarSystemUtil {
	
	private int minStarId;
	private int totalStarSystems;
	private int maximumConnections;
	private UndirectedSparseGraph<Star, String> starGraph;
	private Random random;
	
	public StarSystemUtil(int minStarId, int totalStarSystems, int maximumConnections) {
		this.minStarId = minStarId;
		this.totalStarSystems = totalStarSystems;
		this.maximumConnections = maximumConnections;
		starGraph = new UndirectedSparseGraph<Star, String>();
		random = new Random();
		
		createStarSystems();
	}
	
	public UndirectedSparseGraph<Star, String> getStarGraph() {
		return starGraph;
	}
	
	public Star getStarSystem(int id) {
		List<Star> starList = Lists.newArrayList(starGraph.getVertices());
		for (Star star : starList) {
			if (star.getId() == id) {
				return star;
			}
		}
		return null;
	}
	
	public void createStarSystems() {
		List<Star> starSystems = Lists.newArrayList();
		
		for (Integer i : generateUniqueStarIds(totalStarSystems)) {
			starSystems.add(new Star(i));
		}
//		return createStarSystemConnections(starSystems, maximumConnections);
		createStarSystemGraph(starSystems, maximumConnections);
	}
	
	private List<Integer> generateUniqueStarIds(int totalStarSystems) {
		List<Integer> starSystemIds = Lists.newArrayList();
		
		for (int i = 0; i < totalStarSystems; i++) {
			starSystemIds.add(minStarId + i);
		}
		
		return starSystemIds;
	}
	
	private void createStarSystemGraph(List<Star> starSystems, int maximumConnections) {
		for (Star star : starSystems) {
			starGraph.addVertex(star);
		}
		
		// Begin graph by connecting two random systems
		Star random1 = getRandomStarSystem();
		Star random2;
		do {
			random2 = getRandomStarSystem();
		} while (random1.equals(random2));
		starGraph.addEdge(random1 + "_" + random2, random1, random2);
		
		for (Star star : starSystems) {
			while (starGraph.getNeighborCount(star) == 0 || (starGraph.getNeighborCount(star) < maximumConnections && random.nextBoolean())) {
				// Select star system to connection with
				Star connection = starSystems.get(random.nextInt(starSystems.size()));
				if (!star.equals(connection) && !starGraph.isNeighbor(star, connection) && starGraph.getNeighborCount(connection) > 0 && starGraph.getNeighborCount(connection) < maximumConnections) {
					starGraph.addEdge(star + "_" + connection, star, connection);
				}
			}
		}
	}
	
	public Star getRandomStarSystem() {
		List<Star> starList = Lists.newArrayList(starGraph.getVertices());
		return starList.get(random.nextInt(starList.size()));
	}
	
	public String getNeighborsString(Star star) {
		List<Star> neighbors = Lists.newArrayList(starGraph.getNeighbors(star));
		
		String result = "";
		for (Star neighbor : neighbors) {
			result += neighbor + "  ";
		}
		return result;
	}
	
	public UndirectedSparseGraph<Star, String> createSubGraph(Star star) {
		UndirectedSparseGraph<Star, String> subGraph = new UndirectedSparseGraph<Star, String>();
		
		subGraph.addVertex(star);
		for (Star neighbor : starGraph.getNeighbors(star)) {
			subGraph.addVertex(neighbor);
			subGraph.addEdge(star + "_" + neighbor, star, neighbor);
		}
		
		return subGraph;
	}
}

package sujoo.games.spacegame.manager;

import java.util.List;
import java.util.Random;

import sujoo.games.spacegame.ai.StationManagerAI;
import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.planet.PlanetFactory;
import sujoo.games.spacegame.datatype.planet.PlanetType;

import com.google.common.collect.Lists;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class StarSystemManager {
	
	private int minStarId;
	private int totalStarSystems;
	private int maximumConnections;
	private int minPlanets;
	private int maxPlanets;
	private UndirectedSparseGraph<Star, String> starGraph;
	private Random random;
	
	public StarSystemManager(int minStarId, int totalStarSystems, int maximumConnections, int minPlanets, int maxPlanets) {
		this.minStarId = minStarId;
		this.totalStarSystems = totalStarSystems;
		this.maximumConnections = maximumConnections;
		this.minPlanets = minPlanets;
		this.maxPlanets = maxPlanets;
		starGraph = new UndirectedSparseGraph<Star, String>();
		random = new Random();
		
		createStarSystems();
	}
	
	public void createStarSystems() {
		List<Star> starSystems = Lists.newArrayList();
		
		for (Integer i : generateUniqueStarIds(totalStarSystems)) {
			Star newStar = new Star(i);
			generatePlanets(newStar);
			generateStationCargo(newStar);
			starSystems.add(newStar);
		}

		createStarSystemGraph(starSystems, maximumConnections);
	}
	
	public void refreshStationCargo() {
		List<Star> stars = Lists.newArrayList(starGraph.getVertices());
		for (Star star : stars) {
			StationManagerAI.refreshStationCargo(star.getStation(), star.getPlanets());
		}
	}
	
	private List<Integer> generateUniqueStarIds(int totalStarSystems) {
		List<Integer> starSystemIds = Lists.newArrayList();
		
		for (int i = 0; i < totalStarSystems; i++) {
			starSystemIds.add(minStarId + i);
		}
		
		return starSystemIds;
	}
	
	private void generatePlanets(Star star) {
		while (star.getPlanets().size() < minPlanets || (star.getPlanets().size() < maxPlanets && random.nextBoolean())) {
			star.addPlanet(PlanetFactory.buildPlanet(PlanetType.values()[random.nextInt(PlanetType.values().length)]));
		}
	}
	
	private void generateStationCargo(Star star) {
		StationManagerAI.fillStationWithCargo(star.getStation(), star.getPlanets());
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
	
	public UndirectedSparseGraph<Star, String> createSubGraph(Star star) {
		UndirectedSparseGraph<Star, String> subGraph = new UndirectedSparseGraph<Star, String>();
		
		subGraph.addVertex(star);
		for (Star neighbor : starGraph.getNeighbors(star)) {
			subGraph.addVertex(neighbor);
			subGraph.addEdge(star + "_" + neighbor, star, neighbor);
		}
		
		return subGraph;
	}
	
	public boolean isNeighbor(Star star1, Star star2) {
		return starGraph.isNeighbor(star1, star2);
	}
	
	public List<Star> getNeighbors(Star star) {
		return Lists.newArrayList(starGraph.getNeighbors(star));
	}
	
	public String getNeighborsString(Star star) {
		List<Star> neighbors = Lists.newArrayList(starGraph.getNeighbors(star));
		
		String result = "";
		for (Star neighbor : neighbors) {
			result += neighbor + "  ";
		}
		return result;
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
}

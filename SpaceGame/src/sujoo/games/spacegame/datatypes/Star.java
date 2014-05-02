package sujoo.games.spacegame.datatypes;

import java.util.List;

import sujoo.games.spacegame.datatypes.planet.Planet;

import com.google.common.collect.Lists;

public class Star implements Comparable<Star> {
	private final int id;
	private List<Planet> planets;
	
	public Star(int id) {
		this.id = id;
		planets = Lists.newArrayList();
	}
	
	public int getId() {
		return id;
	}
	
	public List<Planet> getPlanets() {
		return planets;
	}
	
	public void addPlanet(Planet planet) {
		planets.add(planet);
	}

	@Override
	public int compareTo(Star s) {
		return Integer.compare(id, s.getId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Star other = (Star) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public String toString() {
		return String.valueOf(id);
	}
}
package sujoo.games.spacegame.datatypes;

import java.util.List;

import com.google.common.collect.Lists;

public class Star implements Comparable<Star> {
	private final int id;
	private List<Star> connectedStars;
	
	public Star(int id) {
		this.id = id;
		connectedStars = Lists.newArrayList();
	}
	
	public int getId() {
		return id;
	}
	
	public List<Star> getConnections() {
		return connectedStars;
	}
	
	public int getNumberOfConnections() {
		return connectedStars.size();
	}
	
	public void addConnectedStar(Star s) {
		connectedStars.add(s);
	}
	
	public boolean isConnectedTo(Star s) {
		if(connectedStars.contains(s)) {
			return true;
		} else {
			return false;
		}
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
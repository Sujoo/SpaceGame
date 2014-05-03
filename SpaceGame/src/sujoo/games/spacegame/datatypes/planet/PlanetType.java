package sujoo.games.spacegame.datatypes.planet;

public enum PlanetType {
	CLASS_D("D","Planetoid"), //planetoid or moon
	CLASS_H("H","Uninhabitable"), // generally uninhabitable
	CLASS_J("J","Gas giant"), // gas giant
	CLASS_K("K","Adaptable"), // adaptable with pressure domes
	CLASS_L("L","Marginally habitatble"), // marginally habitable
	CLASS_M("M","Terrestrial"), // terrestrial
	CLASS_N("N","Sulfuric"), // sulfuric
	CLASS_T("T","Super gas giant"); // super gas giant
	
	private String shortDesc;
	private String longDesc;
	private PlanetType(String shortDesc, String longDesc) {
		this.shortDesc = shortDesc;
		this.longDesc = longDesc;
	}
	
	public String getShortDesc() {
		return shortDesc;
	}
	
	public String getLongDesc() {
		return longDesc;
	}
}

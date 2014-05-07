package sujoo.games.spacegame.main;

import sujoo.games.spacegame.manager.GameManager;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameManager controller = new GameManager();
		controller.play();
	}

}

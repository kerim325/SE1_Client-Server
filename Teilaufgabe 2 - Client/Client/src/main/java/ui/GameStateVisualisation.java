package ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import gameData.GameState;

public class GameStateVisualisation implements PropertyChangeListener {

	private GameMapStringBuilder gameMapStringBuilder = new GameMapStringBuilder();

	public GameStateVisualisation(GameState gameState) {
		gameState.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		printToCLI((GameState) evt.getSource());
	}

	private void printToCLI(GameState gameState) {
		String players = gameState.getMyPlayer().orElseThrow().getFirstName() + " "
				+ gameState.getMyPlayer().orElseThrow().getLastName();
		if (gameState.getEnemyPlayer().isPresent())
			players += " vs " + gameState.getEnemyPlayer().orElseThrow().getFirstName() + " "
					+ gameState.getEnemyPlayer().orElseThrow().getLastName();
		System.out.println(players);
		System.out.println("Made moves: " + gameState.getRoundNumber() + "/159");
		System.out.println("Searching: "
				+ (gameState.getMyPlayer().orElseThrow().hasCollectedTreasure() ? "Enemy Fort" : "Treasure"));
		System.out.println(gameMapStringBuilder.gameMapToString(gameState));
	}
}
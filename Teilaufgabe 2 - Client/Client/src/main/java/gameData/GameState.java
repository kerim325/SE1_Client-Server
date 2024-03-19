package gameData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.Optional;

import gameData.map.GameMap;
import gameData.player.Player;

public class GameState {

	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}

	private String gameStateId;
	private int roundNumber;
	private Player myPlayer;
	private Player enemyPlayer;
	private GameMap gameMap;

	public GameState(String gameStateId, Player myPlayer, Player enemyPlayer, GameMap gameMap) {
		this.gameStateId = gameStateId;
		this.myPlayer = myPlayer;
		this.enemyPlayer = enemyPlayer;
		this.gameMap = gameMap;
	}

	public GameState(String gameStateId, Player myPlayer, GameMap gameMap) {
		this.gameStateId = gameStateId;
		this.myPlayer = myPlayer;
		this.gameMap = gameMap;
	}

	public GameState() {
		gameStateId = "empty game state";
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public void incrementRoundNumber() {
		roundNumber++;
	}

	public String getGameStateId() {
		return gameStateId;
	}

	public void setGameStateId(String gameStateId) {
		this.gameStateId = gameStateId;
	}

	public void setGameMap(GameMap gameMap) {
		GameMap beforeChange = this.gameMap;
		this.gameMap = gameMap;
		changes.firePropertyChange("gameMap", beforeChange, gameMap);
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	public Optional<Player> getEnemyPlayer() {
		return Optional.ofNullable(enemyPlayer);
	}

	public void setEnemyPlayer(Player enemyPlayer) {
		this.enemyPlayer = enemyPlayer;
	}

	public Optional<Player> getMyPlayer() {
		return Optional.ofNullable(myPlayer);
	}

	public void setMyPlayer(Player myPlayer) {
		this.myPlayer = myPlayer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gameStateId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameState other = (GameState) obj;
		return Objects.equals(gameStateId, other.gameStateId);
	}

}

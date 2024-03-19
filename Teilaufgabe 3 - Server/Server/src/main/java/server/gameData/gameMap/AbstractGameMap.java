package server.gameData.gameMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractGameMap<T extends AbstractMapNode> {
	private Map<Point, T> gameMap;
	private int maxValX = 0;
	private int maxValY = 0;

	public AbstractGameMap(Map<Point, T> gameMap) {
		setGameMap(gameMap);
	}

	public AbstractGameMap() {
		this(new HashMap<Point, T>());
	}

	public Map<Point, T> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Point, T> gameMap) {
		this.gameMap = gameMap;
		this.maxValX = gameMap.keySet().stream().mapToInt(Point::getX).max().orElse(0);
		this.maxValY = gameMap.keySet().stream().mapToInt(Point::getY).max().orElse(0);
	}

	public Optional<T> getNode(Point point) {
		return gameMap.entrySet().stream().filter(pointNode -> pointNode.getKey().equals(point))
				.map(Map.Entry::getValue).findFirst();
	}

	public int getMaxValX() {
		return maxValX;
	}

	public int getMaxValY() {
		return maxValY;
	}

	public boolean mapIsEmpty() {
		return maxValX == 0 && maxValY == 0;
	}

}

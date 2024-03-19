package gameData.map;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractMap {
	private Map<Point, MapNode> gameMap;
	private int maxValX;
	private int maxValY;

	public AbstractMap(Map<Point, MapNode> gameMap) {
		this.gameMap = gameMap;
		this.maxValX = gameMap.keySet().stream().max(Comparator.comparingInt(Point::getX)).orElse(Point.of(0, 0))
				.getX();
		this.maxValY = gameMap.keySet().stream().max(Comparator.comparingInt(Point::getY)).orElse(Point.of(0, 0))
				.getY();
	}

	public Map<Point, MapNode> getGameMap() {
		return gameMap;
	}

	public Optional<MapNode> getNode(int x, int y) {
		return gameMap.entrySet().stream().filter(a -> a.getKey().getX() == x && a.getKey().getY() == y)
				.map(Map.Entry::getValue).findFirst();
	}

	public int getMaxValX() {
		return maxValX;
	}

	public int getMaxValY() {
		return maxValY;
	}

}
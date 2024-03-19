package server.gameData.gameMap;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class GameMap extends AbstractGameMap<MapNode> {

	public GameMap(Map<Point, MapNode> gameMap) {
		super(gameMap);
	}

	public GameMap() {
		super();
	}

	public Optional<Point> getRandomMapPosition() {
		if (mapIsEmpty())
			return Optional.empty();

		Random randomIntGenerator = new Random();
		return Optional.ofNullable(
				Point.of(randomIntGenerator.nextInt(getMaxValX()), randomIntGenerator.nextInt(getMaxValY())));
	}

}

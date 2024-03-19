package gameData.map.halfGameMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import gameData.map.FortPositionState;
import gameData.map.MapNode;
import gameData.map.NodeType;
import gameData.map.PlayerPositionState;
import gameData.map.Point;
import gameData.map.TreasurePositionState;

public class HalfGameMapGenerator {

	HalfGameMapChecker halfGameMapChecker;
	private static final int X_MAX = 9;
	private static final int Y_MAX = 4;
	private static final double WATER_PROBABILITY = 0.14;
	private static final double MOUNTAIN_PROBABILITY = 0.15;

	public HalfGameMapGenerator(HalfGameMapChecker halfGameMapChecker) {
		this.halfGameMapChecker = halfGameMapChecker;
	}

	public HalfGameMapGenerator() {
		this.halfGameMapChecker = new HalfGameMapChecker();
	}

	public HalfGameMap generateHalfGameMap() {
		HalfGameMap gameMap = generator();
		while (!halfGameMapChecker.checkHalfGameMap(gameMap))
			gameMap = generator();
		return gameMap;
	}

	private HalfGameMap generator() {
		Map<Point, MapNode> ret = new HashMap<>();

		Random rand = new Random();
		int xRandom = rand.nextInt(X_MAX + 1);
		int yRandom = rand.nextInt(Y_MAX + 1);

		for (int i = 0; i <= X_MAX; i++)
			for (int j = 0; j <= Y_MAX; j++) {

				NodeType nodeType;

				double p = Math.random();
				if (p <= WATER_PROBABILITY)
					nodeType = NodeType.Water;
				else if (p <= MOUNTAIN_PROBABILITY + WATER_PROBABILITY)
					nodeType = NodeType.Mountain;
				else
					nodeType = NodeType.Grass;

				if (i == xRandom && j == yRandom)
					ret.put(Point.of(i, j), new MapNode(nodeType, PlayerPositionState.NoPlayerPresent,
							FortPositionState.MyFortPresent, TreasurePositionState.NoTreasure));
				else
					ret.put(Point.of(i, j), new MapNode(nodeType, PlayerPositionState.NoPlayerPresent,
							FortPositionState.NoFortPresent, TreasurePositionState.NoTreasure));
			}

		return new HalfGameMap(ret);
	}

	public static int getxMax() {
		return X_MAX;
	}

	public static int getyMax() {
		return Y_MAX;
	}

}

package gameData.map.halfGameMap;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import gameData.map.FortPositionState;
import gameData.map.MapNode;
import gameData.map.NodeType;
import gameData.map.Point;

public class HalfGameMapChecker {
	// TAKEN FROM <1>
	// https://en.wikipedia.org/wiki/Flood_fill
	// I only made use of the wikipedia article to understand what flood fill is.
	// There is also a pseudocode provided.

	public boolean checkHalfGameMap(HalfGameMap gameMap) {

		if (gameMap.getMaxValX() != 9)
			return false;
		if (gameMap.getMaxValY() != 4)
			return false;

		long numberOfGrassNodes = numberOfGrassNodes(gameMap);
		long numberOfMountainNodes = numberOfMountainNodes(gameMap);
		long numberOfWaterNodes = numberOfWaterNodes(gameMap);

		if (numberOfGrassNodes + numberOfMountainNodes + numberOfWaterNodes != 50)
			return false;

		if (numberOfGrassNodes < 24)
			return false;

		if (numberOfMountainNodes < 5)
			return false;

		if (numberOfWaterNodes < 7)
			return false;

		Set<MapNode> fortNode = getFortNode(gameMap);

		if (fortNode.size() != 1)
			return false;
		if (!fortNode.stream().findFirst().orElseThrow().getNodeType().equals(NodeType.Grass))
			return false;

		if (checkMapBordersForWaterLimit(gameMap))
			return false;

		if (mapHasIsland(gameMap))
			return false;

		return true;
	}

	private boolean mapHasIsland(HalfGameMap gameMap) {
		Optional<Point> startingPoint = getFirstNoneWaterPoint(gameMap);
		if (startingPoint.isEmpty())
			return true;

		Set<Point> reachableNodes = new HashSet<>();
		visitEveryReachableNode(reachableNodes, startingPoint.orElseThrow(), gameMap);

		if (reachableNodes.stream().count() != (numberOfMountainNodes(gameMap) + numberOfGrassNodes(gameMap)))
			return true;

		return false;
	}

	// TAKEN FROM START <1>
	private void visitEveryReachableNode(Set<Point> ret, Point currentPoint, HalfGameMap gameMap) {
		Optional<MapNode> tempNode = gameMap.getNode(currentPoint.getX(), currentPoint.getY());
		Point tempPoint = Point.of(currentPoint.getX(), currentPoint.getY());
		if (tempNode.isEmpty() || tempNode.orElseThrow().getNodeType().equals(NodeType.Water)
				|| ret.contains(tempPoint))
			return;

		ret.add(tempPoint);

		visitEveryReachableNode(ret, Point.of(currentPoint.getX(), currentPoint.getY() - 1), gameMap);
		visitEveryReachableNode(ret, Point.of(currentPoint.getX(), currentPoint.getY() + 1), gameMap);
		visitEveryReachableNode(ret, Point.of(currentPoint.getX() - 1, currentPoint.getY()), gameMap);
		visitEveryReachableNode(ret, Point.of(currentPoint.getX() + 1, currentPoint.getY()), gameMap);
	}
	// TAKEN FROM END <1>

	private Optional<Point> getFirstNoneWaterPoint(HalfGameMap gameMap) throws NoSuchElementException {
		return gameMap.getGameMap().entrySet().stream().filter(a -> !a.getValue().getNodeType().equals(NodeType.Water))
				.min(Comparator.<Map.Entry<Point, MapNode>>comparingInt(a -> a.getKey().getX())
						.thenComparingInt(a -> a.getKey().getY()))
				.map(p -> p.getKey());
	}

	private boolean checkMapBordersForWaterLimit(HalfGameMap gameMap) {
		if (gameMap.getGameMap().entrySet().stream().filter(a -> a.getKey().getX() == 0)
				.filter(a -> a.getValue().getNodeType().equals(NodeType.Water)).count() > 2)
			return true;
		if (gameMap.getGameMap().entrySet().stream().filter(a -> a.getKey().getX() == gameMap.getMaxValX())
				.filter(a -> a.getValue().getNodeType().equals(NodeType.Water)).count() > 2)
			return true;
		if (gameMap.getGameMap().entrySet().stream().filter(a -> a.getKey().getY() == 0)
				.filter(a -> a.getValue().getNodeType().equals(NodeType.Water)).count() > 4)
			return true;
		if (gameMap.getGameMap().entrySet().stream().filter(a -> a.getKey().getY() == gameMap.getMaxValY())
				.filter(a -> a.getValue().getNodeType().equals(NodeType.Water)).count() > 4)
			return true;
		return false;
	}

	private Set<MapNode> getFortNode(HalfGameMap gameMap) {
		return gameMap.getGameMap().values().stream()
				.filter(a -> a.getFortPositionState().equals(FortPositionState.MyFortPresent))
				.collect(Collectors.toSet());
	}

	private long numberOfWaterNodes(HalfGameMap gameMap) {
		return gameMap.getGameMap().values().stream().filter(a -> a.getNodeType().equals(NodeType.Water)).count();
	}

	private long numberOfMountainNodes(HalfGameMap gameMap) {
		return gameMap.getGameMap().values().stream().filter(a -> a.getNodeType().equals(NodeType.Mountain)).count();
	}

	private long numberOfGrassNodes(HalfGameMap gameMap) {
		return gameMap.getGameMap().values().stream().filter(a -> a.getNodeType().equals(NodeType.Grass)).count();
	}

}

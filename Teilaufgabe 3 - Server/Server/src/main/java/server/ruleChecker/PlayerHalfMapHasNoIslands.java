package server.ruleChecker;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.PlayerHalfMapRuleViolationException;
import server.gameData.gameMap.Point;

public class PlayerHalfMapHasNoIslands extends AbtractPlayerHalfMapRule {
	// TAKEN FROM <1>
	// https://en.wikipedia.org/wiki/Flood_fill
	// I only made use of the wikipedia article to understand what flood fill is.
	// There is also a pseudocode provided.
	// I also cited this for my client implementation.

	@Override
	PlayerHalfMapRuleViolationException getException() {
		return new PlayerHalfMapRuleViolationException("PlayerHalfMap has island(s)", "PlayerHalfMap has island(s)");
	}

	@Override
	boolean validate(PlayerHalfMap halfMap) {
		return !mapHasIsland(halfMap);
	}

	private boolean mapHasIsland(PlayerHalfMap halfMap) {
		Optional<Point> startingPoint = getFirstNoneWaterPoint(halfMap);
		if (startingPoint.isEmpty())
			return true;

		Set<Point> reachableNodes = new HashSet<>();
		visitEveryReachableNode(reachableNodes, startingPoint.orElseThrow(), halfMap);

		if (reachableNodes.stream().count() != (numberOfMountainNodes(halfMap) + numberOfGrassNodes(halfMap)))
			return true;

		return false;
	}

	// TAKEN FROM START <1>
	private void visitEveryReachableNode(Set<Point> ret, Point currentPoint, PlayerHalfMap halfMap) {
		Optional<PlayerHalfMapNode> tempNode = halfMap.getMapNodes().stream()
				.filter(node -> node.getX() == currentPoint.getX() && node.getY() == currentPoint.getY()).findFirst();
		Point tempPoint = Point.of(currentPoint.getX(), currentPoint.getY());
		if (tempNode.isEmpty() || tempNode.orElseThrow().getTerrain().equals(ETerrain.Water) || ret.contains(tempPoint))
			return;

		ret.add(tempPoint);

		visitEveryReachableNode(ret, Point.of(currentPoint.getX(), currentPoint.getY() - 1), halfMap);
		visitEveryReachableNode(ret, Point.of(currentPoint.getX(), currentPoint.getY() + 1), halfMap);
		visitEveryReachableNode(ret, Point.of(currentPoint.getX() - 1, currentPoint.getY()), halfMap);
		visitEveryReachableNode(ret, Point.of(currentPoint.getX() + 1, currentPoint.getY()), halfMap);
	}
	// TAKEN FROM END <1>

	private Optional<Point> getFirstNoneWaterPoint(PlayerHalfMap halfMap) {
		return halfMap
				.getMapNodes().stream().filter(node -> !node.getTerrain().equals(ETerrain.Water)).min(Comparator
						.<PlayerHalfMapNode>comparingInt(node -> node.getX()).thenComparingInt(node -> node.getY()))
				.map(node -> Point.of(node.getX(), node.getY()));
	}

	private long numberOfMountainNodes(PlayerHalfMap halfMap) {
		return halfMap.getMapNodes().stream().filter(node -> node.getTerrain().equals(ETerrain.Mountain)).count();
	}

	private long numberOfGrassNodes(PlayerHalfMap halfMap) {
		return halfMap.getMapNodes().stream().filter(node -> node.getTerrain().equals(ETerrain.Grass)).count();
	}

}

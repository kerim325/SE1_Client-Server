package navigation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gameData.GameState;
import gameData.map.GameMap;
import gameData.map.MapNode;
import gameData.map.NodeType;
import gameData.map.Point;

public class GoalFinder {

	// TAKEN FROM <1>
	// https://www.redblobgames.com/pathfinding/a-star/introduction.html
	// Shows how to implement Dijkstra’s Algorithm in Python.

	private static Logger logger = LoggerFactory.getLogger(GoalFinder.class);

	private final int MAX_X_VALUE = 19;
	private final int HALF_MAX_X_VALUE = 9;
	private final int MIN_X_VALUE = 0;

	private final int MAX_Y_VALUE = 9;
	private final int HALF_MIN_Y_VALUE = 4;
	private final int MIN_Y_VALUE = 0;

	private int xUpperBorder;
	private int xLowerBorder;
	private int yUpperBorder;
	private int yLowerBorder;

	public Map<Point, Point> findPathToOptimalGoal(GameState gameState, List<Point> visitedPoints) {

		GameMap gameMap = gameState.getGameMap();

		if (!gameState.getMyPlayer().orElseThrow().hasCollectedTreasure())
			setBorderToMyHalfMap(gameMap, visitedPoints);
		else
			setBorderToEnemyHalfMap(gameMap, visitedPoints);

		logger.debug("Set Borders: {}-{}, {}-{}", xLowerBorder, xUpperBorder, yLowerBorder, yUpperBorder);

		if (!gameState.getMyPlayer().orElseThrow().hasCollectedTreasure()) {
			Optional<Point> treasure = gameMap.getMyTreasurePoint();
			if (treasure.isPresent()) {
				Predicate<Point> treasureChecker = p -> p.getX() == treasure.orElseThrow().getX()
						&& p.getY() == treasure.orElseThrow().getY();
				return getPathToNearestNotVisitedNode(gameMap, visitedPoints, treasureChecker);
			}
		} else {
			Optional<Point> enemyFort = gameMap.getEnemyFortPosition();
			if (enemyFort.isPresent()) {
				Predicate<Point> enemyFortChecker = p -> p.getX() == enemyFort.orElseThrow().getX()
						&& p.getY() == enemyFort.orElseThrow().getY();
				return getPathToNearestNotVisitedNode(gameMap, visitedPoints, enemyFortChecker);
			}
		}

		Predicate<Point> mountainChecker = p -> gameMap.getNode(p.getX(), p.getY()).orElseThrow().getNodeType()
				.equals(NodeType.Mountain);
		Map<Point, Point> ret = getPathToNearestNotVisitedNode(gameMap, visitedPoints, mountainChecker);

		if (ret.size() > 1)
			return ret;

		Predicate<Point> grassChecker = p -> gameMap.getNode(p.getX(), p.getY()).orElseThrow().getNodeType()
				.equals(NodeType.Grass);
		return getPathToNearestNotVisitedNode(gameMap, visitedPoints, grassChecker);
	}

	private void setBorderToHalfMap(GameMap gameMap, List<Point> visitedPoints, boolean isEnemyHalfMap) {
		if (gameMap.getMaxValX() == MAX_X_VALUE) {
			if (isEnemyHalfMap == (visitedPoints.get(0).getX() >= HALF_MAX_X_VALUE + 1)) {
				xUpperBorder = HALF_MAX_X_VALUE;
				xLowerBorder = MIN_X_VALUE;
			} else {
				xUpperBorder = MAX_X_VALUE;
				xLowerBorder = HALF_MAX_X_VALUE + 1;
			}

			yUpperBorder = HALF_MIN_Y_VALUE;
			yLowerBorder = MIN_Y_VALUE;

		} else {
			if (isEnemyHalfMap == (visitedPoints.get(0).getY() >= HALF_MIN_Y_VALUE + 1)) {
				yUpperBorder = HALF_MIN_Y_VALUE;
				yLowerBorder = MIN_Y_VALUE;
			} else {
				yUpperBorder = MAX_Y_VALUE;
				yLowerBorder = HALF_MIN_Y_VALUE + 1;
			}

			xUpperBorder = HALF_MAX_X_VALUE;
			xLowerBorder = MIN_X_VALUE;
		}
	}

	private void setBorderToEnemyHalfMap(GameMap gameMap, List<Point> visitedPoints) {
		setBorderToHalfMap(gameMap, visitedPoints, true);
	}

	private void setBorderToMyHalfMap(GameMap gameMap, List<Point> visitedPoints) {
		setBorderToHalfMap(gameMap, visitedPoints, false);
	}

	// TAKEN FROM START <1>
	private Map<Point, Point> getPathToNearestNotVisitedNode(GameMap gameMap, List<Point> visitedPoints,
			Predicate<Point> checkerForWantedNodeType) {
		Point myPlayerPosition = gameMap.getMyPlayerPosition();

		Point goal = myPlayerPosition;

		Map<Point, Point> cameFrom = new HashMap<>();
		Map<Point, Integer> costSoFar = new HashMap<>();
		cameFrom.put(myPlayerPosition, myPlayerPosition);
		costSoFar.put(myPlayerPosition, 0);

		PriorityQueue<Point> frontier = new PriorityQueue<>(
				Comparator.comparingInt(p -> costSoFar.getOrDefault(p, Integer.MAX_VALUE)));
		frontier.add(myPlayerPosition);

		while (!frontier.isEmpty()) {
			Point current = frontier.poll();

			if (checkerForWantedNodeType.test(current) && !visitedPoints.contains(current)
					&& !outsideOfBorder(current.getX(), current.getY())
					&& checkIfOptimal(current, gameMap, visitedPoints)) {
				goal = current;
				break;
			}

			List<Point> neighbor = gameMap.getVisitableNeighbors(current.getX(), current.getY());

			for (Point next : neighbor) {
				Integer new_cost = costSoFar.get(current)
						+ gameMap.getNode(current.getX(), current.getY()).orElseThrow().getNodeType().getCost()
						+ gameMap.getNode(next.getX(), next.getY()).orElseThrow().getNodeType().getCost();

				if (!costSoFar.containsKey(next) || new_cost < costSoFar.get(next)) {
					costSoFar.put(next, new_cost);
					frontier.add(next);
					cameFrom.put(next, current);
				}
			}
		}

		Map<Point, Point> ret = new HashMap<>();
		Point currentPoint = goal;
		while (!cameFrom.get(currentPoint).equals(currentPoint)) {
			Point nextPoint = cameFrom.get(currentPoint);
			ret.put(currentPoint, nextPoint);
			currentPoint = nextPoint;
		}
		ret.put(currentPoint, currentPoint);
		return ret;
	}
	// TAKEN FROM END <1>

	private boolean checkIfOptimal(Point goal, GameMap gameMap, List<Point> visitedPoints) {
		MapNode goalNode = gameMap.getNode(goal.getX(), goal.getY()).orElseThrow();

		long temp = gameMap.getSeeableNeighbors(goal.getX(), goal.getY()).stream()
				.filter(p -> !visitedPoints.contains(p)).count();
		if (goalNode.getNodeType().equals(NodeType.Mountain))
			if (temp <= 1)
				return false;

		return true;
	}

	private boolean outsideOfBorder(int x, int y) {
		return (x < xLowerBorder || x > xUpperBorder || y < yLowerBorder || y > yUpperBorder);
	}

}

package navigation;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gameData.GameState;
import gameData.map.FortPositionState;
import gameData.map.GameMap;
import gameData.map.NodeType;
import gameData.map.Point;
import gameData.map.TreasurePositionState;

public class NavigationManager {

	private static Logger logger = LoggerFactory.getLogger(NavigationManager.class);

	private Point goal;
	private GoalFinder goalFinder;
	private Deque<NextMove> movesToReachCurrentGoal;
	private List<Point> visitedPoints;

	public NavigationManager(GoalFinder goalFinder) {
		this.goalFinder = goalFinder;
		this.movesToReachCurrentGoal = new LinkedList<NextMove>();
		this.visitedPoints = new ArrayList<Point>();
	}

	public NavigationManager() {
		this.goalFinder = new GoalFinder();
		this.movesToReachCurrentGoal = new LinkedList<NextMove>();
		this.visitedPoints = new ArrayList<Point>();
	}

	public List<Point> getVisitedPoints() {
		return visitedPoints;
	}

	private void updateGoal(GameState gameState) {
		logger.info("Calculating new goal, finding optimal path.");
		Map<Point, Point> pathFromCurrentPositionToNewGoal = goalFinder.findPathToOptimalGoal(gameState, visitedPoints);

		if (pathFromCurrentPositionToNewGoal.size() <= 1)
			throw new NoGoalFoundException("The goal finder could not find an optimal goal.");

		setGoal(pathFromCurrentPositionToNewGoal);
		setMovesToReachCurrentGoal(gameState, pathFromCurrentPositionToNewGoal);
		logger.info("New Goal: {}", goal);
	}

	private void setGoal(Map<Point, Point> pathFromCurrentPositionToNewGoal) {
		goal = pathFromCurrentPositionToNewGoal.entrySet().stream()
				.filter(p -> !pathFromCurrentPositionToNewGoal.containsValue(p.getKey())).findFirst().orElseThrow()
				.getKey();
	}

	private void setMovesToReachCurrentGoal(GameState gameState, Map<Point, Point> pathFromCurrentPositionToNewGoal) {
		movesToReachCurrentGoal.clear();
		Point currentPoint = goal;
		while (!pathFromCurrentPositionToNewGoal.get(currentPoint).equals(currentPoint)) {
			Point nextPoint = pathFromCurrentPositionToNewGoal.get(currentPoint);
			NextMove nextMove = convertToNextMove(currentPoint, nextPoint);
			for (int i = 0; i < gameState.getGameMap().getGameMap().get(currentPoint).getNodeType().getCost()
					+ gameState.getGameMap().getGameMap().get(nextPoint).getNodeType().getCost(); i++)
				movesToReachCurrentGoal.addFirst(nextMove);
			currentPoint = nextPoint;
		}
	}

	public Deque<NextMove> getPathToCurrentGoal() {
		return movesToReachCurrentGoal;
	}

	private NextMove convertToNextMove(Point currentPoint, Point nextPoint) {
		if (nextPoint.getX() == currentPoint.getX() - 1 && nextPoint.getY() == currentPoint.getY())
			return NextMove.Right;

		if (nextPoint.getX() - 1 == currentPoint.getX() && nextPoint.getY() == currentPoint.getY())
			return NextMove.Left;

		if (nextPoint.getY() - 1 == currentPoint.getY() && nextPoint.getX() == currentPoint.getX())
			return NextMove.Up;

		if (nextPoint.getY() == currentPoint.getY() - 1 && nextPoint.getX() == currentPoint.getX())
			return NextMove.Down;

		throw new IllegalArgumentException("The passed Points " + currentPoint.toString() + " and "
				+ nextPoint.toString() + " are not neighbors.");
	}

	public NextMove getNextMove(GameState gameState) {
		GameMap gameMap = gameState.getGameMap();
		Point myPlayerPosition = gameState.getGameMap().getMyPlayerPosition();

		if (!visitedPoints.contains(myPlayerPosition)) {
			if (gameMap.getNode(myPlayerPosition.getX(), myPlayerPosition.getY()).orElseThrow().getNodeType()
					.equals(NodeType.Mountain)) {
				gameMap.getSeeableNeighbors(myPlayerPosition.getX(), myPlayerPosition.getY()).forEach(p -> {
					if (gameState.getGameMap().getNode(p.getX(), p.getY()).orElseThrow().getTreasurePositionState()
							.equals(TreasurePositionState.NoTreasure)
							&& !gameState.getGameMap().getNode(p.getX(), p.getY()).orElseThrow().getFortPositionState()
									.equals(FortPositionState.EnemyFortPresent))
						visitedPoints.add(p);
					logger.debug("New explored node: {}", p);
				});
			}
			visitedPoints.add(myPlayerPosition);
			logger.debug("New explored node: {}", myPlayerPosition);
		}

		while (movesToReachCurrentGoal.isEmpty())
			updateGoal(gameState);

		logger.info("Made move: {}", movesToReachCurrentGoal.peek());
		return movesToReachCurrentGoal.removeFirst();
	}

}

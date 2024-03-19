package navigation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import gameData.GameState;
import gameData.map.FortPositionState;
import gameData.map.GameMap;
import gameData.map.MapNode;
import gameData.map.NodeType;
import gameData.map.PlayerPositionState;
import gameData.map.Point;
import gameData.player.Player;
import gameData.player.PlayerState;

public class NavigationManagerTests {

	private static Map<Point, MapNode> gameMapGenerator(List<MapNode> nodes, int width, int height) {
		Map<Point, MapNode> map = new HashMap<>();
		for (int i = 0; i < nodes.size(); i += width)
			for (int j = 0; j < nodes.size() / height; j++)
				map.put(Point.of(j, i / width), nodes.get(i + j));
		return map;
	}

	@Test
	public void givenGoalFinderCantFindNewGoal_tryFindingNewGaol_expectNoGoalFoundException() {
		// arrange
		List<MapNode> nodes = new ArrayList<>();

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, PlayerPositionState.MyPlayerPresent, FortPositionState.MyFortPresent));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		Map<Point, MapNode> mapNodes = gameMapGenerator(nodes, 10, 10);
		GameMap gameMap = new GameMap(mapNodes);

		Player myPlayer = new Player("myPlayerId", "firstName", "LastName", "uAccount", false, PlayerState.MustAct);
		String gameStateId = "gameStateId";
		GameState gameState = new GameState(gameStateId, myPlayer, gameMap);

		Point myPlayerPosition = mapNodes.entrySet().stream()
				.filter(p -> p.getValue().getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
				.findFirst().orElseThrow().getKey();

		GoalFinder mockGoalFinder = Mockito.mock(GoalFinder.class);
		List<Point> visitedNodes = new ArrayList<>();
		visitedNodes.add(myPlayerPosition);
		NavigationManager navigationManager = new NavigationManager(mockGoalFinder);
		Map<Point, Point> ret = new HashMap<>();
		ret.put(myPlayerPosition, myPlayerPosition);
		when(mockGoalFinder.findPathToOptimalGoal(gameState, visitedNodes)).thenReturn(ret);

		// act
		Executable testCode = () -> {
			navigationManager.getNextMove(gameState);
		};
		// assert
		Assertions.assertThrows(NoGoalFoundException.class, testCode);
	}

	@Test
	public void givenHalfGameMapChecker_generateHalfGameMap_expectUsageOfHalfGameMapChecker() {
		// arrange
		List<MapNode> nodes = new ArrayList<>();

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, PlayerPositionState.MyPlayerPresent, FortPositionState.MyFortPresent));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		Map<Point, MapNode> mapNodes = gameMapGenerator(nodes, 10, 10);
		GameMap gameMap = new GameMap(mapNodes);

		Player myPlayer = new Player("myPlayerId", "firstName", "LastName", "uAccount", false, PlayerState.MustAct);
		String gameStateId = "gameStateId";
		GameState gameState = new GameState(gameStateId, myPlayer, gameMap);

		Point myPlayerPosition = mapNodes.entrySet().stream()
				.filter(p -> p.getValue().getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
				.findFirst().orElseThrow().getKey();

		GoalFinder mockGoalFinder = Mockito.mock(GoalFinder.class);
		List<Point> visitedNodes = new ArrayList<>();
		visitedNodes.add(myPlayerPosition);
		NavigationManager navigationManager = new NavigationManager(mockGoalFinder);
		Map<Point, Point> ret = new HashMap<>();
		ret.put(myPlayerPosition, myPlayerPosition);
		ret.put(Point.of(7, 4), myPlayerPosition);
		when(mockGoalFinder.findPathToOptimalGoal(gameState, visitedNodes)).thenReturn(ret);

		// act
		NextMove move1 = navigationManager.getNextMove(gameState);
		NextMove move2 = navigationManager.getNextMove(gameState);
		NextMove move3 = navigationManager.getNextMove(gameState);
		NextMove move4 = navigationManager.getNextMove(gameState);
		// assert
		assertThat(move1, is(equalTo(NextMove.Down)));
		assertThat(move2, is(equalTo(NextMove.Down)));
		assertThat(move3, is(equalTo(NextMove.Down)));
		assertThat(move4, is(equalTo(NextMove.Down)));
	}

	@Test
	public void givenGameMapWithPlayerOnMountain_makesMove_expectNeighborNodesToBeMarkedAsVisited() {
		// arrange
		List<MapNode> nodes = new ArrayList<>();

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, FortPositionState.MyFortPresent));
		nodes.add(new MapNode(NodeType.Mountain, PlayerPositionState.MyPlayerPresent));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		Map<Point, MapNode> mapNodes = gameMapGenerator(nodes, 10, 10);
		GameMap gameMap = new GameMap(mapNodes);

		Player myPlayer = new Player("myPlayerId", "firstName", "LastName", "uAccount", false, PlayerState.MustAct);
		String gameStateId = "gameStateId";
		GameState gameState = new GameState(gameStateId, myPlayer, gameMap);

		Point myPlayerPosition = mapNodes.entrySet().stream()
				.filter(p -> p.getValue().getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
				.findFirst().orElseThrow().getKey();

		GoalFinder mock = Mockito.mock(GoalFinder.class);
		NavigationManager navigationManager = new NavigationManager(mock);
		Map<Point, Point> ret = new HashMap<>();
		ret.put(myPlayerPosition, myPlayerPosition);
		ret.put(Point.of(7, 2), myPlayerPosition);
		when(mock.findPathToOptimalGoal(Mockito.any(GameState.class), ArgumentMatchers.anyList())).thenReturn(ret);

		List<Point> pointsToBeVisited = new ArrayList<Point>();
		pointsToBeVisited.add(Point.of(7, 3));
		pointsToBeVisited.add(Point.of(7, 4));
		pointsToBeVisited.add(Point.of(7, 2));
		pointsToBeVisited.add(Point.of(6, 4));
		pointsToBeVisited.add(Point.of(6, 2));

		// act
		navigationManager.getNextMove(gameState);

		// assert
		List<Point> visitedPoints = navigationManager.getVisitedPoints();
		assertThat(visitedPoints, containsInAnyOrder(pointsToBeVisited.toArray()));
	}

	@Test
	public void givenGameMap_makingMoveAndCalculatingUnwalkablePathWithHole_expectIllegalArgumentException() {
		// arrange
		List<MapNode> nodes = new ArrayList<>();

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, PlayerPositionState.MyPlayerPresent, FortPositionState.MyFortPresent));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		Map<Point, MapNode> mapNodes = gameMapGenerator(nodes, 10, 10);
		GameMap gameMap = new GameMap(mapNodes);

		Player myPlayer = new Player("myPlayerId", "firstName", "LastName", "uAccount", false, PlayerState.MustAct);
		String gameStateId = "gameStateId";
		GameState gameState = new GameState(gameStateId, myPlayer, gameMap);

		Point myPlayerPosition = mapNodes.entrySet().stream()
				.filter(p -> p.getValue().getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
				.findFirst().orElseThrow().getKey();

		GoalFinder mock = Mockito.mock(GoalFinder.class);
		NavigationManager navigationManager = new NavigationManager(mock);
		Map<Point, Point> ret = new HashMap<>();
		ret.put(myPlayerPosition, myPlayerPosition);
		ret.put(Point.of(7, 2), myPlayerPosition);
		when(mock.findPathToOptimalGoal(Mockito.any(GameState.class), ArgumentMatchers.anyList())).thenReturn(ret);

		// act
		Executable testCode = () -> {
			navigationManager.getNextMove(gameState);
		};

		// assert
		Assertions.assertThrows(IllegalArgumentException.class, testCode);
	}
}

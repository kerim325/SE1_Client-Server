package navigation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import gameData.GameState;
import gameData.map.FortPositionState;
import gameData.map.GameMap;
import gameData.map.MapNode;
import gameData.map.NodeType;
import gameData.map.PlayerPositionState;
import gameData.map.Point;
import gameData.map.TreasurePositionState;
import gameData.player.Player;
import gameData.player.PlayerState;

public class GoalFinderTests {

	private static Map<Point, MapNode> gameMapGenerator(List<MapNode> nodes, int width, int height) {
		Map<Point, MapNode> map = new HashMap<>();
		for (int i = 0; i < nodes.size(); i += width)
			for (int j = 0; j < nodes.size() / height; j++)
				map.put(Point.of(j, i / width), nodes.get(i + j));
		return map;
	}

	@Test
	public void givenGameMap_findNewGaol_expectShortestPathToClosestUnvisitedMountain() {
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
		nodes.add(new MapNode(NodeType.Grass, PlayerPositionState.MyPlayerPresent));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, FortPositionState.MyFortPresent));
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
		GoalFinder goalFinder = new GoalFinder();
		Point myPlayerPosition = mapNodes.entrySet().stream()
				.filter(p -> p.getValue().getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
				.findFirst().orElseThrow().getKey();
		List<Point> visitedNodes = new ArrayList<>();
		visitedNodes.add(myPlayerPosition);

		// act
		Map<Point, Point> pathToOptimalGoal = goalFinder.findPathToOptimalGoal(gameState, visitedNodes);

		// assert
		assertThat(pathToOptimalGoal.get(myPlayerPosition), is(equalTo(myPlayerPosition)));
		assertThat(pathToOptimalGoal.get(Point.of(4, 3)), is(equalTo(myPlayerPosition)));
		assertThat(pathToOptimalGoal.get(Point.of(4, 2)), is(equalTo(Point.of(4, 3))));
		assertThat(pathToOptimalGoal.size(), is(equalTo(3)));
	}

	@Test
	public void givenGameMapWithTreasure_findNewGoal_expectTreasureNodeToBeNewGoal() {
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
		nodes.add(new MapNode(NodeType.Grass, TreasurePositionState.MyTreasurePosition));
		nodes.add(new MapNode(NodeType.Mountain, PlayerPositionState.MyPlayerPresent));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, FortPositionState.MyFortPresent));
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
		GoalFinder goalFinder = new GoalFinder();
		Point myPlayerPosition = mapNodes.entrySet().stream()
				.filter(p -> p.getValue().getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
				.findFirst().orElseThrow().getKey();
		List<Point> visitedNodes = new ArrayList<>();
		visitedNodes.add(myPlayerPosition);

		// act
		Map<Point, Point> pathToOptimalGoal = goalFinder.findPathToOptimalGoal(gameState, visitedNodes);

		// assert
		assertThat(pathToOptimalGoal.get(myPlayerPosition), is(equalTo(myPlayerPosition)));
		assertThat(pathToOptimalGoal.get(Point.of(1, 3)), is(equalTo(myPlayerPosition)));
		assertThat(pathToOptimalGoal.size(), is(equalTo(2)));
	}

	@Test
	public void givenGameMapWithEnemyFort_findNewGaol_expectShortestPathToEnemyFort() {
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
		nodes.add(new MapNode(NodeType.Grass, FortPositionState.EnemyFortPresent));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass, PlayerPositionState.MyPlayerPresent));
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
		nodes.add(new MapNode(NodeType.Grass, FortPositionState.MyFortPresent));
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

		Map<Point, MapNode> mapNodes = gameMapGenerator(nodes, 20, 5);
		GameMap gameMap = new GameMap(mapNodes);
		Player myPlayer = new Player("myPlayerId", "firstName", "LastName", "uAccount", true, PlayerState.MustAct);
		String gameStateId = "gameStateId";
		GameState gameState = new GameState(gameStateId, myPlayer, gameMap);
		GoalFinder goalFinder = new GoalFinder();
		Point myPlayerPosition = mapNodes.entrySet().stream()
				.filter(p -> p.getValue().getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
				.findFirst().orElseThrow().getKey();
		List<Point> visitedNodes = new ArrayList<>();
		visitedNodes.add(myPlayerPosition);

		// act
		Map<Point, Point> pathToOptimalGoal = goalFinder.findPathToOptimalGoal(gameState, visitedNodes);

		// assert
		assertThat(pathToOptimalGoal.get(myPlayerPosition), is(equalTo(myPlayerPosition)));
		assertThat(pathToOptimalGoal.get(Point.of(11, 1)), is(equalTo(myPlayerPosition)));
		assertThat(pathToOptimalGoal.get(Point.of(10, 1)), is(equalTo(Point.of(11, 1))));
		assertThat(pathToOptimalGoal.get(Point.of(9, 1)), is(equalTo(Point.of(10, 1))));
		assertThat(pathToOptimalGoal.get(Point.of(9, 0)), is(equalTo(Point.of(9, 1))));
		assertThat(pathToOptimalGoal.size(), is(equalTo(5)));
	}
}

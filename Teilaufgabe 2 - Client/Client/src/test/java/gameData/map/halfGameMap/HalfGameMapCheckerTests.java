package gameData.map.halfGameMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import gameData.map.FortPositionState;
import gameData.map.MapNode;
import gameData.map.NodeType;
import gameData.map.Point;

public class HalfGameMapCheckerTests {

	private static Map<Point, MapNode> mapGeneratorHelper(List<MapNode> nodes) {
		Map<Point, MapNode> map = new HashMap<>();
		for (int i = 0; i < nodes.size(); i += 10)
			for (int j = 0; j < nodes.size() / 5; j++)
				map.put(Point.of(j, i / 10), nodes.get(i + j));
		return map;
	}

	@Test
	public void givenHalfMapWithNoWaterNodes_checkingTheHalfMap_expectFalse() {
		// arrange
		HalfGameMapChecker halfGameMapChecker = new HalfGameMapChecker();

		List<MapNode> nodes = new ArrayList<>();

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
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
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, FortPositionState.MyFortPresent));
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

		Map<Point, MapNode> map = mapGeneratorHelper(nodes);

		HalfGameMap halfMap = new HalfGameMap(map);

		// act
		boolean result = halfGameMapChecker.checkHalfGameMap(halfMap);

		// assert
		assertThat(result, is(equalTo(false)));
	}

	private static Stream<Arguments> getHalfMapsWithIslands() {

		// -------------------------------------
		List<MapNode> nodes = new ArrayList<>();

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, FortPositionState.MyFortPresent));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		HalfGameMap halfMap1 = new HalfGameMap(mapGeneratorHelper(nodes));

		// -------------------------------------

		nodes = new ArrayList<>();

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));

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
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass, FortPositionState.MyFortPresent));
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

		HalfGameMap halfMap2 = new HalfGameMap(mapGeneratorHelper(nodes));

		// -------------------------------------

		nodes = new ArrayList<>();

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));

		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));
		nodes.add(new MapNode(NodeType.Water));

		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
		nodes.add(new MapNode(NodeType.Mountain));
		nodes.add(new MapNode(NodeType.Grass));
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

		HalfGameMap halfMap3 = new HalfGameMap(mapGeneratorHelper(nodes));

		// -------------------------------------

		return Stream.of(Arguments.of(halfMap1, false), Arguments.of(halfMap2, false), Arguments.of(halfMap3, false));
	}

	@ParameterizedTest
	@MethodSource("getHalfMapsWithIslands")
	public void givenHalfMapsWithIslands_checkingHalfMaps_expectFalse(HalfGameMap halfMap, boolean expectedResult) {
		// arrange
		HalfGameMapChecker halfGameMapChecker = new HalfGameMapChecker();

		// act
		boolean result = halfGameMapChecker.checkHalfGameMap(halfMap);

		// assert
		assertThat(result, is(equalTo(expectedResult)));
	}

	@Test
	public void givenHalfMapWithNoFort_checkingHalfMap_expectFalse() {
		// arrange
		HalfGameMapChecker halfGameMapChecker = new HalfGameMapChecker();
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

		HalfGameMap halfMap = new HalfGameMap(mapGeneratorHelper(nodes));
		// act
		boolean result = halfGameMapChecker.checkHalfGameMap(halfMap);

		// assert
		assertThat(result, is(equalTo(false)));
	}

}
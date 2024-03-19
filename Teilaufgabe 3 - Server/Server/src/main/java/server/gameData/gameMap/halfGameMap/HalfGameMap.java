package server.gameData.gameMap.halfGameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import server.gameData.gameMap.AbstractGameMap;
import server.gameData.gameMap.GameMap;
import server.gameData.gameMap.MapNode;
import server.gameData.gameMap.NodeType;
import server.gameData.gameMap.Point;

public class HalfGameMap extends AbstractGameMap<HalfGameMapNode> {

	public HalfGameMap(Map<Point, HalfGameMapNode> gameMap) {
		super(gameMap);
	}

	public List<Point> getAllGrassNodes() {
		List<Point> grassNodes = new ArrayList<>();

		for (Map.Entry<Point, HalfGameMapNode> entry : getGameMap().entrySet()) {
			Point point = entry.getKey();
			HalfGameMapNode mapNode = entry.getValue();

			if (mapNode.getNodeType().equals(NodeType.Grass))
				grassNodes.add(point);
		}

		return grassNodes;
	}

	public GameMap halfGameMapToGameMap() {
		Map<Point, MapNode> gameMapNodes = getGameMap().entrySet().stream()
				.collect(Collectors.toMap(node -> node.getKey(), node -> new MapNode(node.getValue().getNodeType())));
		return new GameMap(gameMapNodes);
	}

	public Point placeTreasureOnRandomGrassNode() {
		List<Point> allGrassNodes = getAllGrassNodes();
		Point fortPosition = getFortPosition().orElseThrow();
		allGrassNodes.remove(fortPosition);

		int randomNumber = new Random().nextInt(allGrassNodes.size());
		Point treasureNodePosition = allGrassNodes.get(randomNumber);

		getGameMap().get(treasureNodePosition).setTreasurePositionState(TreasurePositionState.MyTreasurePresent);
		return treasureNodePosition;
	}

	public Optional<Point> getFortPosition() {
		return getGameMap().entrySet().stream()
				.filter(node -> node.getValue().getFortPositionState().equals(FortPositionState.MyFortPresent))
				.map(node -> node.getKey()).findFirst();
	}

}

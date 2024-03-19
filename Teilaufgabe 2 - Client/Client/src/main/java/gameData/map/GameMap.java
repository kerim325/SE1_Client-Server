package gameData.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameMap extends AbstractMap {

	public GameMap(Map<Point, MapNode> gameMap) {
		super(gameMap);
	}

	public Point getMyPlayerPosition() {
		return getGameMap().entrySet().stream()
				.filter(n -> n.getValue().getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent)
						|| n.getValue().getPlayerPositionState().equals(PlayerPositionState.BothPlayersPresent))
				.findFirst().orElseThrow().getKey();
	}

	public List<Point> getVisitableNeighbors(int x, int y) {
		List<Point> ret = getDirectNeighbors(x, y);
		ret.removeIf(p -> getNode(p.getX(), p.getY()).orElseThrow().getNodeType().equals(NodeType.Water));
		return ret;
	}

	public Optional<Point> getMyTreasurePoint() {
		return getGameMap().entrySet().stream()
				.filter(p -> p.getValue().getTreasurePositionState().equals(TreasurePositionState.MyTreasurePosition))
				.map(Map.Entry::getKey).findFirst();
	}

	public Optional<Point> getEnemyFortPosition() {
		return getGameMap().entrySet().stream()
				.filter(p -> p.getValue().getFortPositionState().equals(FortPositionState.EnemyFortPresent))
				.map(Map.Entry::getKey).findFirst();
	}

	public List<Point> getSeeableNeighbors(int x, int y) {
		List<Point> ret = getAllNeighbors(x, y);
		ret.removeIf(p -> getNode(p.getX(), p.getY()).orElseThrow().getNodeType().equals(NodeType.Water)
				|| getNode(p.getX(), p.getY()).orElseThrow().getNodeType().equals(NodeType.Mountain)
				|| getNode(p.getX(), p.getY()).orElseThrow().getFortPositionState()
						.equals(FortPositionState.MyFortPresent));
		return ret;
	}

	private List<Point> getAllNeighbors(int x, int y) {
		List<Point> ret = getDirectNeighbors(x, y);

		Optional<MapNode> temp;
		temp = getNode(x + 1, y + 1);
		if (temp.isPresent())
			ret.add(Point.of(x + 1, y + 1));

		temp = getNode(x + 1, y - 1);
		if (temp.isPresent())
			ret.add(Point.of(x + 1, y - 1));

		temp = getNode(x - 1, y - 1);
		if (temp.isPresent())
			ret.add(Point.of(x - 1, y - 1));

		temp = getNode(x - 1, y + 1);
		if (temp.isPresent())
			ret.add(Point.of(x - 1, y + 1));

		return ret;
	}

	private List<Point> getDirectNeighbors(int x, int y) {
		List<Point> ret = new ArrayList<Point>();
		Optional<MapNode> temp = getNode(x, y + 1);
		if (temp.isPresent())
			ret.add(Point.of(x, y + 1));

		temp = getNode(x - 1, y);
		if (temp.isPresent())
			ret.add(Point.of(x - 1, y));

		temp = getNode(x, y - 1);
		if (temp.isPresent())
			ret.add(Point.of(x, y - 1));

		temp = getNode(x + 1, y);
		if (temp.isPresent())
			ret.add(Point.of(x + 1, y));
		return ret;
	}

}

package server.controller;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerRegistration;
import server.gameData.GameId;
import server.gameData.gameMap.NodeType;
import server.gameData.gameMap.Point;
import server.gameData.gameMap.halfGameMap.FortPositionState;
import server.gameData.gameMap.halfGameMap.HalfGameMap;
import server.gameData.gameMap.halfGameMap.HalfGameMapNode;
import server.gameData.gameMap.halfGameMap.PlayerPositionState;
import server.gameData.player.Player;
import server.gameData.player.PlayerId;

public class ConverterToServer {

	public GameId toGameId(UniqueGameIdentifier gameId) {
		return new GameId(gameId.getUniqueGameID());
	}

	public Player toPlayer(PlayerRegistration playerRegistration) {
		return new Player(playerRegistration.getStudentFirstName(), playerRegistration.getStudentLastName(),
				playerRegistration.getStudentUAccount());
	}

	public PlayerId extractPlayerIdFromPlayerHalfMap(PlayerHalfMap playerHalfMap) {
		return new PlayerId(playerHalfMap.getUniquePlayerID());
	}

	public Optional<Point> extractFortPositionFromPlayerHalfMap(PlayerHalfMap playerHalfMap) {
		return playerHalfMap.getMapNodes().stream().filter(node -> node.isFortPresent())
				.map(node -> Point.of(node.getX(), node.getY())).findFirst();
	}

	public HalfGameMap toHalfGameMap(PlayerHalfMap playerHalfMap) {
		Map<Point, HalfGameMapNode> halfGameMapNodes = playerHalfMap.getMapNodes().stream()
				.collect(Collectors.toMap(node -> toPoint(node), node -> toMapNode(node)));

		Optional<Point> fortPosition = extractFortPositionFromPlayerHalfMap(playerHalfMap);
		halfGameMapNodes.get(fortPosition.orElseThrow()).setFortPositionState(FortPositionState.MyFortPresent);
		halfGameMapNodes.get(fortPosition.orElseThrow()).setPlayerPositionState(PlayerPositionState.MyPlayerPresent);

		return new HalfGameMap(halfGameMapNodes);
	}

	private Point toPoint(PlayerHalfMapNode node) {
		return Point.of(node.getX(), node.getY());
	}

	private HalfGameMapNode toMapNode(PlayerHalfMapNode node) {
		return new HalfGameMapNode(toNodeType(node.getTerrain()));
	}

	private NodeType toNodeType(ETerrain terrain) {
		if (terrain.equals(ETerrain.Grass))
			return NodeType.Grass;
		else if (terrain.equals(ETerrain.Mountain))
			return NodeType.Mountain;
		return NodeType.Water;
	}

	public PlayerId toPlayerId(UniquePlayerIdentifier playerId) {
		return new PlayerId(playerId.getUniquePlayerID());
	}

}

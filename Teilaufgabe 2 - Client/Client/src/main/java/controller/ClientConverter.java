package controller;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import gameData.map.FortPositionState;
import gameData.map.GameMap;
import gameData.map.MapNode;
import gameData.map.NodeType;
import gameData.map.PlayerPositionState;
import gameData.map.Point;
import gameData.map.TreasurePositionState;
import gameData.map.halfGameMap.HalfGameMap;
import gameData.player.Player;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.PlayerState;
import navigation.NextMove;

public class ClientConverter {

	public gameData.GameState toGameState(messagesbase.messagesfromserver.GameState state, String myPlayerId) {
		gameData.GameState ret;
		String gameStateId = state.getGameStateId();
		GameMap gameMap = toGameMap(state.getMap());

		Set<Player> player = state.getPlayers().stream().map(p -> toPlayer(p)).collect(Collectors.toSet());

		Player myPlayer = player.stream().filter(x -> x.getPlayerId().equals(myPlayerId)).findAny().orElseThrow();

		Optional<Player> enemyPlayer = player.stream().filter(x -> !x.getPlayerId().equals(myPlayerId)).findAny();
		if (enemyPlayer.isPresent())
			ret = new gameData.GameState(gameStateId, myPlayer, enemyPlayer.orElseThrow(), gameMap);
		else
			ret = new gameData.GameState(gameStateId, myPlayer, gameMap);

		return ret;
	}

	private Player toPlayer(PlayerState player) {
		return new Player(player.getUniquePlayerID(), player.getFirstName(), player.getLastName(), player.getUAccount(),
				player.hasCollectedTreasure(), toPlayerState(player.getState()));
	}

	private gameData.player.PlayerState toPlayerState(EPlayerGameState state) {
		if (state.equals(EPlayerGameState.Lost))
			return gameData.player.PlayerState.Lost;
		else if (state.equals(EPlayerGameState.Won))
			return gameData.player.PlayerState.Won;
		else if (state.equals(EPlayerGameState.MustAct))
			return gameData.player.PlayerState.MustAct;
		return gameData.player.PlayerState.MustWait;
	}

	private GameMap toGameMap(FullMap fullMap) {
		return new GameMap(fullMap.getMapNodes().stream()
				.collect(Collectors.toMap(a -> Point.of(a.getX(), a.getY()), a -> toMapNode(a))));
	}

	private MapNode toMapNode(FullMapNode node) {
		return new MapNode(toNodeType(node.getTerrain()), toPlayerPositionState(node.getPlayerPositionState()),
				toFortPositionState(node.getFortState()), toTreasurePositionState(node.getTreasureState()));
	}

	private TreasurePositionState toTreasurePositionState(ETreasureState treasureState) {
		if (treasureState.equals(ETreasureState.MyTreasureIsPresent))
			return TreasurePositionState.MyTreasurePosition;
		return TreasurePositionState.NoTreasure;
	}

	private FortPositionState toFortPositionState(EFortState fortState) {
		if (fortState.equals(EFortState.MyFortPresent))
			return FortPositionState.MyFortPresent;
		else if (fortState.equals(EFortState.EnemyFortPresent))
			return FortPositionState.EnemyFortPresent;
		return FortPositionState.NoFortPresent;
	}

	private PlayerPositionState toPlayerPositionState(EPlayerPositionState playerPositionState) {
		if (playerPositionState.equals(EPlayerPositionState.MyPlayerPosition))
			return PlayerPositionState.MyPlayerPresent;
		else if (playerPositionState.equals(EPlayerPositionState.EnemyPlayerPosition))
			return PlayerPositionState.EnemyPlayerPresent;
		else if (playerPositionState.equals(EPlayerPositionState.BothPlayerPosition))
			return PlayerPositionState.BothPlayersPresent;
		return PlayerPositionState.NoPlayerPresent;
	}

	private NodeType toNodeType(ETerrain terrain) {
		if (terrain.equals(ETerrain.Grass))
			return NodeType.Grass;
		else if (terrain.equals(ETerrain.Mountain))
			return NodeType.Mountain;
		return NodeType.Water;
	}

	public PlayerHalfMap toPlayerHalfMap(HalfGameMap gameMap, String playerId) {
		return new PlayerHalfMap(playerId, gameMap.getGameMap().entrySet().stream()
				.map(a -> toPlayerHalfMapNode(a.getKey(), a.getValue())).toList());
	}

	private PlayerHalfMapNode toPlayerHalfMapNode(Point nodePoint, MapNode node) {
		return new PlayerHalfMapNode(nodePoint.getX(), nodePoint.getY(),
				node.getFortPositionState().equals(FortPositionState.MyFortPresent), toETerrain(node.getNodeType()));
	}

	private ETerrain toETerrain(NodeType nodeType) {
		if (nodeType.equals(NodeType.Grass))
			return ETerrain.Grass;
		else if (nodeType.equals(NodeType.Mountain))
			return ETerrain.Mountain;
		return ETerrain.Water;
	}

	public PlayerMove toPlayerMove(NextMove nextMove, String playerId) {
		return PlayerMove.of(playerId, toEMove(nextMove));
	}

	private EMove toEMove(NextMove nextMove) {
		if (nextMove.equals(NextMove.Up))
			return EMove.Up;
		else if (nextMove.equals(NextMove.Down))
			return EMove.Down;
		else if (nextMove.equals(NextMove.Right))
			return EMove.Right;
		return EMove.Left;
	}

}

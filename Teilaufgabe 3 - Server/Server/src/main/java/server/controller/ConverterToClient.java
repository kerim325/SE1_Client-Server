package server.controller;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import server.gameData.Game;
import server.gameData.GameId;
import server.gameData.GameStateId;
import server.gameData.gameMap.GameMap;
import server.gameData.gameMap.MapNode;
import server.gameData.gameMap.NodeType;
import server.gameData.gameMap.Point;
import server.gameData.player.Player;
import server.gameData.player.PlayerId;
import server.gameData.player.ServerPlayerState;

public class ConverterToClient {

	public UniqueGameIdentifier toGameIdentifier(GameId gameId) {
		return new UniqueGameIdentifier(gameId.gameId());
	}

	public UniquePlayerIdentifier toUniquePlayerIdentifier(PlayerId newPlayerId) {
		return new UniquePlayerIdentifier(newPlayerId.playerId());
	}

	public GameState toGameStateRelativeToPlayer(Game serverGame, PlayerId playerId) {
		GameStateId gameStateId = serverGame.getGameState().gameStateId();
		Collection<PlayerState> players = toPlayerStateCollectionRelativeToPlayer(serverGame, playerId);
		FullMap fullMap = toFullMap(serverGame, playerId);

		GameState state = new GameState(fullMap, players, gameStateId.gameStateId());
		return state;
	}

	private FullMap toFullMap(Game serverGame, PlayerId playerId) {
		GameMap gameMap = serverGame.getGameState().gameMap();
		Optional<PlayerId> enemyPlayerId = serverGame.getOtherPlayersId(playerId);
		Optional<Point> myPlayerPosition = serverGame.getPlayerPosition(playerId, true);
		Optional<Point> myFortPosition = serverGame.getPlayersFortPosition(playerId);
		Optional<Point> myTreasurePosition = serverGame.getPlayersTreasurePositionRelativeToRevealedNodes(playerId);
		boolean enemyPresent = enemyPlayerId.isPresent();
		Optional<Point> enemyPlayerPosition = (enemyPresent
				? serverGame.getPlayerPosition(enemyPlayerId.orElseThrow(), false)
				: Optional.empty());
		Optional<Point> enemyFortPosition = (enemyPlayerId.isPresent()
				? serverGame.getFortPositionRelativeToPlayersRevealedNodes(enemyPlayerId.orElseThrow(), playerId)
				: Optional.empty());

		Function<Map.Entry<Point, MapNode>, FullMapNode> mapToFullMapNode = (mapNode) -> {
			ETerrain eTerrain = toETerrain(mapNode.getValue().getNodeType());

			EPlayerPositionState ePlayerPositionState = EPlayerPositionState.NoPlayerPresent;
			if (myPlayerPosition.isPresent() && myPlayerPosition.orElseThrow().equals(mapNode.getKey())) {
				if (enemyPresent && enemyPlayerPosition.orElseThrow().equals(mapNode.getKey()))
					ePlayerPositionState = EPlayerPositionState.BothPlayerPosition;
				else
					ePlayerPositionState = EPlayerPositionState.MyPlayerPosition;
			} else if (enemyPresent && enemyPlayerPosition.orElseThrow().equals(mapNode.getKey())) {
				ePlayerPositionState = EPlayerPositionState.EnemyPlayerPosition;
			}

			ETreasureState eTreasureState = ETreasureState.NoOrUnknownTreasureState;
			if (myTreasurePosition.isPresent() && myTreasurePosition.orElseThrow().equals(mapNode.getKey()))
				eTreasureState = ETreasureState.MyTreasureIsPresent;

			EFortState eFortState = EFortState.NoOrUnknownFortState;
			if (myFortPosition.isPresent() && myFortPosition.orElseThrow().equals(mapNode.getKey()))
				eFortState = EFortState.MyFortPresent;
			else if (enemyFortPosition.isPresent() && enemyFortPosition.orElseThrow().equals(mapNode.getKey()))
				eFortState = EFortState.EnemyFortPresent;

			int xValue = mapNode.getKey().getX();
			int yValue = mapNode.getKey().getY();

			return new FullMapNode(eTerrain, ePlayerPositionState, eTreasureState, eFortState, xValue, yValue);
		};

		Collection<FullMapNode> fullMapNodes = gameMap.getGameMap().entrySet().stream().map(mapToFullMapNode).toList();

		return new FullMap(fullMapNodes);
	}

	private ETerrain toETerrain(NodeType nodeType) {
		if (nodeType.equals(NodeType.Grass))
			return ETerrain.Grass;
		else if (nodeType.equals(NodeType.Mountain))
			return ETerrain.Mountain;
		return ETerrain.Water;
	}

	private Collection<PlayerState> toPlayerStateCollectionRelativeToPlayer(Game serverGame, PlayerId playerId) {
		final String DUMMY_PLAYER_ID = "Enemey Id";

		Function<Map.Entry<PlayerId, Player>, PlayerState> convertToEPlayerGameState = (player) -> {
			String firstName = player.getValue().getFirstName();
			String lastName = player.getValue().getLastName();
			String uAccount = player.getValue().getuAccount();
			EPlayerGameState ePlayerGameState = toEPlayerGameState(player.getValue().getServerPlayerState());
			boolean collectedTreasure = player.getValue().isCollectedTreasure();
			UniquePlayerIdentifier playerIdentifier = player.getKey().equals(playerId)
					? UniquePlayerIdentifier.of(player.getKey().playerId())
					: UniquePlayerIdentifier.of(DUMMY_PLAYER_ID);

			return new PlayerState(firstName, lastName, uAccount, ePlayerGameState, playerIdentifier,
					collectedTreasure);
		};

		return serverGame.getGameState().players().entrySet().stream().map(convertToEPlayerGameState).toList();
	}

	private EPlayerGameState toEPlayerGameState(ServerPlayerState serverPlayerState) {
		if (serverPlayerState.equals(ServerPlayerState.Lost))
			return EPlayerGameState.Lost;
		else if (serverPlayerState.equals(ServerPlayerState.Won))
			return EPlayerGameState.Won;
		else if (serverPlayerState.equals(ServerPlayerState.MustAct))
			return EPlayerGameState.MustAct;
		return EPlayerGameState.MustWait;
	}

}

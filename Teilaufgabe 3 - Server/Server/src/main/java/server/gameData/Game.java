package server.gameData;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.exceptions.MaxNumberOfPlayersReachedException;
import server.exceptions.PlayerNotRegisteredException;
import server.gameData.gameMap.GameMap;
import server.gameData.gameMap.MapNode;
import server.gameData.gameMap.Point;
import server.gameData.gameMap.halfGameMap.HalfGameMap;
import server.gameData.gameMap.halfGameMap.HalfMapCombinationFormation;
import server.gameData.player.Player;
import server.gameData.player.PlayerId;
import server.gameData.player.ServerPlayerState;

public class Game {
	private static Logger logger = LoggerFactory.getLogger(Game.class);
	private Instant gameCreationTime = Instant.now();
	private ServerGameState gameState;
	private final static int NUMBER_OF_PLAYERS = 2;
	private final static int INITIAL_NUMBER_OF_ROUNDS = -2; // First 2 rounds are for sending halfmaps. At round 0,
															// first move is made.
	private final static int MAX_NUMBER_OF_ROUNDS = 320;
	private final static int ROUNDS_LIMIT_FOR_RANDOM_POSITION_OF_ENEMY = 16;
	private final static Duration maxGameDuration = Duration.ofMinutes(10);

	public Game() {
		this.gameState = ServerGameState.of(INITIAL_NUMBER_OF_ROUNDS);
	}

	public boolean gameExpired() {
		Instant currentTime = Instant.now();
		Instant expirationTime = gameCreationTime.plus(maxGameDuration);
		return currentTime.isAfter(expirationTime);
	}

	public PlayerId registerPlayer(Player newPlayer) throws MaxNumberOfPlayersReachedException {
		if (allPlayersAreRegistered())
			throw new MaxNumberOfPlayersReachedException("MaxNumberOfPlayersReachedError",
					"The game has alredy reached the max number of players.");
		PlayerId newPlayerId = createUniquePlayerId();
		gameState.players().put(newPlayerId, newPlayer);
		gameStateChangeHandler();
		return newPlayerId;
	}

	private PlayerId createUniquePlayerId() {
		PlayerId playerId = PlayerId.createRandomPlayerId();
		while (playerIsRegistered(playerId))
			playerId = PlayerId.createRandomPlayerId();
		return playerId;
	}

	public Player getPlayer(PlayerId playerId) {
		if (!playerIsRegistered(playerId))
			throw new PlayerNotRegisteredException("PlayerNotRegisteredError",
					"No player with given playerId is registered.");
		return gameState.players().get(playerId);
	}

	public boolean playerIsRegistered(PlayerId playerId) {
		return gameState.players().containsKey(playerId);
	}

	public boolean allPlayersAreRegistered() {
		return gameState.players().size() == NUMBER_OF_PLAYERS;
	}

	public boolean playerHasSentHalfMap(PlayerId playerId) {
		return getPlayer(playerId).isSentHalfMap();
	}

	public void addHalfGameMap(PlayerId sendingPlayerId, HalfGameMap halfGameMap) {
		Player sendingPlayer = getPlayer(sendingPlayerId);

		sendingPlayer.setSentHalfMap(true);

		GameMap newHalfGameMapAsGameMap = halfGameMap.halfGameMapToGameMap();
		GameMap currentGameMap = gameState.gameMap();

		boolean currentGameMapIsShifted = false; // if the current maps coordinates shift, this will be set to true.
		int horizontalOffset = 0; // the offset on the x axis
		int verticalOffset = 0; // the offset on the y axis

		if (currentGameMap.getGameMap().isEmpty()) {
			currentGameMap.setGameMap(newHalfGameMapAsGameMap.getGameMap());
		} else {
			HalfMapCombinationFormation formation = HalfMapCombinationFormation.getRandomFormation();
			logger.debug("HalfMap formation: " + formation);
			switch (formation) {
			case HorizontalRight:
				horizontalOffset = newHalfGameMapAsGameMap.getMaxValX() + 1;
				currentGameMap.setGameMap(
						combineGameMap(currentGameMap, newHalfGameMapAsGameMap, horizontalOffset, verticalOffset));
				break;
			case HorizontalLeft:
				horizontalOffset = currentGameMap.getMaxValX() + 1;
				currentGameMapIsShifted = true;
				currentGameMap.setGameMap(
						combineGameMap(newHalfGameMapAsGameMap, currentGameMap, horizontalOffset, verticalOffset));
				break;
			case VerticalAbove:
				verticalOffset = currentGameMap.getMaxValY() + 1;
				currentGameMapIsShifted = true;
				currentGameMap.setGameMap(
						combineGameMap(newHalfGameMapAsGameMap, currentGameMap, horizontalOffset, verticalOffset));
				break;
			case VerticalBelow:
				verticalOffset = newHalfGameMapAsGameMap.getMaxValY() + 1;
				currentGameMap.setGameMap(
						combineGameMap(currentGameMap, newHalfGameMapAsGameMap, horizontalOffset, verticalOffset));
				break;
			default:
				throw new IllegalArgumentException("Unexpected half map combination formation: " + formation);
			}
		}

		setPlayersPositionDataAfterHalfGameMapCombination(sendingPlayerId, halfGameMap, horizontalOffset,
				verticalOffset, currentGameMapIsShifted);

		gameStateChangeHandler();
	}

	private void setPlayersPositionDataAfterHalfGameMapCombination(PlayerId sendingPlayerId, HalfGameMap halfGameMap,
			int horizontalOffset, int verticalOffset, boolean currentGameMapIsShifted) {
		Player sendingPlayer = getPlayer(sendingPlayerId);

		Point sendingPlayerFortPosition = halfGameMap.getFortPosition().orElseThrow();
		int shiftedFortX = sendingPlayerFortPosition.getX() + (currentGameMapIsShifted ? 0 : horizontalOffset);
		int shiftedFortY = sendingPlayerFortPosition.getY() + (currentGameMapIsShifted ? 0 : verticalOffset);
		sendingPlayerFortPosition = Point.of(shiftedFortX, shiftedFortY);
		sendingPlayer.setFortPosition(sendingPlayerFortPosition);
		sendingPlayer.setPlayerPosition(sendingPlayerFortPosition); // player starts at fort position

		Point sendingPlayerTreasurePosition = halfGameMap.placeTreasureOnRandomGrassNode();
		int shiftedTreasureX = sendingPlayerTreasurePosition.getX() + (currentGameMapIsShifted ? 0 : horizontalOffset);
		int shiftedTreasureY = sendingPlayerTreasurePosition.getY() + (currentGameMapIsShifted ? 0 : verticalOffset);
		sendingPlayerTreasurePosition = Point.of(shiftedTreasureX, shiftedTreasureY);
		sendingPlayer.setTreasurePosition(sendingPlayerTreasurePosition);

		if (!allPlayersHaveSentHalfGameMap())
			return;

		PlayerId otherPlayerId = getOtherPlayersId(sendingPlayerId).orElseThrow();
		Player otherPlayer = getPlayer(otherPlayerId);

		Point otherPlayerFortPosition = otherPlayer.getFortPosition().orElseThrow();
		int shiftedFortXotherPlayer = otherPlayerFortPosition.getX() + (currentGameMapIsShifted ? horizontalOffset : 0);
		int shiftedFortYotherPlayer = otherPlayerFortPosition.getY() + (currentGameMapIsShifted ? verticalOffset : 0);
		otherPlayerFortPosition = Point.of(shiftedFortXotherPlayer, shiftedFortYotherPlayer);
		otherPlayer.setFortPosition(otherPlayerFortPosition);
		otherPlayer.setPlayerPosition(otherPlayerFortPosition); // player starts at fort position

		Point otherPlayerTreasurePosition = otherPlayer.getTreasurePosition().orElseThrow();
		int shiftedTreasureXotherPlayer = otherPlayerTreasurePosition.getX()
				+ (currentGameMapIsShifted ? horizontalOffset : 0);
		int shiftedTreasureYotherPlayer = otherPlayerTreasurePosition.getY()
				+ (currentGameMapIsShifted ? verticalOffset : 0);
		otherPlayerTreasurePosition = Point.of(shiftedTreasureXotherPlayer, shiftedTreasureYotherPlayer);
		otherPlayer.setTreasurePosition(otherPlayerTreasurePosition);

	}

	private boolean allPlayersHaveSentHalfGameMap() {
		return gameState.players().entrySet().stream().filter(player -> player.getValue().isSentHalfMap())
				.count() == NUMBER_OF_PLAYERS;
	}

	private Map<Point, MapNode> combineGameMap(GameMap gameMap1, GameMap gameMap2, int horizontalOffset,
			int verticalOffset) {
		Map<Point, MapNode> combinedGameMap = new HashMap<>();
		combinedGameMap.putAll(gameMap1.getGameMap());

		for (var entry : gameMap2.getGameMap().entrySet()) {
			Point newPointBelow = Point.of(entry.getKey().getX() + horizontalOffset,
					entry.getKey().getY() + verticalOffset);
			combinedGameMap.put(newPointBelow, entry.getValue());
		}

		return combinedGameMap;
	}

	private void gameStateChangeHandler() {
		switchTurns();

		if (gameState.rounds() + 1 == 0)
			shufflePlayerTurn();

		this.gameState = ServerGameState.of(gameState.players(), gameState.rounds() + 1, gameState.gameMap(),
				gameState.revealedMapNodes());

		if (gameState.rounds() >= MAX_NUMBER_OF_ROUNDS)
			playerLose(getPlayerIdOfCurrentTurn().orElseThrow());

	}

	private void shufflePlayerTurn() {
		PlayerId playerId = gameState.players().entrySet().stream().map(player -> player.getKey()).findFirst()
				.orElseThrow();
		PlayerId otherPlayerId = getOtherPlayersId(playerId).orElseThrow();
		getPlayer(otherPlayerId).assignOppositeTurn(getPlayer(otherPlayerId));

	}

	private void switchTurns() {
		Optional<PlayerId> oldPlayer = getPlayerIdOfCurrentTurn();
		if (oldPlayer.isPresent()) {
			getPlayer(oldPlayer.orElseThrow()).switchTurn();

			Optional<PlayerId> otherPlayer = getOtherPlayersId(oldPlayer.orElseThrow());
			if (otherPlayer.isPresent())
				getPlayer(otherPlayer.orElseThrow()).switchTurn();
		}
	}

	public Optional<PlayerId> getPlayerIdOfCurrentTurn() {
		return gameState.players().entrySet().stream().filter(player -> player.getValue().isTurn())
				.map(player -> player.getKey()).findFirst();
	}

	public Optional<PlayerId> getOtherPlayersId(PlayerId playerId) {
		return gameState.players().entrySet().stream().filter(player -> !player.getKey().equals(playerId))
				.map(player -> player.getKey()).findFirst();
	}

	public Optional<Point> getPlayersTreasurePositionRelativeToRevealedNodes(PlayerId playerId) {
		Optional<Point> treasurePosition = getPlayer(playerId).getTreasurePosition();
		if (treasurePosition.isPresent() && isMapNodeRevealedToPlayer(playerId, treasurePosition.orElseThrow()))
			return treasurePosition;
		return Optional.empty();
	}

	public Optional<Point> getPlayersFortPosition(PlayerId playerId) {
		return getPlayer(playerId).getFortPosition();
	}

	private boolean isMapNodeRevealedToPlayer(PlayerId playerId, Point mapNode) {
		if (!gameState.revealedMapNodes().containsKey(playerId))
			return false;
		Set<Point> revealedMapNodesOfPlayer = gameState.revealedMapNodes().get(playerId);
		return revealedMapNodesOfPlayer.contains(mapNode);
	}

	public Optional<Point> getFortPositionRelativeToPlayersRevealedNodes(PlayerId fortOwnerPlayerId,
			PlayerId perspectivePlayerId) {
		Optional<Point> fortPosition = getPlayer(perspectivePlayerId).getFortPosition();
		if (fortPosition.isPresent() && isMapNodeRevealedToPlayer(perspectivePlayerId, fortPosition.orElseThrow()))
			return fortPosition;
		return Optional.empty();
	}

	public Optional<Point> getPlayerPosition(PlayerId playerId, boolean truePosition) {
		if (!truePosition && gameState.rounds() <= ROUNDS_LIMIT_FOR_RANDOM_POSITION_OF_ENEMY)
			return gameState.gameMap().getRandomMapPosition();
		else
			return getPlayer(playerId).getPlayerPosition();
	}

	public ServerGameState getGameState() {
		return gameState;
	}

	public void playerLose(PlayerId playerId) {
		logger.info("Player (Id:" + playerId.playerId() + ") lost due to rule violation.");
		getPlayer(playerId).setServerPlayerState(ServerPlayerState.Lost);

		Optional<PlayerId> otherPlayerId = getOtherPlayersId(playerId);
		if (otherPlayerId.isPresent())
			getPlayer(otherPlayerId.orElseThrow()).setServerPlayerState(ServerPlayerState.Won);

	}

}

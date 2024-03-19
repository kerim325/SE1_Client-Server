package server.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.exceptions.GameNotFoundException;
import server.gameData.Game;
import server.gameData.GameId;
import server.gameData.gameMap.halfGameMap.HalfGameMap;
import server.gameData.player.Player;
import server.gameData.player.PlayerId;

public class ServerManager {
	private static Logger logger = LoggerFactory.getLogger(ServerManager.class);
	private static final int MAX_SIZE = 100;
	private Map<GameId, Game> games = new LinkedHashMap<GameId, Game>() {
		@Override
		protected boolean removeEldestEntry(Map.Entry<GameId, Game> eldest) {
			boolean removeEldest = size() >= MAX_SIZE;
			if (removeEldest)
				logger.info("Maximum number of games reached. Removed game: " + eldest.getKey().gameId());
			return removeEldest;
		}
	};

	public GameId createNewGame() {
		GameId gameId = createUniqueGameId();
		Game game = new Game();
		games.put(gameId, game);
		return gameId;
	}

	private GameId createUniqueGameId() {
		GameId uniqueGameId = GameId.createRandomGameId();
		while (games.containsKey(uniqueGameId))
			uniqueGameId = GameId.createRandomGameId();
		return uniqueGameId;
	}

	public PlayerId registerPlayer(GameId serverGameId, Player newPlayer) {
		Game game = getGame(serverGameId);
		PlayerId newPlayerId = game.registerPlayer(newPlayer);
		return newPlayerId;
	}

	public void addHalfGameMapToGame(GameId serverGameId, PlayerId playerId, HalfGameMap halfGameMap) {
		Game game = getGame(serverGameId);
		game.addHalfGameMap(playerId, halfGameMap);
	}

	public Game getGame(GameId gameId) throws GameNotFoundException {
		Optional<Game> game = Optional.ofNullable(games.get(gameId));
		if (game.isEmpty()) {
			logger.error("Could not find game with given gameId " + gameId.gameId() + ".");
			throw new GameNotFoundException("GameNotFoundError", "Could not find game with given gameId.");
		}
		return game.orElseThrow();
	}

	public void deleteExpiredGames() {
		Set<Map.Entry<GameId, Game>> gamesSet = games.entrySet();
		for (var gameEntry : gamesSet) {
			Game game = gameEntry.getValue();
			if (game.gameExpired()) {
				games.remove(gameEntry.getKey());
				logger.info("Game expired: " + gameEntry.getKey());
			}
		}
	}

}

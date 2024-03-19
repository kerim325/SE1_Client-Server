package server.gameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import server.gameData.gameMap.GameMap;
import server.gameData.gameMap.Point;
import server.gameData.player.Player;
import server.gameData.player.PlayerId;

public record ServerGameState(GameStateId gameStateId, int rounds, Map<PlayerId, Player> players, GameMap gameMap,
		Map<PlayerId, Set<Point>> revealedMapNodes) {

	static public ServerGameState of(Map<PlayerId, Player> players, int rounds, GameMap gameMap,
			Map<PlayerId, Set<Point>> revealedMapNodes) {
		return new ServerGameState(GameStateId.createRandomGameStateId(), rounds, players, gameMap, revealedMapNodes);
	}

	static public ServerGameState of(int rounds) {
		return new ServerGameState(GameStateId.createRandomGameStateId(), rounds, new HashMap<>(), new GameMap(),
				new HashMap<>());
	}
}

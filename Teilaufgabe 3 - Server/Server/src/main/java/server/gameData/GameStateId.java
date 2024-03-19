package server.gameData;

import java.util.UUID;

public record GameStateId(String gameStateId) {

	public static GameStateId createRandomGameStateId() {
		return new GameStateId(UUID.randomUUID().toString());
	}

}

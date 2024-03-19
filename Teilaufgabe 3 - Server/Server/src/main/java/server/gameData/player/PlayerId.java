package server.gameData.player;

import java.util.UUID;

public record PlayerId(String playerId) {

	public static PlayerId createRandomPlayerId() {
		return new PlayerId(UUID.randomUUID().toString());
	}

}

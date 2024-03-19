package server.gameData.player;

import java.util.Random;

public enum ServerPlayerState {
	MustWait, MustAct, Won, Lost;

	public static ServerPlayerState randomTurn() {
		Random randomBooleanGenerator = new Random();
		if (randomBooleanGenerator.nextBoolean())
			return ServerPlayerState.MustWait;
		return ServerPlayerState.MustAct;
	}
}

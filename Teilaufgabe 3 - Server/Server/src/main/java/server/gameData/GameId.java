package server.gameData;

import java.util.Random;

public record GameId(String gameId) {

	private final static String CHARACTERS_FOR_GAMEID = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static GameId createRandomGameId() {
		final int GAME_ID_LENGTH = 5;

		StringBuilder gameIdBuilder = new StringBuilder();
		Random randomNumberGenerator = new Random();

		for (int i = 0; i < GAME_ID_LENGTH; i++) {
			int randomIndex = randomNumberGenerator.nextInt(CHARACTERS_FOR_GAMEID.length());
			char randomChar = CHARACTERS_FOR_GAMEID.charAt(randomIndex);
			gameIdBuilder.append(randomChar);
		}

		return new GameId(gameIdBuilder.toString());
	}

}

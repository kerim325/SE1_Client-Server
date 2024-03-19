package server.gameData.gameMap.halfGameMap;

import java.util.Random;

public enum HalfMapCombinationFormation {
	HorizontalLeft, HorizontalRight, VerticalAbove, VerticalBelow;

	public static HalfMapCombinationFormation getRandomFormation() {
		Random randomIntGenerator = new Random();
		int randomNumber = randomIntGenerator.nextInt(values().length);
		return values()[randomNumber];
	}

}

package server.ruleChecker;

import java.util.Comparator;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.PlayerHalfMapRuleViolationException;

public class PlayerHalfMapHasCorrectDimensions extends AbtractPlayerHalfMapRule {

	private final static int MAX_X = 9;
	private final static int MAX_Y = 4;

	@Override
	PlayerHalfMapRuleViolationException getException() {
		return new PlayerHalfMapRuleViolationException("PlayerHalfMapHasWrongDimensionsError",
				"Halfmap has wrong dimension. It should be 10x5.");
	}

	@Override
	boolean validate(PlayerHalfMap halfMap) {
		int maxX = halfMap.getMapNodes().stream().map(node -> node.getX()).max(Comparator.comparingInt(x -> x))
				.orElseThrow();
		int maxY = halfMap.getMapNodes().stream().map(node -> node.getY()).max(Comparator.comparingInt(y -> y))
				.orElseThrow();
		return maxX == MAX_X && maxY == MAX_Y;
	}

}

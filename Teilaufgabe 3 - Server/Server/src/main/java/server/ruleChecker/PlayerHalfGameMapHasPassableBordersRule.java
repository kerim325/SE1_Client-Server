package server.ruleChecker;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.PlayerHalfMapRuleViolationException;

public class PlayerHalfGameMapHasPassableBordersRule extends AbtractPlayerHalfMapRule {

	private final int MAX_VAL_X = 9;
	private final int MAX_VAL_Y = 4;
	private final int MAX_WATER_NODE_COUNT_SHORT_BORDER = 2;
	private final int MAX_WATER_NODE_COUNT_LONG_BORDER = 4;

	@Override
	PlayerHalfMapRuleViolationException getException() {
		return new PlayerHalfMapRuleViolationException("PlayerHalfGameMapHasNotPassableBordersError",
				"Borders of halfmap has to many water node.");
	}

	@Override
	boolean validate(PlayerHalfMap halfMap) {
		return !checkMapBordersForWaterLimit(halfMap);
	}

	private boolean checkMapBordersForWaterLimit(PlayerHalfMap halfMap) {
		if (halfMap.getMapNodes().stream().filter(node -> node.getX() == 0)
				.filter(node -> node.getTerrain().equals(ETerrain.Water)).count() > MAX_WATER_NODE_COUNT_SHORT_BORDER)
			return true;
		if (halfMap.getMapNodes().stream().filter(node -> node.getX() == MAX_VAL_X)
				.filter(node -> node.getTerrain().equals(ETerrain.Water)).count() > MAX_WATER_NODE_COUNT_SHORT_BORDER)
			return true;
		if (halfMap.getMapNodes().stream().filter(node -> node.getY() == 0)
				.filter(node -> node.getTerrain().equals(ETerrain.Water)).count() > MAX_WATER_NODE_COUNT_LONG_BORDER)
			return true;
		if (halfMap.getMapNodes().stream().filter(node -> node.getY() == MAX_VAL_Y)
				.filter(node -> node.getTerrain().equals(ETerrain.Water)).count() > MAX_WATER_NODE_COUNT_LONG_BORDER)
			return true;
		return false;
	}

}

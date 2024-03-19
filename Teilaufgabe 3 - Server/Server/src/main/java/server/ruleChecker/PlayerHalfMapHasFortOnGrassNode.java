package server.ruleChecker;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.PlayerHalfMapRuleViolationException;

public class PlayerHalfMapHasFortOnGrassNode extends AbtractPlayerHalfMapRule {

	@Override
	PlayerHalfMapRuleViolationException getException() {
		return new PlayerHalfMapRuleViolationException("Fort not on Grass", "Fort is not on a Grass node.");
	}

	@Override
	boolean validate(PlayerHalfMap halfMap) {
		PlayerHalfMapNode fortNode = halfMap.getMapNodes().stream().filter(node -> node.isFortPresent()).findFirst()
				.orElseThrow();
		return fortNode.getTerrain().equals(ETerrain.Grass);
	}
}

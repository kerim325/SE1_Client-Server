package server.ruleChecker;

import java.util.Collection;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.PlayerHalfMapRuleViolationException;

public class PlayerHalfMapHasEnoughWaterMountainGrassNodesRule extends AbtractPlayerHalfMapRule {

	private final static int WATER_MIN_NUMBER = 7;
	private final static int GRASS_MIN_NUMBER = 24;
	private final static int MOUNTAIN_MIN_NUMBER = 5;

	@Override
	public boolean validate(PlayerHalfMap halfMap) {
		Collection<PlayerHalfMapNode> nodes = halfMap.getMapNodes();
		long numberOfWaterNodes = nodes.stream().filter(node -> node.getTerrain().equals(ETerrain.Water)).count();
		long numberOfGrassNodes = nodes.stream().filter(node -> node.getTerrain().equals(ETerrain.Grass)).count();
		long numberOfMountainNodes = nodes.stream().filter(node -> node.getTerrain().equals(ETerrain.Mountain)).count();

		return numberOfWaterNodes >= WATER_MIN_NUMBER && numberOfGrassNodes >= GRASS_MIN_NUMBER
				&& numberOfMountainNodes >= MOUNTAIN_MIN_NUMBER;
	}

	@Override
	public PlayerHalfMapRuleViolationException getException() {
		return new PlayerHalfMapRuleViolationException("PlayerHalfMapHasEnoughWaterMountainGrassNodesError",
				"The number of water, mountain and/or grass nodes is less then the appropriate number.");
	}

}

package server.ruleChecker;

import java.util.ArrayList;
import java.util.List;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.controller.ServerManager;
import server.exceptions.PlayerHalfMapRuleViolationException;
import server.exceptions.PlayerRuleViolationException;
import server.gameData.Game;
import server.gameData.GameId;
import server.gameData.player.Player;
import server.gameData.player.PlayerId;

public class RuleChecker {
	private ServerManager serverManager;

	public RuleChecker(ServerManager serverManager) {
		this.serverManager = serverManager;
	}

	public void check(PlayerHalfMap halfMap, GameId gameId, PlayerId playerId)
			throws PlayerHalfMapRuleViolationException {
		List<AbtractPlayerHalfMapRule> rules = new ArrayList<AbtractPlayerHalfMapRule>();
		rules.add(new PlayerHalfMapHas50NodesRule());
		rules.add(new PlayerHalfMapHasEnoughWaterMountainGrassNodesRule());
		rules.add(new PlayerHalfMapHasOneFort());
		rules.add(new PlayerHalfMapHasFortOnGrassNode());
		rules.add(new PlayerHalfMapHasNoIslands());
		rules.add(new PlayerHalfGameMapHasPassableBordersRule());
		rules.add(new PlayerHalfMapHasCorrectDimensions());

		for (var rule : rules) {
			if (!rule.validate(halfMap)) {
				rule.ruleViolationHandler(serverManager.getGame(gameId), playerId);
				throw rule.getException();
			}
		}
	}

	public void check(Player player, GameId gameId, PlayerId playerId) throws PlayerRuleViolationException {
		List<AbstractPlayerRule> rules = new ArrayList<AbstractPlayerRule>();
		rules.add(new PlayerHasTurnRule());
		rules.add(new PlayerHasNotSentHalfMapBeforeRule());

		for (var rule : rules) {
			if (!rule.validate(player)) {
				rule.ruleViolationHandler(serverManager.getGame(gameId), playerId);
				throw rule.getException();
			}
		}

	}

	public void check(Game game, GameId gameId, PlayerId playerId) throws PlayerRuleViolationException {
		List<AbstractGameRule> rules = new ArrayList<AbstractGameRule>();
		rules.add(new GameHasNotReachedMaxRoundRule());

		for (var rule : rules) {
			if (!rule.validate(game)) {
				rule.ruleViolationHandler(serverManager.getGame(gameId), playerId);
				throw rule.getException();
			}
		}

	}

}

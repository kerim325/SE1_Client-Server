package ui;

import java.util.Optional;

import gameData.GameState;
import gameData.map.FortPositionState;
import gameData.map.MapNode;
import gameData.map.NodeType;
import gameData.map.PlayerPositionState;
import gameData.map.TreasurePositionState;

public class GameMapStringBuilder {

	private final int CELL_SIZE = 5;
	private final String COLOR_CODE_RESET_TEXT_FORMAT = "\033[0m";
	private final String COLOR_CODE_GREEN = "\033[42m";
	private final String COLOR_CODE_RED = "\033[41m";
	private final String COLOR_CODE_GREY = "\033[37m";
	private final String COLOR_CODE_BLUE = "\033[44m";
	private final String COLOR_CODE_YELLOW = "\033[33m";
	private final String COLOR_CODE_BLACK = "\033[40m";

	public String gameMapToString(GameState gameState) {
		StringBuilder sb = new StringBuilder();

		sb.append("+").append("-".repeat((gameState.getGameMap().getMaxValX() + 1) * CELL_SIZE)).append("+\n");

		for (int y = 0; y <= gameState.getGameMap().getMaxValY(); y++) {
			sb.append("|");
			for (int x = 0; x <= gameState.getGameMap().getMaxValX(); x++) {
				Optional<MapNode> node = gameState.getGameMap().getNode(x, y);
				if (node.isPresent()) {
					String nodeString = getStringToDisplayForNode(node.orElseThrow());
					String cellCenteredString = String.format("%-" + CELL_SIZE + "s",
							String.format("%" + (CELL_SIZE + nodeString.length()) / 2 + "s", nodeString));
					sb.append(getColorOfStringToDisplay(node.orElseThrow())).append(cellCenteredString)
							.append(COLOR_CODE_RESET_TEXT_FORMAT);
				} else
					sb.append(" ".repeat(CELL_SIZE));
			}
			sb.append("|\n");
		}

		sb.append("+").append("-".repeat((gameState.getGameMap().getMaxValX() + 1) * CELL_SIZE)).append("+\n");
		return sb.toString();
	}

	private String getColorOfStringToDisplay(MapNode node) {
		if (node.getFortPositionState().equals(FortPositionState.MyFortPresent))
			return COLOR_CODE_BLACK;
		else if (node.getFortPositionState().equals(FortPositionState.EnemyFortPresent))
			return COLOR_CODE_RED;
		else if (node.getTreasurePositionState().equals(TreasurePositionState.MyTreasurePosition))
			return COLOR_CODE_YELLOW;

		if (node.getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
			return COLOR_CODE_RED;
		if (node.getPlayerPositionState().equals(PlayerPositionState.EnemyPlayerPresent))
			return COLOR_CODE_RED;

		if (node.getNodeType().equals(NodeType.Mountain))
			return COLOR_CODE_GREY;
		if (node.getNodeType().equals(NodeType.Water))
			return COLOR_CODE_BLUE;

		return COLOR_CODE_GREEN;
	}

	private String getStringToDisplayForNode(MapNode node) {
		String ret = node.getNodeType().getSymbol();

		if (node.getFortPositionState().equals(FortPositionState.MyFortPresent))
			ret = "#";
		else if (node.getFortPositionState().equals(FortPositionState.EnemyFortPresent))
			ret = "X";
		else if (node.getTreasurePositionState().equals(TreasurePositionState.MyTreasurePosition))
			ret = "$";

		if (node.getPlayerPositionState().equals(PlayerPositionState.NoPlayerPresent))
			return ret;
		if (node.getPlayerPositionState().equals(PlayerPositionState.MyPlayerPresent))
			return ">" + ret + "<";
		if (node.getPlayerPositionState().equals(PlayerPositionState.EnemyPlayerPresent))
			return "<" + ret + ">";

		return "{" + ret + "}";
	}

}

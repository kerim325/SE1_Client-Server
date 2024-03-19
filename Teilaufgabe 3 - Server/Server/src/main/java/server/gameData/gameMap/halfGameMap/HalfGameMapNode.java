package server.gameData.gameMap.halfGameMap;

import server.gameData.gameMap.AbstractMapNode;
import server.gameData.gameMap.NodeType;

public class HalfGameMapNode extends AbstractMapNode {
	private PlayerPositionState playerPositionState;
	private FortPositionState fortPositionState;
	private TreasurePositionState treasurePositionState;

	public HalfGameMapNode(NodeType nodeType) {
		super(nodeType);
		this.playerPositionState = PlayerPositionState.NoPlayerPresent;
		this.fortPositionState = FortPositionState.NoFortPresent;
		this.treasurePositionState = TreasurePositionState.NoTreasure;
	}

	public HalfGameMapNode(NodeType nodeType, FortPositionState fortPositionState,
			PlayerPositionState playerPositionState) {
		super(nodeType);
		this.playerPositionState = playerPositionState;
		this.fortPositionState = fortPositionState;
		this.treasurePositionState = TreasurePositionState.NoTreasure;
	}

	public PlayerPositionState getPlayerPositionState() {
		return playerPositionState;
	}

	public void setPlayerPositionState(PlayerPositionState playerPositionState) {
		this.playerPositionState = playerPositionState;
	}

	public FortPositionState getFortPositionState() {
		return fortPositionState;
	}

	public void setFortPositionState(FortPositionState fortPositionState) {
		this.fortPositionState = fortPositionState;
	}

	public TreasurePositionState getTreasurePositionState() {
		return treasurePositionState;
	}

	public void setTreasurePositionState(TreasurePositionState treasurePositionState) {
		this.treasurePositionState = treasurePositionState;
	}

}

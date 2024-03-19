package gameData.map;

public class MapNode {
	private NodeType nodeType;
	private PlayerPositionState playerPositionState;
	private FortPositionState fortPositionState;
	private TreasurePositionState treasurePositionState;

	public MapNode(NodeType nodeType, PlayerPositionState playerPositionState, FortPositionState fortPositionState,
			TreasurePositionState treasurePositionState) {
		this.nodeType = nodeType;
		this.playerPositionState = playerPositionState;
		this.fortPositionState = fortPositionState;
		this.treasurePositionState = treasurePositionState;
	}

	public MapNode(NodeType nodeType, PlayerPositionState playerPositionState, FortPositionState fortPositionState) {
		this.nodeType = nodeType;
		this.playerPositionState = playerPositionState;
		this.fortPositionState = fortPositionState;
		this.treasurePositionState = TreasurePositionState.NoTreasure;
	}

	public MapNode(NodeType nodeType, FortPositionState fortPositionState) {
		this.nodeType = nodeType;
		this.playerPositionState = PlayerPositionState.NoPlayerPresent;
		this.fortPositionState = fortPositionState;
		this.treasurePositionState = TreasurePositionState.NoTreasure;
	}

	public MapNode(NodeType nodeType, TreasurePositionState treasurePositionState) {
		this.nodeType = nodeType;
		this.playerPositionState = PlayerPositionState.NoPlayerPresent;
		this.fortPositionState = FortPositionState.NoFortPresent;
		this.treasurePositionState = treasurePositionState;
	}

	public MapNode(NodeType nodeType, PlayerPositionState playerPositionState) {
		this.nodeType = nodeType;
		this.playerPositionState = playerPositionState;
		this.fortPositionState = FortPositionState.NoFortPresent;
		this.treasurePositionState = TreasurePositionState.NoTreasure;
	}

	public MapNode(NodeType nodeType) {
		this.nodeType = nodeType;
		this.playerPositionState = PlayerPositionState.NoPlayerPresent;
		this.fortPositionState = FortPositionState.NoFortPresent;
		this.treasurePositionState = TreasurePositionState.NoTreasure;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public FortPositionState getFortPositionState() {
		return fortPositionState;
	}

	public PlayerPositionState getPlayerPositionState() {
		return playerPositionState;
	}

	public TreasurePositionState getTreasurePositionState() {
		return treasurePositionState;
	}

}

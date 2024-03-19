package server.gameData.gameMap;

public abstract class AbstractMapNode {
	private NodeType nodeType;

	public AbstractMapNode(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public NodeType getNodeType() {
		return nodeType;
	}
}

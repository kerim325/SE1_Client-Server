package gameData.map;

public enum NodeType {
	Water("~", Integer.MAX_VALUE), Grass(",", 1), Mountain("\u25B2", 2);

	private final String label;
	private final Integer cost;

	private NodeType(String label, Integer cost) {
		this.label = label;
		this.cost = cost;
	}

	public String getSymbol() {
		return label;
	}

	public Integer getCost() {
		return cost;
	}

}

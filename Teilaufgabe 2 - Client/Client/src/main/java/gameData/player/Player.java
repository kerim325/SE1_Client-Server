package gameData.player;

public class Player {

	private String playerId;
	private String firstName;
	private String lastName;
	private String uAccount;
	private boolean hasTreasure;

	private PlayerState playerState;

	public boolean mustAct() {
		return playerState.equals(PlayerState.MustAct);
	}

	public String getPlayerId() {
		return playerId;
	}

	public boolean hasWon() {
		return playerState.equals(PlayerState.Won);
	}

	public Player(String playerId, String firstName, String lastName, String uAccount, boolean hasTreasure,
			PlayerState playerState) {
		this.playerId = playerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.uAccount = uAccount;
		this.playerState = playerState;
		this.hasTreasure = hasTreasure;
	}

	public boolean hasLost() {
		return playerState.equals(PlayerState.Lost);
	}

	public boolean hasCollectedTreasure() {
		return hasTreasure;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getuAccount() {
		return uAccount;
	}

}

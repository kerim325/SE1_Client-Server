package server.gameData.player;

import java.util.Optional;

import server.gameData.gameMap.Point;

public class Player {
	private String firstName;
	private String lastName;
	private String uAccount;
	private boolean collectedTreasure;
	private boolean sentHalfMap;
	private Point playerPosition;
	private Point treasurePosition;
	private Point fortPosition;
	private ServerPlayerState serverPlayerState;

	public Player(String firstName, String lastName, String uAccount) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.uAccount = uAccount;
		this.collectedTreasure = false;
		this.sentHalfMap = false;
		this.serverPlayerState = ServerPlayerState.MustWait;
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

	public Optional<Point> getPlayerPosition() {
		return Optional.ofNullable(playerPosition);
	}

	public void setPlayerPosition(Point playerPosition) {
		this.playerPosition = playerPosition;
	}

	public Optional<Point> getTreasurePosition() {
		return Optional.ofNullable(treasurePosition);
	}

	public void setTreasurePosition(Point treasurePosition) {
		this.treasurePosition = treasurePosition;
	}

	public Optional<Point> getFortPosition() {
		return Optional.ofNullable(fortPosition);
	}

	public void setFortPosition(Point fortPosition) {
		this.fortPosition = fortPosition;
	}

	public boolean isSentHalfMap() {
		return sentHalfMap;
	}

	public void setSentHalfMap(boolean sentHalfMap) {
		this.sentHalfMap = sentHalfMap;
	}

	public boolean isCollectedTreasure() {
		return collectedTreasure;
	}

	public boolean isTurn() {
		return serverPlayerState.equals(ServerPlayerState.MustAct);
	}

	public boolean hasWon() {
		return serverPlayerState.equals(ServerPlayerState.Won);
	}

	public void switchTurn() {
		serverPlayerState = isTurn() ? ServerPlayerState.MustWait : ServerPlayerState.MustAct;
	}

	public ServerPlayerState getServerPlayerState() {
		return serverPlayerState;
	}

	public void setServerPlayerState(ServerPlayerState serverPlayerState) {
		this.serverPlayerState = serverPlayerState;
	}

	public void assignRandomTurn() {
		setServerPlayerState(ServerPlayerState.randomTurn());
	}

	public void assignOppositeTurn(Player otherPlayer) {
		if (otherPlayer.isTurn())
			setServerPlayerState(ServerPlayerState.MustWait);
		else
			setServerPlayerState(ServerPlayerState.MustAct);
	}

}

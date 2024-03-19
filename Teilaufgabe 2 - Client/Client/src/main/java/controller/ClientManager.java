package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gameData.GameState;
import gameData.map.halfGameMap.HalfGameMapGenerator;
import navigation.NavigationManager;
import navigation.NextMove;

public class ClientManager {

	private static Logger logger = LoggerFactory.getLogger(ClientManager.class);
	private ClientNetwork clientNetwork;
	private GameState gameState = new GameState();
	private NavigationManager navigationManager;

	public ClientManager(ClientNetwork clientNetwork, NavigationManager navigationManager) {
		this.clientNetwork = clientNetwork;
		this.navigationManager = navigationManager;
	}

	public boolean updateGameState(GameState gameState) {
		if (gameState.equals(this.gameState))
			return false;

		if (gameState.getMyPlayer().orElseThrow().hasCollectedTreasure()
				&& !this.gameState.getMyPlayer().orElseThrow().hasCollectedTreasure())
			navigationManager.getPathToCurrentGoal().clear();

		if (gameState.getMyPlayer().isPresent())
			this.gameState.setMyPlayer(gameState.getMyPlayer().orElseThrow());
		if (gameState.getEnemyPlayer().isPresent())
			this.gameState.setEnemyPlayer(gameState.getEnemyPlayer().orElseThrow());
		this.gameState.setGameMap(gameState.getGameMap());
		this.gameState.setGameStateId(gameState.getGameStateId());

		return true;
	}

	private void move() throws ClientNetworkException {
		NextMove nextMove = navigationManager.getNextMove(gameState);
		logger.info("Moving {}", nextMove);
		clientNetwork.sendNextMove(nextMove);
		gameState.incrementRoundNumber();
	}

	public NavigationManager getNavigationManager() {
		return navigationManager;
	}

	public void run(String firstName, String lastName, String uAccount) {
		try {
			logger.info("Attempting player registration with credentials: first name={}, last name={}, uAccount={}",
					firstName, lastName, uAccount);
			clientNetwork.sendPlayerRegistration(firstName, lastName, uAccount);
			logger.info("Registration successful. Your Player ID is: {}", clientNetwork.getPlayerId());
		} catch (ClientNetworkException e) {
			logger.error("Registration failed. Please check your credentials.", e);
			System.exit(1);
		}

		try {
			while (true) {
				updateGameState(clientNetwork.sendGameStateRequest());
				if (gameState.getMyPlayer().orElseThrow().mustAct() == true)
					break;

				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {

				}
			}
		} catch (ClientNetworkException e) {
			logger.error("Received error message from server: {}", e);
			System.exit(1);
		}

		try {
			HalfGameMapGenerator halfGameMapGenerator = new HalfGameMapGenerator();
			clientNetwork.sendHalfMap(halfGameMapGenerator.generateHalfGameMap());
			logger.info("Sent half map to server.");
		} catch (ClientNetworkException e) {
			logger.error("Sent half map is invalid.", e);
			System.exit(1);
		}

		try {
			while (true) {
				updateGameState(clientNetwork.sendGameStateRequest());
				if (gameState.getMyPlayer().orElseThrow().hasWon() == true) {
					System.out.println("Congratulations! You are the winner!");
					break;
				} else if (gameState.getMyPlayer().orElseThrow().hasLost() == true) {
					System.out.println("You lost the game.");
					break;
				}
				if (gameState.getMyPlayer().orElseThrow().mustAct() == true)
					move();

				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {

				}

			}
		} catch (ClientNetworkException e) {
			logger.error("Received error message from server: {}", e);
			System.exit(1);
		}
	}

	public GameState getGameState() {
		return gameState;
	}

}
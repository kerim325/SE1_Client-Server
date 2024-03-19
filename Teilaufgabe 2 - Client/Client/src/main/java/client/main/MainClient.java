package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.ClientManager;
import controller.ClientNetwork;
import navigation.NavigationManager;
import ui.GameStateVisualisation;

public class MainClient {

	private static Logger logger = LoggerFactory.getLogger(MainClient.class);

	public static void main(String[] args) {

		logger.info("Starting client application...");

		if (args.length < 3) {
			logger.error("Invalid command line arguments.");
			System.err.println(
					"Invalid command line arguments. Usage: java -jar <FileNameClient.jar> <Modus> <BasisUrlServer> <GameId>");
			System.exit(1);
		}

		String serverBaseUrl = args[1];
		String gameId = args[2];

		String firstName = "firstName";
		String lastName = "lastName";
		String uAccount = "uAccount";

		NavigationManager navigationManager = new NavigationManager();
		ClientNetwork clientNetwork = new ClientNetwork(serverBaseUrl, gameId);
		ClientManager clientManager = new ClientManager(clientNetwork, navigationManager);
		new GameStateVisualisation(clientManager.getGameState());

		clientManager.run(firstName, lastName, uAccount);

		logger.info("Game is over! Ending client application...");

		System.exit(0);

	}

}

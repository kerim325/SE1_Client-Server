package server.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import server.exceptions.ServerException;
import server.gameData.Game;
import server.gameData.GameId;
import server.gameData.gameMap.halfGameMap.HalfGameMap;
import server.gameData.player.Player;
import server.gameData.player.PlayerId;
import server.ruleChecker.RuleChecker;

@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {
	private static Logger logger = LoggerFactory.getLogger(ServerEndpoints.class);
	private ServerManager serverManager = new ServerManager();
	private ConverterToClient converterToClient = new ConverterToClient();
	private ConverterToServer converterToServer = new ConverterToServer();
	private RuleChecker ruleChecker = new RuleChecker(serverManager);

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {

		GameId newGameId = serverManager.createNewGame();
		UniqueGameIdentifier gameIdentifier = converterToClient.toGameIdentifier(newGameId);

		logger.info("New game created: " + newGameId.gameId());

		return gameIdentifier;
	}

	@RequestMapping(value = "/{gameId}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameId,
			@Validated @RequestBody PlayerRegistration playerRegistration) {

		Player newPlayer = converterToServer.toPlayer(playerRegistration);
		GameId serverGameId = converterToServer.toGameId(gameId);

		logger.info("Player registration arrived for game " + serverGameId.gameId());

		PlayerId newPlayerId = serverManager.registerPlayer(serverGameId, newPlayer);

		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(
				converterToClient.toUniquePlayerIdentifier(newPlayerId));

		logger.info("Player (Id:" + newPlayerId.playerId() + ") registered for game " + serverGameId.gameId());

		return playerIDMessage;
	}

	@RequestMapping(value = "/{gameId}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> receiveHalfMap(@Validated @PathVariable UniqueGameIdentifier gameId,
			@Validated @RequestBody PlayerHalfMap playerHalfMap) {

		GameId serverGameId = converterToServer.toGameId(gameId);
		PlayerId serverPlayerId = converterToServer.extractPlayerIdFromPlayerHalfMap(playerHalfMap);
		Player player = serverManager.getGame(serverGameId).getPlayer(serverPlayerId);

		logger.info("Player (Id:" + serverPlayerId.playerId() + ") sent halfmap for game (Id:" + serverGameId.gameId()
				+ ")");

		ruleChecker.check(player, serverGameId, serverPlayerId);
		ruleChecker.check(playerHalfMap, serverGameId, serverPlayerId);

		HalfGameMap halfGameMap = converterToServer.toHalfGameMap(playerHalfMap);

		serverManager.addHalfGameMapToGame(serverGameId, serverPlayerId, halfGameMap);

		ResponseEnvelope<?> playerIDMessage = new ResponseEnvelope<>();

		logger.info("Players (Id:" + serverPlayerId.playerId() + ") halfmap was saved. Game: " + serverGameId.gameId());

		return playerIDMessage;
	}

	@RequestMapping(value = "/{gameId}/states/{playerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> sendGameState(@Validated @PathVariable UniqueGameIdentifier gameId,
			@Validated @PathVariable UniquePlayerIdentifier playerId) {

		PlayerId serverPlayerId = converterToServer.toPlayerId(playerId);
		GameId serverGameId = converterToServer.toGameId(gameId);

		logger.info("Player (Id:" + serverPlayerId.playerId() + ") asked for game state.");

		Game serverGame = serverManager.getGame(serverGameId);

		GameState gameState = converterToClient.toGameStateRelativeToPlayer(serverGame, serverPlayerId);

		logger.info("Sending player (Id:" + serverPlayerId.playerId() + ") game state (Id:" + gameState.getGameStateId()
				+ ")");

		return new ResponseEnvelope<GameState>(gameState);
	}

	@Scheduled(fixedRate = 1000)
	public void removeExpiredGames() {
		serverManager.deleteExpiredGames();
	}

	@ExceptionHandler({ ServerException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(ServerException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
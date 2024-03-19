package controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import gameData.map.halfGameMap.HalfGameMap;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import navigation.NextMove;
import reactor.core.publisher.Mono;

public class ClientNetwork {

	private WebClient baseWebClient;
	private String gameId;
	private String playerId;
	private ClientConverter clientConverter;

	public ClientNetwork(WebClient baseWebClient, String gameId, ClientConverter clientConverter) {
		this.baseWebClient = baseWebClient;
		this.gameId = gameId;
		this.clientConverter = clientConverter;
	}

	public ClientNetwork(String serverBaseUrl, String gameId) {
		this.baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
		this.gameId = gameId;
		this.clientConverter = new ClientConverter();
	}

	public String getPlayerId() {
		return playerId;
	}

	public String sendPlayerRegistration(String firstName, String lastName, String uAccount)
			throws ClientNetworkException {
		PlayerRegistration playerReg = new PlayerRegistration(firstName, lastName, uAccount);

		Mono<ResponseEnvelope<UniquePlayerIdentifier>> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + gameId + "/players").body(BodyInserters.fromValue(playerReg)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<UniquePlayerIdentifier>>() {
				});

		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error)
			throw new ClientNetworkException(resultReg.getExceptionMessage());

		UniquePlayerIdentifier uniqueId = resultReg.getData().get();
		playerId = uniqueId.getUniquePlayerID();
		return playerId;
	}

	public gameData.GameState sendGameStateRequest() throws ClientNetworkException {
		Mono<ResponseEnvelope<messagesbase.messagesfromserver.GameState>> webAccess = baseWebClient
				.method(HttpMethod.GET).uri("/" + gameId + "/states/" + playerId).retrieve().bodyToMono(
						new ParameterizedTypeReference<ResponseEnvelope<messagesbase.messagesfromserver.GameState>>() {
						});

		ResponseEnvelope<messagesbase.messagesfromserver.GameState> requestResult = webAccess.block();

		if (requestResult.getState() == ERequestState.Error)
			throw new ClientNetworkException(requestResult.getExceptionMessage());

		return clientConverter.toGameState(requestResult.getData().get(), playerId);
	}

	public void sendHalfMap(HalfGameMap gameMap) throws ClientNetworkException {

		PlayerHalfMap playerHalfMap = clientConverter.toPlayerHalfMap(gameMap, playerId);

		Mono<ResponseEnvelope<Void>> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/halfmaps")
				.body(BodyInserters.fromValue(playerHalfMap)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<Void>>() {
				});

		ResponseEnvelope<Void> resultReg = webAccess.block();

		System.out.println();

		if (resultReg.getState() == ERequestState.Error)
			throw new ClientNetworkException(resultReg.getExceptionMessage());
	}

	public void sendNextMove(NextMove nextMove) throws ClientNetworkException {
		PlayerMove playerMove = clientConverter.toPlayerMove(nextMove, playerId);

		Mono<ResponseEnvelope<Void>> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/moves")
				.body(BodyInserters.fromValue(playerMove)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<Void>>() {
				});

		ResponseEnvelope<Void> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error)
			throw new ClientNetworkException(resultReg.getExceptionMessage());
	}

}

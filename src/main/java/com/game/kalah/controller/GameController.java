package com.game.kalah.controller;

import java.net.InetAddress;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game.kalah.common.model.entity.Game;
import com.game.kalah.common.model.entity.Pit;
import com.game.kalah.common.model.response.GameResponse;
import com.game.kalah.service.GameService;

@RestController
@RequestMapping("/api/kalah")
public class GameController {
	
	@Value("${server.port}")
    private String serverPort;
	
	@Autowired
    private GameService service;
    
    private String getUrl(final String gameId) {
        return String.format("http://%s:%s/api/kalah/games/%s", InetAddress.getLoopbackAddress().getHostName(),
        		serverPort, gameId);
    }
    
    @PostMapping("/games")
    public ResponseEntity<GameResponse> createGame() {
        final Game game = service.createGame();
        final GameResponse gameResponse = new GameResponse(game.getId(), getUrl(game.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(gameResponse);
    }

    @PutMapping("/games/{gameId}/pits/{pitId}")
    public ResponseEntity<GameResponse> playGame(@PathVariable final String gameId, @PathVariable final Integer pitId) {
        final Game game = service.play(gameId, pitId);
        final Map<Integer, String> status = game.getBoard().getPits().stream()
                .collect(Collectors.toMap(Pit::getId, value -> Integer.toString(value.getStoneCount())));
        final GameResponse gameResponse = new GameResponse(game.getId(), getUrl(game.getId()), status);
        return ResponseEntity.status(HttpStatus.OK).body(gameResponse);
    }
}

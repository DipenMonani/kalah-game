package com.game.kalah.common.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.game.kalah.common.exception.GameNotFoundException;
import com.game.kalah.common.model.entity.Game;

@Service
public class InMemoryGameRepository implements GameRepository {

    private final Map<String, Game> repository = new HashMap<>();

    @Override
    public Game find(final String id) {
        final Game game = this.repository.get(id);
        if (game == null) {
            throw new GameNotFoundException(id);
        }
        return game;
    }

    @Override
    public Game save(final Game game) {
        this.repository.put(game.getId(), game);
        return this.find(game.getId());
    }

}

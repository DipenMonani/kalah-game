package com.game.kalah.common.repository;

import com.game.kalah.common.model.entity.Game;

public interface GameRepository {

    Game find(final String id);

    Game save(final Game game);

}

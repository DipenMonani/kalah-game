package com.game.kalah.common.enums;

import com.game.kalah.common.model.entity.GameBoard;

public enum Player {
    PLAYER1(GameBoard.END_PIT_POSSITION / 2),
    PLAYER2(GameBoard.END_PIT_POSSITION);

    private int houseIndex;

    Player(final int houseIndex) {
        this.houseIndex = houseIndex;
    }

    public int getHouseIndex() {
        return this.houseIndex;
    }
}

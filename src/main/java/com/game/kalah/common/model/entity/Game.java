package com.game.kalah.common.model.entity;

import java.util.UUID;
import com.game.kalah.common.enums.Player;

public class Game {

    private final String id;
    private final GameBoard board;
    private Player winner;
    private Player turn;

    public Game() {
        this.id = UUID.randomUUID().toString();
        this.board = new GameBoard();
    }

    public GameBoard getBoard() {
        return this.board;
    }

    public String getId() {
        return this.id;
    }

    public Player getTurn() {
        return this.turn;
    }

    public void setTurn(final Player turn) {
        this.turn = turn;
    }

    public Player getWinner() {
        return this.winner;
    }

    public void setWinner(final Player winner) {
        this.winner = winner;
    }
}

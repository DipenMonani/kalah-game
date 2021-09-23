package com.game.kalah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.kalah.common.enums.Player;
import com.game.kalah.common.exception.IllegalMoveException;
import com.game.kalah.common.model.entity.GameBoard;
import com.game.kalah.common.model.entity.Game;
import com.game.kalah.common.model.entity.Pit;
import com.game.kalah.common.repository.GameRepository;

@Service
public class GameService {

    private final GameRepository repository;

    @Autowired
    public GameService(final GameRepository repository) {
        this.repository = repository;
    }

    public Game createGame() {
        return this.repository.save(new Game());
    }

    public Game play(final String gameId, final Integer pitId) {
        final Game game = this.repository.find(gameId);
        distributeStones(game, pitId);
        checkGameOver(game);
        return game;
    }

    private void resetBoard(final Game game) {
        game.getBoard().getPits().parallelStream()
                .filter(pit -> (Player.PLAYER1.getHouseIndex() != pit.getId())
                               && (Player.PLAYER2.getHouseIndex() != pit.getId()))
                .forEach(pit -> pit.setStoneCount(0));
    }

    private void distributeStones(final Game game, int pitId) {
        final Pit startPit = game.getBoard().getPit(pitId);
        validateMove(game, pitId);
        int stoneToDistribute = startPit.getStoneCount();
        startPit.setStoneCount(0);
        while (stoneToDistribute > 0) {
            final Pit currentPit = game.getBoard().getPit(++pitId);
            if (currentPit.isDistributable(game.getTurn())) {
                currentPit.setStoneCount(currentPit.getStoneCount() + 1);
                stoneToDistribute--;
            }
        }
        lastEmptyPit(game, pitId);
        decidePlayerTurn(game, pitId);
    }

    private void checkGameOver(final Game game) {
        final int playerNorthPitStoneCount = game.getBoard().getStoneCount(Player.PLAYER1, false);
        final int playerSouthPitStoneCount = game.getBoard().getStoneCount(Player.PLAYER2, false);
        if ((playerNorthPitStoneCount == 0) || (playerSouthPitStoneCount == 0)) {
            final Pit houseNorth = game.getBoard().getPit(Player.PLAYER1.getHouseIndex());
            final Pit houseSouth = game.getBoard().getPit(Player.PLAYER2.getHouseIndex());
            houseNorth.setStoneCount(houseNorth.getStoneCount() + playerNorthPitStoneCount);
            houseSouth.setStoneCount(houseSouth.getStoneCount() + playerSouthPitStoneCount);
            determineWinner(game);
            resetBoard(game);
        }
    }

    private void determineWinner(final Game game) {
        final int houseNorthStoneCount = game.getBoard().getStoneCount(Player.PLAYER1, true);
        final int houseSouthStoneCount = game.getBoard().getStoneCount(Player.PLAYER2, true);
        if (houseNorthStoneCount > houseSouthStoneCount) {
            game.setWinner(Player.PLAYER1);
        } else if (houseNorthStoneCount < houseSouthStoneCount) {
            game.setWinner(Player.PLAYER2);
        }
    }

    private void lastEmptyPit(final Game game, final int endPitId) {
        final Pit endPit = game.getBoard().getPit(endPitId);
        if (!endPit.isHouse() && endPit.getOwner().equals(game.getTurn())
            && (endPit.getStoneCount() == 1)) {
            final Pit oppositePit = game.getBoard().getPit(GameBoard.END_PIT_POSSITION - endPit.getId());
            if (oppositePit.getStoneCount() > 0) {
                final Pit house = game.getBoard().getPit(endPit.getOwner().getHouseIndex());
                house.setStoneCount(
                        (house.getStoneCount() + oppositePit.getStoneCount()) + endPit.getStoneCount());
                oppositePit.setStoneCount(0);
                endPit.setStoneCount(0);
            }
        }
    }

    private void decidePlayerTurn(final Game game, final int pitId) {
        final Pit pit = game.getBoard().getPit(pitId);
        if (pit.isHouse() && Player.PLAYER1.equals(pit.getOwner())
            && Player.PLAYER1.equals(game.getTurn())) {
            game.setTurn(Player.PLAYER1);
        } else if (pit.isHouse() && Player.PLAYER2.equals(pit.getOwner())
                   && Player.PLAYER2.equals(game.getTurn())) {
            game.setTurn(Player.PLAYER2);
        } else {
            if (Player.PLAYER1.equals(game.getTurn())) {
                game.setTurn(Player.PLAYER2);
            } else {
                game.setTurn(Player.PLAYER1);
            }
        }
    }

    private void validateMove(final Game game, final int startPitId) {
        final Pit startPit = game.getBoard().getPit(startPitId);
        if (startPit.isHouse()) {
            throw new IllegalMoveException("Can not start from house");
        }
        if (Player.PLAYER1.equals(game.getTurn())
            && !Player.PLAYER1.equals(startPit.getOwner())) {
            throw new IllegalMoveException("It's Player North's turn");
        }
        if (Player.PLAYER2.equals(game.getTurn())
            && !Player.PLAYER2.equals(startPit.getOwner())) {
            throw new IllegalMoveException("It's Player South's turn");
        }
        if (startPit.getStoneCount() == 0) {
            throw new IllegalMoveException("Can not start from empty pit");
        }
        if (game.getTurn() == null) {
            if (Player.PLAYER1.equals(startPit.getOwner())) {
                game.setTurn(Player.PLAYER1);
            } else {
                game.setTurn(Player.PLAYER2);
            }
        }
    }
}

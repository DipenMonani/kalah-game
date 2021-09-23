package com.game.kalah.common.model.entity;

import java.util.ArrayList;
import java.util.List;
import com.game.kalah.common.enums.Player;

public class GameBoard {

    public static final int START_PIT_POSSITION = 1;
    public static final int END_PIT_POSSITION = 14;

    private final List<Pit> pits;

    public GameBoard() {
        this.pits = new ArrayList<>();
        for (int i = GameBoard.START_PIT_POSSITION; i <= GameBoard.END_PIT_POSSITION; i++) {
            this.pits.add(new Pit(i));
        }
    }

    public Pit getPit(final int index) {
        return this.pits.get((index - 1) % GameBoard.END_PIT_POSSITION);
    }

    public List<Pit> getPits() {
        return this.pits;
    }

    public int getStoneCount(final Player player, final boolean includeHouse) {
        return this.getPits().stream()
                .filter(pit -> (pit.getOwner().equals(player) && (includeHouse || !pit.isHouse())))
                .mapToInt(Pit::getStoneCount).sum();
    }
}

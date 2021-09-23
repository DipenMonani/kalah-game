package com.game.kalah.common.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.game.kalah.common.enums.Player;

public class Pit {

    private final int id;
    private int stoneCount;

    @JsonCreator
    public Pit(@JsonProperty("id") final int id) {
        this.id = id;
        if (!this.isHouse()) {
            this.setStoneCount(6);
        }
    }

    public int getId() {
        return this.id;
    }

    public Player getOwner() {
        if (this.getId() <= Player.PLAYER1.getHouseIndex()) {
            return Player.PLAYER1;
        } else {
            return Player.PLAYER2;
        }
    }

    public int getStoneCount() {
        return this.stoneCount;
    }

    public void setStoneCount(final int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public boolean isDistributable(final Player turn) {
        return (!turn.equals(Player.PLAYER1)
                || (this.getId() != Player.PLAYER2.getHouseIndex()))
               && (!turn.equals(Player.PLAYER2)
                   || (this.getId() != Player.PLAYER1.getHouseIndex()));
    }

    public boolean isHouse() {
        return (this.getId() == Player.PLAYER1.getHouseIndex())
               || (this.getId() == Player.PLAYER2.getHouseIndex());
    }
}

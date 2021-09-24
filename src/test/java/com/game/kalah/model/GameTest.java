package com.game.kalah.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.game.kalah.common.enums.Player;
import com.game.kalah.common.model.entity.Game;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameTest {

  @Test
  public void initialization() {
    final Game game = new Game();

    Assert.assertNotNull(game.getBoard());
    Assert.assertNull(game.getTurn());
    Assert.assertNull(game.getWinner());
  }

  @Test
  public void winner() {
    final Game game = new Game();
    game.setWinner(Player.PLAYER1);

    Assert.assertEquals(Player.PLAYER1, game.getWinner());

    game.setWinner(Player.PLAYER2);

    Assert.assertEquals(Player.PLAYER2, game.getWinner());
  }

  @Test
  public void turn() {
    final Game game = new Game();
    game.setTurn(Player.PLAYER1);

    Assert.assertEquals(Player.PLAYER1, game.getTurn());

    game.setTurn(Player.PLAYER2);

    Assert.assertEquals(Player.PLAYER2, game.getTurn());
  }
}

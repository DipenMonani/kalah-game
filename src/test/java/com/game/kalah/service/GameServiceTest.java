package com.game.kalah.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import com.game.kalah.common.enums.Player;
import com.game.kalah.common.exception.IllegalMoveException;
import com.game.kalah.common.model.entity.Game;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

  @Autowired
  private GameService service;

  private Game initial;
  private Game finishedWinnerPlayer1;
  private Game finishedWinnerPlayer2;
  private Game player1MovedFirst;
  private Game player2MovedFirst;
  private Game turnPlayer1;
  private Game turnPlayer2;

  @Before
  public void init()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    this.initial = new Game();
    this.finishedWinnerPlayer2 = new Game();
    this.finishedWinnerPlayer1 = new Game();
    this.player1MovedFirst = new Game();
    this.player2MovedFirst = new Game();
    this.turnPlayer1 = new Game();
    this.turnPlayer2 = new Game();

    final Method resetBoard =
        openMethodForTest(service.getClass().getDeclaredMethod("resetBoard", Game.class));
    final Method distributeStones = openMethodForTest(
        service.getClass().getDeclaredMethod("distributeStones", Game.class, int.class));

    resetBoard.invoke(this.service, this.finishedWinnerPlayer2);
    this.finishedWinnerPlayer2.getBoard().getPit(Player.PLAYER1.getHouseIndex())
        .setStoneCount(10);
    this.finishedWinnerPlayer2.getBoard().getPit(Player.PLAYER2.getHouseIndex())
        .setStoneCount(62);
    resetBoard.invoke(this.service, this.finishedWinnerPlayer1);
    this.finishedWinnerPlayer1.getBoard().getPit(1).setStoneCount(1);
    this.finishedWinnerPlayer1.getBoard().getPit(Player.PLAYER1.getHouseIndex())
        .setStoneCount(39);
    this.finishedWinnerPlayer1.getBoard().getPit(Player.PLAYER2.getHouseIndex())
        .setStoneCount(32);

    distributeStones.invoke(this.service, this.player1MovedFirst, 1);
    distributeStones.invoke(this.service, this.player2MovedFirst, 10);
    distributeStones.invoke(this.service, this.turnPlayer1, 1);
    distributeStones.invoke(this.service, this.turnPlayer2, 8);
  }

  @Test
  @DirtiesContext
  public void createGame() {
    final Game game = this.service.createGame();

    Assert.assertNotNull(game);
  }

  @Test
  @DirtiesContext
  public void play() {
    final Game game = this.service.createGame();
    this.service.play(game.getId(), 6);

    Assert.assertEquals(Player.PLAYER2, game.getTurn());
    Assert.assertNull(game.getWinner());
    Assert.assertEquals(31, game.getBoard().getStoneCount(Player.PLAYER1, true));
  }

  @Test
  public void decidePlayerTurn() {
    Assert.assertNull(this.initial.getTurn());
    Assert.assertEquals(Player.PLAYER1, this.player1MovedFirst.getTurn());
    Assert.assertEquals(Player.PLAYER1, this.player2MovedFirst.getTurn());
  }

  @Test
  public void determineWinner()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    final Method determineWinner =
        openMethodForTest(service.getClass().getDeclaredMethod("determineWinner", Game.class));
    determineWinner.invoke(this.service, this.initial);
    determineWinner.invoke(this.service, this.finishedWinnerPlayer2);
    determineWinner.invoke(this.service, this.finishedWinnerPlayer1);

    Assert.assertNull(this.initial.getWinner());
    Assert.assertEquals(Player.PLAYER2, this.finishedWinnerPlayer2.getWinner());
    Assert.assertEquals(Player.PLAYER1, this.finishedWinnerPlayer1.getWinner());
  }

  @Test
  public void checkGameOver()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    final Method checkGameOver =
        openMethodForTest(service.getClass().getDeclaredMethod("checkGameOver", Game.class));
    checkGameOver.invoke(this.service, this.initial);
    checkGameOver.invoke(this.service, this.finishedWinnerPlayer2);
    checkGameOver.invoke(this.service, this.finishedWinnerPlayer1);

    Assert.assertEquals(36, this.initial.getBoard().getStoneCount(Player.PLAYER1, true));
    Assert.assertEquals(36, this.initial.getBoard().getStoneCount(Player.PLAYER2, true));
    Assert.assertEquals(10,
        this.finishedWinnerPlayer2.getBoard().getStoneCount(Player.PLAYER1, true));
    Assert.assertEquals(62,

        this.finishedWinnerPlayer2.getBoard().getStoneCount(Player.PLAYER2, true));
    Assert.assertEquals(40,
        this.finishedWinnerPlayer1.getBoard().getStoneCount(Player.PLAYER1, true));
    Assert.assertEquals(32,
        this.finishedWinnerPlayer1.getBoard().getStoneCount(Player.PLAYER2, true));
  }

  @Test(expected = IllegalMoveException.class)
  public void invalidMovePlayerNorthTurn() throws Throwable {
    try {
      final Method validateMove = openMethodForTest(
          service.getClass().getDeclaredMethod("validateMove", Game.class, int.class));
      validateMove.invoke(this.service, this.turnPlayer1, 12);
    } catch (final Exception e) {
      if (e instanceof InvocationTargetException) {
        throw ((InvocationTargetException) e).getTargetException();
      } else {
        throw e;
      }
    }
  }

  @Test(expected = IllegalMoveException.class)
  public void invalidMovePlayerSouthTurn() throws Throwable {
    try {
      final Method validateMove = openMethodForTest(
          service.getClass().getDeclaredMethod("validateMove", Game.class, int.class));
      validateMove.invoke(this.service, this.turnPlayer2, 1);
    } catch (final Exception e) {
      if (e instanceof InvocationTargetException) {
        throw ((InvocationTargetException) e).getTargetException();
      } else {
        throw e;
      }
    }
  }

  @Test(expected = IllegalMoveException.class)
  public void invalidMoveStartFromEmptyPit() throws Throwable {
    try {
      final Method validateMove = openMethodForTest(
          service.getClass().getDeclaredMethod("validateMove", Game.class, int.class));
      validateMove.invoke(this.service, this.turnPlayer1, 1);
    } catch (final Exception e) {
      if (e instanceof InvocationTargetException) {
        throw ((InvocationTargetException) e).getTargetException();
      } else {
        throw e;
      }
    }
  }

  @Test(expected = IllegalMoveException.class)
  public void invalidMoveStartFromHouse() throws Throwable {
    try {
      final Method validateMove = openMethodForTest(
          service.getClass().getDeclaredMethod("validateMove", Game.class, int.class));
      validateMove.invoke(this.service, this.initial, Player.PLAYER1.getHouseIndex());
    } catch (final Exception e) {
      if (e instanceof InvocationTargetException) {
        throw ((InvocationTargetException) e).getTargetException();
      } else {
        throw e;
      }
    }
  }

  @Test
  public void lastEmptyPit()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    final Method resetBoard =
        openMethodForTest(service.getClass().getDeclaredMethod("resetBoard", Game.class));
    final Method lastEmptyPit = openMethodForTest(
        service.getClass().getDeclaredMethod("lastEmptyPit", Game.class, int.class));

    resetBoard.invoke(this.service, this.turnPlayer1);
    this.turnPlayer1.getBoard().getPit(Player.PLAYER1.getHouseIndex()).setStoneCount(0);
    this.turnPlayer1.getBoard().getPit(Player.PLAYER2.getHouseIndex()).setStoneCount(0);
    this.turnPlayer1.getBoard().getPit(4).setStoneCount(1);
    this.turnPlayer1.getBoard().getPit(10).setStoneCount(6);

    lastEmptyPit.invoke(this.service, this.turnPlayer1, 4);

    Assert.assertEquals(7, this.turnPlayer1.getBoard().getStoneCount(Player.PLAYER1, true));
    Assert.assertEquals(0, this.turnPlayer1.getBoard().getStoneCount(Player.PLAYER2, true));
  }

  @Test
  public void validMovePlayer1()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    final Method validateMove = openMethodForTest(
        service.getClass().getDeclaredMethod("validateMove", Game.class, int.class));
    validateMove.invoke(this.service, this.initial, 1);
    Assert.assertEquals(Player.PLAYER1, this.initial.getTurn());
  }

  @Test
  public void validMovePlayer2()
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    final Method validateMove = openMethodForTest(
        service.getClass().getDeclaredMethod("validateMove", Game.class, int.class));
    validateMove.invoke(this.service, this.initial, 13);
    Assert.assertEquals(Player.PLAYER2, this.initial.getTurn());
  }

  private Method openMethodForTest(final Method method) {
    ReflectionUtils.makeAccessible(method);
    return method;
  }
}

package com.game.kalah.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.game.kalah.common.enums.Player;
import com.game.kalah.common.model.entity.Pit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PitTest {

  @Test
  public void distributable() {
    final Pit pit1 = new Pit(1);
    final Pit pit2 = new Pit(14);
    final Pit pit3 = new Pit(7);

    Assert.assertTrue(pit1.isDistributable(Player.PLAYER1));
    Assert.assertTrue(pit2.isDistributable(Player.PLAYER2));
    Assert.assertFalse(pit2.isDistributable(Player.PLAYER1));
    Assert.assertFalse(pit3.isDistributable(Player.PLAYER2));
  }

  @Test
  public void house() {
    final Pit pit1 = new Pit(7);
    final Pit pit2 = new Pit(14);
    final Pit pit3 = new Pit(3);
    final Pit pit4 = new Pit(9);

    Assert.assertTrue(pit1.isHouse());
    Assert.assertTrue(pit2.isHouse());
    Assert.assertFalse(pit3.isHouse());
    Assert.assertFalse(pit4.isHouse());
  }

  @Test
  public void initialization() {
    final Pit pit1 = new Pit(1);
    final Pit pit2 = new Pit(14);
    final Pit pit3 = new Pit(7);

    Assert.assertEquals(1, pit1.getId());
    Assert.assertEquals(14, pit2.getId());
    Assert.assertEquals(7, pit3.getId());
  }

  @Test
  public void initialStoneCount() {
    final Pit pit1 = new Pit(1);
    final Pit pit2 = new Pit(7);

    Assert.assertEquals(6, pit1.getStoneCount());
    Assert.assertEquals(0, pit2.getStoneCount());
  }

  @Test
  public void owner() {
    final Pit pit1 = new Pit(4);
    final Pit pit2 = new Pit(7);
    final Pit pit3 = new Pit(10);
    final Pit pit4 = new Pit(14);

    Assert.assertEquals(Player.PLAYER1, pit1.getOwner());
    Assert.assertEquals(Player.PLAYER1, pit2.getOwner());
    Assert.assertEquals(Player.PLAYER2, pit3.getOwner());
    Assert.assertEquals(Player.PLAYER2, pit4.getOwner());
  }

  @Test
  public void stoneCountSet() {
    final Pit givenPit = new Pit(1);
    givenPit.setStoneCount(7);

    Assert.assertEquals(7, givenPit.getStoneCount());
  }
}

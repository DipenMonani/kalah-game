package com.game.kalah.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.game.kalah.common.enums.Player;
import com.game.kalah.common.model.entity.GameBoard;
import com.game.kalah.common.model.entity.Pit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameBoardTest {

	@Test
	public void initialization() {
		final GameBoard gameBoard = new GameBoard();

		Assert.assertNotNull(gameBoard.getPits());
		Assert.assertEquals(GameBoard.END_PIT_POSSITION, gameBoard.getPits().size());
	}

	@Test
	public void getPit() {
		final GameBoard gameBoard = new GameBoard();
		final Pit givenPit = gameBoard.getPit(4);

		Assert.assertNotNull(givenPit);
		Assert.assertEquals(4, givenPit.getId());
	}

	@Test
	public void stoneCount() {
		final GameBoard gameBoard1 = new GameBoard();
		final GameBoard gameBoard2 = new GameBoard();
		gameBoard2.getPit(5).setStoneCount(0);
		gameBoard2.getPit(11).setStoneCount(9);

		Assert.assertEquals(36, gameBoard1.getStoneCount(Player.PLAYER1, true));
		Assert.assertEquals(30, gameBoard2.getStoneCount(Player.PLAYER1, true));
		Assert.assertEquals(39, gameBoard2.getStoneCount(Player.PLAYER2, true));
	}
}

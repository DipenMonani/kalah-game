package com.game.kalah.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import com.game.kalah.common.model.entity.Game;
import com.game.kalah.common.repository.GameRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRepositoryTest {

  @Autowired
  private GameRepository repository;

  @Test
  @DirtiesContext
  public void find() {
    final Game savedGame = this.repository.save(new Game());
    final Game game = this.repository.find(savedGame.getId());

    Assert.assertNotNull(savedGame);
    Assert.assertEquals(savedGame, game);
  }

  @Test
  @DirtiesContext
  public void save() {
    final Game game = this.repository.save(new Game());

    Assert.assertNotNull(game);
  }
}

package com.game.kalah.model.exception;

import javax.annotation.PostConstruct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.kalah.common.model.entity.Game;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class GameExceptionHandlerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@PostConstruct
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void gameNotFoundException() throws Exception {
		final MockHttpServletRequestBuilder gameRequest = MockMvcRequestBuilders.put("/api/kalah/games/123/pits/7");
		this.mockMvc.perform(gameRequest).andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().string("Could not find game 123")).andReturn();
	}

	@Test
	public void illegalMoveException() throws Exception {
		final MockHttpServletRequestBuilder intGameRequest = MockMvcRequestBuilders.post("/api/kalah/games");
		final String responseString = this.mockMvc.perform(intGameRequest).andReturn().getResponse()
				.getContentAsString();
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		final Game game = objectMapper.readValue(responseString, Game.class);

		final MockHttpServletRequestBuilder gameRequest = MockMvcRequestBuilders
				.put("/api/kalah/games/" + game.getId() + "/pits/7");
		this.mockMvc.perform(gameRequest).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Illegal move: Can not start from house"))
				.andReturn();
	}
}
package com.kxtract.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.context.ContextConfiguration;

import com.kxtract.app.WebSecurityConfig;
import com.kxtract.data.EpisodeRepository;
import com.kxtract.data.PodcastRepository;

@AutoConfigureMockMvc
@ContextConfiguration(classes = { RefreshController.class })
@WebMvcTest(includeFilters = @Filter(classes = EnableWebSecurity.class))
@Import(WebSecurityConfig.class)
public class RefreshControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PodcastRepository podRepo;

	@MockBean
	private EpisodeRepository episodeRepo;

	@Disabled
	@Test
	public void testClearCache() throws Exception {
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/clearcache"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();
		assertTrue(content.contains("files were deleted"));
	}

	@Test
	public void testRefresh() throws Exception {
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/refresh"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();
		assertTrue(content.contains("<li>Downloads: <span>0</span> episodes</li>"));
		assertTrue(content.contains("<li>Uploaded to S3: <span>0</span> episodes</li>"));
		assertTrue(content.contains("<li>Errors: <span>0</span> </li>"));

	}

}

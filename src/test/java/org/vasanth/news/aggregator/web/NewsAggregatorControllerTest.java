package org.vasanth.news.aggregator.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.vasanth.news.aggregator.dto.NewsResponse;
import org.vasanth.news.aggregator.exception.AllProvidersFailedException;
import org.vasanth.news.aggregator.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = NewsAggregatorController.class) // Load only the controller layer
@ActiveProfiles("test")
public class NewsAggregatorControllerTest {

  @MockitoBean private NewsService newsService; // Mock the NewsService

  @Autowired private MockMvc mockMvc; // MockMvc for HTTP request simulations

  @Value("${pagination.default-page}")
  private int defaultPage;

  @Value("${pagination.default-limit}")
  private int defaultLimit;

  @Test
  public void testSearchNews_ValidRequest() throws Exception {

    String keyword = "tech";
    int page = 1;
    int limit = 10;

    NewsResponse mockResponse = new NewsResponse(); // Assume NewsResponse is a valid response DTO
    when(newsService.getNewsFeed(keyword, page, limit)).thenReturn(mockResponse);

    // Act & Assert
    mockMvc
        .perform(
            get("/news/search")
                .param("keyword", keyword)
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(limit)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testSearchNews_MissingPageOrLimit_UsesDefaultValues() throws Exception {
    // Arrange
    String keyword = "tech";
    // No page and limit params, so the defaults should be used
    NewsResponse mockResponse = new NewsResponse();
    when(newsService.getNewsFeed(keyword, defaultPage, defaultLimit)).thenReturn(mockResponse);

    // Act & Assert
    mockMvc
        .perform(get("/news/search").param("keyword", keyword))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$.totalResults").exists());
  }

  @Test
  public void testSearchNews_InternalServerError() throws Exception {
    // Arrange
    String keyword = "tech";

    // Mock the service layer to throw an exception
    when(newsService.getNewsFeed(keyword, defaultPage, defaultLimit))
        .thenThrow(new AllProvidersFailedException("All providers failed"));

    // Act & Assert
    mockMvc
        .perform(get("/news/search").param("keyword", keyword))
        .andExpect(status().isInternalServerError());
  }

  @Test
  public void testSearchNews_InternalServerError_Fallback() throws Exception {
    // Arrange
    String keyword = "tech";

    // Mock the service layer to throw an exception
    when(newsService.getNewsFeed(keyword, defaultPage, defaultLimit))
        .thenThrow(new AllProvidersFailedException("All providers failed"));

    // Act & Assert
    mockMvc
        .perform(get("/news/search").param("keyword", keyword))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}

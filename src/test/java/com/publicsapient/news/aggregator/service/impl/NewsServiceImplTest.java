package com.publicsapient.news.aggregator.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.publicsapient.news.aggregator.dto.HeadLine;
import com.publicsapient.news.aggregator.dto.NewsResponse;
import com.publicsapient.news.aggregator.exception.AllProvidersFailedException;
import com.publicsapient.news.aggregator.exception.NewsApiFailureException;
import com.publicsapient.news.aggregator.service.HeadLineProviderService;
import java.util.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

  private NewsServiceImpl newsService;

  @Mock private HeadLineProviderService mockProvider1;

  @Mock private HeadLineProviderService mockProvider2;

  private List<HeadLineProviderService> headLineProviderServices;

  @BeforeEach
  void setUp() {
//    MockitoAnnotations.openMocks(this);
    headLineProviderServices = List.of(mockProvider1, mockProvider2);
    newsService = new NewsServiceImpl(false, headLineProviderServices);
  }

  @Test
  @SneakyThrows
  void testGetNewsFeed_Success() throws NewsApiFailureException {
    // Arrange
    String keyword = "test";
    int page = 1;
    int limit = 5;

    HeadLine headLine1 = new HeadLine("Title 1", "url1", "lead 1", "source 1");
    HeadLine headLine2 = new HeadLine("Title 2", "url2", "lead 2", "source 2");

    when(mockProvider1.getNewsHeadLines(keyword)).thenReturn(List.of(headLine1));
    when(mockProvider2.getNewsHeadLines(keyword)).thenReturn(List.of(headLine2));

    // Act
    NewsResponse response = newsService.getNewsFeed(keyword, page, limit);

    // Assert
    assertNotNull(response);
    assertEquals(2, response.getHeadlines().size());
    assertEquals(2, response.getTotalResults());
    assertEquals("Title 1", response.getHeadlines().get(0).getTitle());
    assertEquals("Title 2", response.getHeadlines().get(1).getTitle());
  }

  @Test
  void testGetNewsFeed_OfflineMode() {
    // Arrange
    newsService = new NewsServiceImpl(true, headLineProviderServices); // offlineMode set to true
    String keyword = "test";
    int page = 1;
    int limit = 5;

    // Act & Assert
    AllProvidersFailedException exception =
        assertThrows(
            AllProvidersFailedException.class, () -> newsService.getNewsFeed(keyword, page, limit));
    assertEquals("All News Providers were currently down", exception.getMessage());
  }

  @Test
  void testGetNewsFeed_AllProvidersFailed() throws NewsApiFailureException {
    // Arrange
    String keyword = "test";
    int page = 1;
    int limit = 5;

    when(mockProvider1.getNewsHeadLines(keyword))
        .thenThrow(new NewsApiFailureException("Provider 1 failed"));
    when(mockProvider2.getNewsHeadLines(keyword))
        .thenThrow(new NewsApiFailureException("Provider 2 failed"));

    // Act & Assert
    AllProvidersFailedException exception =
        assertThrows(
            AllProvidersFailedException.class, () -> newsService.getNewsFeed(keyword, page, limit));
    assertEquals("All News Providers were currently down", exception.getMessage());
  }

  @Test
  void testGetNewsFeed_NoHeadlines() throws NewsApiFailureException {
    // Arrange
    String keyword = "test";
    int page = 1;
    int limit = 5;

    when(mockProvider1.getNewsHeadLines(keyword)).thenReturn(Collections.emptyList());
    when(mockProvider2.getNewsHeadLines(keyword)).thenReturn(Collections.emptyList());

    // Act & Assert
    AllProvidersFailedException exception =
        assertThrows(
            AllProvidersFailedException.class, () -> newsService.getNewsFeed(keyword, page, limit));
    assertEquals("All News Providers were currently down", exception.getMessage());
  }

  @Test
  @SneakyThrows
  void testGetNewsFeed_Pagination() throws NewsApiFailureException {
    // Arrange
    String keyword = "test";
    int page = 2; // Request page 2
    int limit = 1; // Limit to 1 headline per page

    HeadLine headLine1 = new HeadLine("Title 1", "url1", "lead 1", "source 1");
    HeadLine headLine2 = new HeadLine("Title 2", "url2", "lead 2", "source 2");

    when(mockProvider1.getNewsHeadLines(keyword)).thenReturn(List.of(headLine1));
    when(mockProvider2.getNewsHeadLines(keyword)).thenReturn(List.of(headLine2));

    // Act
    NewsResponse response = newsService.getNewsFeed(keyword, page, limit);

    // Assert
    assertNotNull(response);
    assertEquals(1, response.getHeadlines().size());
    assertEquals(
        "Title 2",
        response.getHeadlines().get(0).getTitle()); // Pagination should skip the first headline
  }
}

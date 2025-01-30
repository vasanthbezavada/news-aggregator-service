package com.publicsapient.news.aggregator.service.impl;

import com.publicsapient.news.aggregator.dto.HeadLine;
import com.publicsapient.news.aggregator.dto.nytimesResponse.DocsItem;
import com.publicsapient.news.aggregator.dto.nytimesResponse.NyTimesNewsFeedResponse;
import com.publicsapient.news.aggregator.dto.nytimesResponse.Response;
import com.publicsapient.news.aggregator.exception.NewsApiFailureException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NyTimesHeadLineProviderServiceTest {

  @InjectMocks private NyTimesHeadLineProviderService nyTimesHeadLineProviderService;

  @Mock private RestTemplate restTemplate;

  @Value("${news.nytimes-api-url}")
  private String nyTimesApiUrl;

  @Value("${news.nytimes-api-key}")
  private String nyTimesApiKey;

  @BeforeEach
  void setUp() {

    nyTimesHeadLineProviderService = new NyTimesHeadLineProviderService("url", "key", restTemplate);
  }

  @Test
  @SneakyThrows
  void testGetNewsHeadLines_Success() {
    // Arrange
    String keyword = "test";
    DocsItem docsItem = new DocsItem();
    docsItem.setJsonMemberAbstract("Test Abstract");
    docsItem.setWebUrl("https://example.com");
    docsItem.setLeadParagraph("Test Lead Paragraph");
    docsItem.setSource("Test Source");

    NyTimesNewsFeedResponse mockResponse = mock(NyTimesNewsFeedResponse.class);
    List<DocsItem> docsItems = List.of(docsItem);
    when(mockResponse.getResponse()).thenReturn(mock(Response.class));
    when(mockResponse.getResponse().getDocs()).thenReturn(docsItems);
    when(restTemplate.getForObject(anyString(), eq(NyTimesNewsFeedResponse.class)))
        .thenReturn(mockResponse);

    // Act
    List<HeadLine> headLines = nyTimesHeadLineProviderService.getNewsHeadLines(keyword);

    // Assert
    assertNotNull(headLines);
    assertEquals(1, headLines.size());
    assertEquals("Test Abstract", headLines.get(0).getTitle());
    assertEquals("https://example.com", headLines.get(0).getArticleLink());
    assertEquals("Test Lead Paragraph", headLines.get(0).getDescription());
    assertEquals("Test Source", headLines.get(0).getSource());
  }

  @Test
  @SneakyThrows
  void testGetNewsHeadLines_NoArticles() {
    // Arrange
    String keyword = "test";
    NyTimesNewsFeedResponse mockResponse = mock(NyTimesNewsFeedResponse.class);
    when(mockResponse.getResponse()).thenReturn(mock(Response.class));
    when(mockResponse.getResponse().getDocs()).thenReturn(List.of());

    when(restTemplate.getForObject(anyString(), eq(NyTimesNewsFeedResponse.class)))
        .thenReturn(mockResponse);

    // Act
    List<HeadLine> headLines = nyTimesHeadLineProviderService.getNewsHeadLines(keyword);

    // Assert
    assertTrue(headLines.isEmpty());
  }

  @Test
  void testGetNewsHeadLines_ApiFailure() {
    // Arrange
    String keyword = "test";
    when(restTemplate.getForObject(anyString(), eq(NyTimesNewsFeedResponse.class)))
        .thenThrow(new RuntimeException("API failure"));

    // Act & Assert
    NewsApiFailureException exception =
        assertThrows(
            NewsApiFailureException.class,
            () -> nyTimesHeadLineProviderService.getNewsHeadLines(keyword));
    assertEquals("API failure", exception.getMessage());
  }
}

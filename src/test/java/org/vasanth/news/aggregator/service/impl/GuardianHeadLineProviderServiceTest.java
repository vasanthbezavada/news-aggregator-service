package org.vasanth.news.aggregator.service.impl;

import org.vasanth.news.aggregator.dto.HeadLine;
import org.vasanth.news.aggregator.dto.guardianResponse.GuardianArticle;
import org.vasanth.news.aggregator.dto.guardianResponse.GuardianMetadata;
import org.vasanth.news.aggregator.dto.guardianResponse.GuardianNewsFeedResponse;
import org.vasanth.news.aggregator.exception.NewsApiFailureException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuardianHeadLineProviderServiceTest {

    @InjectMocks
    private GuardianHeadLineProviderService guardianHeadLineProviderService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${news.guardian-api-url}")
    private String guardianApiUrl;

    @Value("${news.guardian-api-key}")
    private String guardianApiKey;

    @BeforeEach
    void setUp() {
        guardianHeadLineProviderService = new GuardianHeadLineProviderService("url","key", restTemplate);
    }

    @Test
    @SneakyThrows
    void testGetNewsHeadLines_Success() {
        // Arrange
        String keyword = "test";
        GuardianArticle guardianArticle = new GuardianArticle();
        GuardianNewsFeedResponse mockResponse = mock(GuardianNewsFeedResponse.class);
        List<GuardianArticle> articles = List.of(guardianArticle);
        when(mockResponse.getResponse()).thenReturn(mock(GuardianMetadata.class));
        when(mockResponse.getResponse().getArticles()).thenReturn(articles);
        when(restTemplate.getForObject(anyString(), eq(GuardianNewsFeedResponse.class)))
                .thenReturn(mockResponse);

        // Act
        List<HeadLine> headLines = guardianHeadLineProviderService.getNewsHeadLines(keyword);

        // Assert
        assertNotNull(headLines);
        assertEquals(1, headLines.size());
    }

    @Test
    @SneakyThrows
    void testGetNewsHeadLines_NoArticles() {
        // Arrange
        String keyword = "test";
        GuardianNewsFeedResponse mockResponse = mock(GuardianNewsFeedResponse.class);
        when(mockResponse.getResponse()).thenReturn(mock(GuardianMetadata.class));
        when(mockResponse.getResponse().getArticles()).thenReturn(List.of());

        when(restTemplate.getForObject(anyString(), eq(GuardianNewsFeedResponse.class)))
                .thenReturn(mockResponse);

        // Act
        List<HeadLine> headLines = guardianHeadLineProviderService.getNewsHeadLines(keyword);

        // Assert
        assertTrue(headLines.isEmpty());
    }

    @Test
    void testGetNewsHeadLines_ApiFailure() {
        // Arrange
        String keyword = "test";
        when(restTemplate.getForObject(anyString(), eq(GuardianNewsFeedResponse.class)))
                .thenThrow(new RuntimeException("API failure"));

        // Act & Assert
        NewsApiFailureException exception = assertThrows(NewsApiFailureException.class, () ->
                guardianHeadLineProviderService.getNewsHeadLines(keyword)
        );
        assertEquals("API failure", exception.getMessage());
    }
}

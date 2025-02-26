package org.vasanth.news.aggregator.service.impl;

import org.vasanth.news.aggregator.dto.HeadLine;
import org.vasanth.news.aggregator.dto.nytimesResponse.DocsItem;
import org.vasanth.news.aggregator.dto.nytimesResponse.NyTimesNewsFeedResponse;
import org.vasanth.news.aggregator.exception.NewsApiFailureException;
import org.vasanth.news.aggregator.service.HeadLineProviderService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class NyTimesHeadLineProviderService implements HeadLineProviderService {

  public static final String THE_GUARDIAN_NEWS = "The Guardian News";
  @Autowired private final RestTemplate restTemplate;

  private final String nyTimesApiUrl;

  private final String nyTimesApiKey;

  public NyTimesHeadLineProviderService(
      @Value("${news.nytimes-api-url}") String nyTimesApiUrl,
      @Value("${news.nytimes-api-key}") String nyTimesApiKey,
      RestTemplate restTemplate) {
    this.nyTimesApiUrl = nyTimesApiUrl;
    this.nyTimesApiKey = nyTimesApiKey;
    this.restTemplate = restTemplate;
  }

  @Override
  public List<HeadLine> getNewsHeadLines(String keyword) throws NewsApiFailureException {
    try {
      String guardianUrl =
          String.format("%s?q=%s&api-key=%s", nyTimesApiUrl, keyword, nyTimesApiKey);
      var guardianResponse = restTemplate.getForObject(guardianUrl, NyTimesNewsFeedResponse.class);
      // Extract article headlines based on NyTimes API response formats
      List<DocsItem> nyArticles =
          guardianResponse != null ? guardianResponse.getResponse().getDocs() : null;

      if (Objects.nonNull(nyArticles)) {
        List<HeadLine> headLines =
            nyArticles.stream()
                .map(
                    docsItem ->
                        new HeadLine(
                            docsItem.getJsonMemberAbstract(),
                            docsItem.getWebUrl(),
                            docsItem.getLeadParagraph(),
                            docsItem.getSource()))
                .toList();
        return headLines;
      }
    } catch (Exception ex) {
      log.error("Failed while quering nyTimes newsfeed ", ex);
      throw new NewsApiFailureException(ex.getMessage());
    }
    return Collections.emptyList();
  }
}

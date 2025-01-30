package com.publicsapient.news.aggregator.service.impl;

import com.publicsapient.news.aggregator.dto.HeadLine;
import com.publicsapient.news.aggregator.dto.guardianResponse.GuardianArticle;
import com.publicsapient.news.aggregator.dto.guardianResponse.GuardianNewsFeedResponse;
import com.publicsapient.news.aggregator.exception.NewsApiFailureException;
import com.publicsapient.news.aggregator.service.HeadLineProviderService;
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
public class GuardianHeadLineProviderService implements HeadLineProviderService {

  public static final String THE_GUARDIAN_NEWS = "The Guardian News";

  private final RestTemplate restTemplate;

  private final String guardianApiUrl;

  private final String guardianApiKey;

  public GuardianHeadLineProviderService(
      @Value("${news.guardian-api-url}") String guardianApiUrl,
      @Value("${news.guardian-api-key}") String guardianApiKey, RestTemplate restTemplate) {
    this.guardianApiUrl = guardianApiUrl;
    this.guardianApiKey = guardianApiKey;
    this.restTemplate = restTemplate;
  }

  @Override
  public List<HeadLine> getNewsHeadLines(String keyword) throws NewsApiFailureException {
    try {
      String guardianUrl =
          String.format("%s?page-size=50&q=%s&api-key=%s", guardianApiUrl, keyword, guardianApiKey);
      var guardianResponse = restTemplate.getForObject(guardianUrl, GuardianNewsFeedResponse.class);
      // Extract article headlines based on Guardian API response formats
      List<GuardianArticle> guardianArticles =
          guardianResponse != null ? guardianResponse.getResponse().getArticles() : null;

      if (Objects.nonNull(guardianArticles)) {
        List<HeadLine> headLines =
            guardianArticles.stream()
                .map(
                    guardianArticle ->
                        new HeadLine(
                            guardianArticle.getWebTitle(),
                            guardianArticle.getWebUrl(),
                            guardianArticle.getWebTitle(),
                            THE_GUARDIAN_NEWS))
                .toList();
        return headLines;
      }
    } catch (Exception ex) {
      log.error("Failed while quering guardian newsfeed ", ex);
      throw new NewsApiFailureException(ex.getMessage());
    }
    return Collections.emptyList();
  }
}

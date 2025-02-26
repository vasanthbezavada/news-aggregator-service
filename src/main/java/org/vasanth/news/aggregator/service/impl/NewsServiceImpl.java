package org.vasanth.news.aggregator.service.impl;

import org.vasanth.news.aggregator.dto.HeadLine;
import org.vasanth.news.aggregator.dto.NewsResponse;
import org.vasanth.news.aggregator.exception.AllProvidersFailedException;
import org.vasanth.news.aggregator.exception.NewsApiFailureException;
import org.vasanth.news.aggregator.service.HeadLineProviderService;
import org.vasanth.news.aggregator.service.NewsService;
import java.util.*;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

  private final boolean offlineMode;

  private final List<HeadLineProviderService> headLineProviderServices;

  public NewsServiceImpl(
      @Value("${offline-mode}") boolean offlineMode,
      List<HeadLineProviderService> headLineProviderServices) {
    // using strategy design pattern
    // code is open for extension and closed for modification
    // we can easily add new provider in future without disturbing the core logic
    this.headLineProviderServices = headLineProviderServices;
    this.offlineMode = offlineMode;
  }

  public NewsResponse getNewsFeed(String keyword, int page, int limit)
      throws AllProvidersFailedException {
    if (offlineMode) {
      throw new AllProvidersFailedException("All News Providers were currently down");
    }

    List<HeadLine> uniqueHeadLines =
        headLineProviderServices.parallelStream()
            .flatMap(
                headLineProviderService -> {
                  try {
                    return headLineProviderService.getNewsHeadLines(keyword).stream();
                  } catch (NewsApiFailureException e) {
                    log.error(e.getMessage());
                  }
                  return Stream.empty();
                })
            .distinct() // removes duplicates. Overridden equals method in Headlines class
            .toList();

    if (uniqueHeadLines.isEmpty()) {
      // this means , all news provider apis were down or having some errors
      throw new AllProvidersFailedException("All News Providers were currently down");
    }

    int skip = (page - 1) * limit;

    return new NewsResponse(
        keyword, uniqueHeadLines.stream().skip(skip).limit(limit).toList(), uniqueHeadLines.size());
  }
}

package org.vasanth.news.aggregator.service;

import org.vasanth.news.aggregator.dto.NewsResponse;
import org.vasanth.news.aggregator.exception.AllProvidersFailedException;

public interface NewsService {
  NewsResponse getNewsFeed(String keyword, int page, int limit) throws AllProvidersFailedException;
}

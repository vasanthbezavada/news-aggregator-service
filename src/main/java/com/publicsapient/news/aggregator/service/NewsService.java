package com.publicsapient.news.aggregator.service;

import com.publicsapient.news.aggregator.dto.NewsResponse;
import com.publicsapient.news.aggregator.exception.AllProvidersFailedException;

public interface NewsService {
  NewsResponse getNewsFeed(String keyword, int page, int limit) throws AllProvidersFailedException;
}

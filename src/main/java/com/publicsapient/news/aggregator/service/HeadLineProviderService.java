package com.publicsapient.news.aggregator.service;

import com.publicsapient.news.aggregator.dto.HeadLine;
import com.publicsapient.news.aggregator.exception.NewsApiFailureException;
import java.util.List;

public interface HeadLineProviderService {

  List<HeadLine> getNewsHeadLines(String keyword) throws NewsApiFailureException;
}

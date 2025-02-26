package org.vasanth.news.aggregator.service;

import org.vasanth.news.aggregator.dto.HeadLine;
import org.vasanth.news.aggregator.exception.NewsApiFailureException;
import java.util.List;

public interface HeadLineProviderService {

  List<HeadLine> getNewsHeadLines(String keyword) throws NewsApiFailureException;
}

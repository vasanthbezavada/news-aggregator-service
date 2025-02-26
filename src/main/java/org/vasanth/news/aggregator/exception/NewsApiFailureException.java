package org.vasanth.news.aggregator.exception;

import java.io.Serial;

public class NewsApiFailureException extends Exception {
  @Serial private static final long serialVersionUID = 1L;

  public NewsApiFailureException(final String message) {
    super(message);
  }
}

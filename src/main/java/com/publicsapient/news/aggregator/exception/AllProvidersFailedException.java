package com.publicsapient.news.aggregator.exception;

import java.io.Serial;

public class AllProvidersFailedException extends Exception {
  @Serial private static final long serialVersionUID = 1L;

  public AllProvidersFailedException(final String message) {
    super(message);
  }
}

package org.vasanth.news.aggregator.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.vasanth.news.aggregator.dto.NewsResponse;
import org.vasanth.news.aggregator.exception.AllProvidersFailedException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@Slf4j
public class FallbackControllerAdvice {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @ExceptionHandler(AllProvidersFailedException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<NewsResponse> getLatestNews(AllProvidersFailedException e)
      throws IOException {
    log.error("Offline mode due to exception", e);

    ClassPathResource resource = new ClassPathResource("latest-news.json");
    byte[] jsonData = FileCopyUtils.copyToByteArray(resource.getInputStream());

    NewsResponse offlineResponse = objectMapper.readValue(jsonData, NewsResponse.class);
    return new ResponseEntity<>(offlineResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

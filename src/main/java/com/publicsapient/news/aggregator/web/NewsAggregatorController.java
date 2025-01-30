package com.publicsapient.news.aggregator.web;

import com.publicsapient.news.aggregator.dto.NewsResponse;
import com.publicsapient.news.aggregator.exception.AllProvidersFailedException;
import com.publicsapient.news.aggregator.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Controller for handling news search requests. */
@RestController
@RequestMapping("/news")
@CrossOrigin(origins = "*")
public class NewsAggregatorController {

  private final int defaultPage;

  private final int defaultLimit;

  private final NewsService newsService;

  public NewsAggregatorController(
      NewsService newsService,
      @Value("${pagination.default-page}") Integer defaultPage,
      @Value("${pagination.default-limit}") Integer defaultLimit) {
    this.newsService = newsService;
    this.defaultPage = defaultPage;
    this.defaultLimit = defaultLimit;
  }

  @GetMapping("/search")
  @Operation(
      summary = "Search news articles by keyword",
      description =
          "Fetch news articles from external providers such as The Guardian and NY Times, "
              + "aggregated without duplicates, using keyword search with pagination support.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NewsResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "500",
            description = "Server error while fetching news",
            content = @Content(mediaType = "application/json"))
      })
  public ResponseEntity<NewsResponse> search(
      @RequestParam String keyword,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer limit)
      throws AllProvidersFailedException {

    int pageNumber = page != null ? page : defaultPage;
    int resultLimit = limit != null ? limit : defaultLimit;

    NewsResponse newsResponse = newsService.getNewsFeed(keyword, pageNumber, resultLimit);
    return ResponseEntity.ok(newsResponse);
  }
}

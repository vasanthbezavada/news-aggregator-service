package org.vasanth.news.aggregator.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsResponse {
  private String keyword;
  private List<HeadLine> headlines;
  private int totalResults;

  public NewsResponse(String keyword, List<HeadLine> headlines, Integer totalResults) {
    this.keyword = keyword;
    this.headlines = headlines;
    this.totalResults = totalResults;
  }
}

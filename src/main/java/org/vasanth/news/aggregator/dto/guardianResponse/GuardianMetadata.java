package org.vasanth.news.aggregator.dto.guardianResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class GuardianMetadata {
  private String userTier;
  private int total;
  private int startIndex;
  private int pages;
  private int pageSize;
  private String orderBy;
  private int currentPage;

  @JsonProperty("results")
  private List<GuardianArticle> articles;

  private String status;
}

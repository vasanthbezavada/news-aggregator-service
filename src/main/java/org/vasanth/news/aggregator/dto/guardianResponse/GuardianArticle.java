package org.vasanth.news.aggregator.dto.guardianResponse;

import lombok.Data;

@Data
public class GuardianArticle {
  private String sectionName;
  private String pillarName;
  private String webPublicationDate;
  private String apiUrl;
  private String webUrl;
  private boolean isHosted;
  private String pillarId;
  private String webTitle;
  private String id;
  private String sectionId;
  private String type;
}

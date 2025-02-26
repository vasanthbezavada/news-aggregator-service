package org.vasanth.news.aggregator.dto;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeadLine {
  private String title;
  private String articleLink;
  private String description;
  private String source;

  /*
   * If two Headlines are said to be equal when their title matches
   * */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    HeadLine headLine = (HeadLine) obj;
    return Objects.equals(title, headLine.title);
  }
}

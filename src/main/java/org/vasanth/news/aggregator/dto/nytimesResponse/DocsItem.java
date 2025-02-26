package org.vasanth.news.aggregator.dto.nytimesResponse;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DocsItem{
	private String snippet;
//	private List<KeywordsItem> keywords;
	private String sectionName;
	@JsonProperty("abstract")
	private String jsonMemberAbstract;
	private String source;
	private String uri;
	private String newsDesk;
	private String pubDate;
//	private List<MultimediaItem> multimedia;
	private int wordCount;
	@JsonProperty("lead_paragraph")
	private String leadParagraph;
	private String typeOfMaterial;
	@JsonProperty("web_url")
	private String webUrl;
	private String id;
	private String subsectionName;
//	private Headline headline;
//	private Byline byline;
	private String documentType;
	private String printPage;
	private String printSection;
}
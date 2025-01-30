package com.publicsapient.news.aggregator.dto.nytimesResponse;

import java.util.List;
import lombok.Data;

@Data
public class Response{
	private List<DocsItem> docs;
	private Meta meta;
}
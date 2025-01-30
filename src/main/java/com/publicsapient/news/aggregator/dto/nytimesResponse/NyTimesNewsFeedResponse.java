package com.publicsapient.news.aggregator.dto.nytimesResponse;

import lombok.Data;

@Data
public class NyTimesNewsFeedResponse{
	private String copyright;
	private Response response;
	private String status;
}
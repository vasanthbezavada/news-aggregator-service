package com.publicsapient.news.aggregator.dto.nytimesResponse;

import lombok.Data;

@Data
public class Meta{
	private int hits;
	private int offset;
	private int time;
}
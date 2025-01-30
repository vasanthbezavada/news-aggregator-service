package com.publicsapient.news.aggregator;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicsapient.news.aggregator.dto.guardianResponse.GuardianNewsFeedResponse;
import com.publicsapient.news.aggregator.dto.nytimesResponse.NyTimesNewsFeedResponse;
import java.io.InputStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NewsAggregatorIntegrationTest {

  @Test
  void contextLoads() {}

  @MockitoBean RestTemplate restTemplate;

  @Autowired MockMvc mockMvc;

  @Test
  @SneakyThrows
  void test_e2eFlow() {
    String keyword = "tech";
    int page = 1;
    int limit = 10;

    when(restTemplate.getForObject(anyString(), eq(GuardianNewsFeedResponse.class)))
        .thenReturn(getGuardianMockObject());
    when(restTemplate.getForObject(anyString(), eq(NyTimesNewsFeedResponse.class)))
        .thenReturn(getNytMockObject());

    mockMvc
        .perform(
            get("/news/search")
                .param("keyword", keyword)
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(limit)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }























































  @Test
  @SneakyThrows

  void test_offlineFlow() {
    System.setProperty("offline-mode", "true");
    String keyword = "tech";

    mockMvc
        .perform(
            get("/news/search")
                .param("keyword", keyword))
        .andExpect(status().is5xxServerError())
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @SneakyThrows
  private GuardianNewsFeedResponse getGuardianMockObject() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    InputStream inputStream =
        getClass().getClassLoader().getResourceAsStream("guardian-sample-response.json");
    assert inputStream != null;
    return objectMapper.readValue(inputStream, GuardianNewsFeedResponse.class);
  }

  @SneakyThrows
  private NyTimesNewsFeedResponse getNytMockObject() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    InputStream inputStream =
        getClass().getClassLoader().getResourceAsStream("nyt-sample-response.json");
    assert inputStream != null;
    return objectMapper.readValue(inputStream, NyTimesNewsFeedResponse.class);
  }
}

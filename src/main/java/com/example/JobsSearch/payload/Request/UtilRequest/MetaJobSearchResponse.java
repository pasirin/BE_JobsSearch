package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

@Data
public class MetaJobSearchResponse {
  private Integer searchResult;
  private Integer allResult;

  public MetaJobSearchResponse(Integer searchResult, Integer allResult) {
    this.searchResult = searchResult;
    this.allResult = allResult;
  }

  public static MetaJobSearchResponse create(Integer searchResult, Integer allResult) {
    return new MetaJobSearchResponse(searchResult, allResult);
  }
}

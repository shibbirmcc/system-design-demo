package com.electrolux.demo.status.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class RestResponsePage<T> extends PageImpl<T> implements Serializable {

  private static final long serialVersionUID = 3248189030448292002L;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public RestResponsePage(@JsonProperty("content") List<T> content,
      @JsonProperty("number") int number,
      @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements,
      @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
      @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
      @JsonProperty("first") boolean first, @JsonProperty("empty") boolean empty,
      @JsonProperty("numberOfElements") int numberOfElements) {
    super(content, PageRequest.of(number, size), totalElements);
  }

}

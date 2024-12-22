package com.roomify.detection_be.utility.web.model.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldError {
  private String field;
  private String message;
}

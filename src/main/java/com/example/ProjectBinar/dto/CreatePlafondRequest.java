package com.example.ProjectBinar.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO untuk request pembuatan Plafond baru. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlafondRequest {

  private String name;
  private String description;
  private BigDecimal maxAmount;
  private BigDecimal interestRate;
  private Integer tenorMonth;
  private Boolean isActive;
}

package com.example.ProjectBinar.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO untuk request update Plafond. Semua field optional - hanya yang disertakan akan diupdate. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlafondRequest {

  private String name;
  private String description;
  private BigDecimal maxAmount;
  private BigDecimal interestRate;
  private Integer tenorMonth;
  private Boolean isActive;
}

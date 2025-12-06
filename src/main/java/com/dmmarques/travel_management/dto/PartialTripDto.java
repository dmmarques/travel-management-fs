package com.dmmarques.travel_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartialTripDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private List<String> preferenceList;
}

package com.dmmarques.travel_management.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class TravelCost {
    private BigDecimal fuel;
    private BigDecimal tollCost;
    private BigDecimal totalCost;
}

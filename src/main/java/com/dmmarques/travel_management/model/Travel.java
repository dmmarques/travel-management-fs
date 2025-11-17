package com.dmmarques.travel_management.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Travel {
    @Id
    private String id;
    private String name;
    @NotNull
    private String transport;
    @NotNull
    private String fromLat;
    @NotNull
    private String fromLng;
    @NotNull
    private LocalDateTime departureDate;
    @NotNull
    private String toLat;
    @NotNull
    private String toLng;
    @NotNull
    private LocalDateTime arrivalDate;
    @NotNull
    private String estimatedDuration;
    @NotNull
    private String distance;
    private String estimatedCost;
    private TravelCost genTravelCost;
}

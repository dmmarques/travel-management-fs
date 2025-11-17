package com.dmmarques.travel_management.model;

import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Accommodation {
    @Id
    @Generated
    private String id;
    private String name;
    @NotNull private String googlePlaceId;
    private String googleRating;
    private Integer googleReviewsNumber;
    private String address;
    private String internationalPhoneNumber;
    private String latitude;
    private String longitude;
    private boolean isAccessible;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BigDecimal priceForAdult;
    private BigDecimal priceForChild;
    private Boolean allowsPets;
    private BigDecimal priceForPet;
}

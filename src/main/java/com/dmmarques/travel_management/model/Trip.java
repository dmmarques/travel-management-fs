package com.dmmarques.travel_management.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Document
public record Trip(@Id String id,
                   @NotNull String name,
                   String description,
                   @CreatedBy String creatorUsername,
                   @CreatedDate LocalDateTime creationDate,
                   @NotNull LocalDate startDate,
                   @NotNull LocalDate endDate,
                   List<String> participantUsernames,
                   @NotNull
                   List<Accommodation> accommodations,
                   @NotNull
                   List<Activity> activityList,
                   @NotNull
                   List<Travel> travelList,
                   BigDecimal budget
    ) {
}

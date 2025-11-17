package com.dmmarques.travel_management.model;

import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
public record Activity(@Id @Generated String id,
                       @NotNull String name,
                       @NotNull String address,
                       @NotNull String category,
                       @CreatedBy String creatorUsername,
                       @CreatedDate LocalDateTime creationDate,
                       @NotNull LocalDateTime activityDate,
                       BigDecimal cost,
                       String description,
                       String latitude,
                       String longitude) {
}

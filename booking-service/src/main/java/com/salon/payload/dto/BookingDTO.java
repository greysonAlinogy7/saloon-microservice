package com.salon.payload.dto;

import com.salon.domain.BookingStatus;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {
    private  Long id;

    private  Long salonId;
    private Long customerId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Set<Long> serviceIds;

    private BookingStatus status;

    private  int totalPrice;
}

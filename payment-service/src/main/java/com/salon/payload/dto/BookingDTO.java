package com.salon.payload.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter

public class BookingDTO {
    private  Long id;

    private  Long salonId;
    private Long customerId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Set<Long> serviceIds;

    private  Long totalPrice;
}

package com.salon.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalonDTO {
    private  Long id;

    private  String name;

    private List<String> images;

    private  String address;

    private  String phoneNumber;
    private  String email;

    private  String city;
    private  Long ownerId;

    private LocalTime openTime;
    private LocalTime closeTime;
}

package com.saloon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private LocalDate openTime;

    private LocalDate closeTime;
}

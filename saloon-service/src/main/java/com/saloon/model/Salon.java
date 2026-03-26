package com.saloon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @Column(nullable = false)
    private  String name;

    @ElementCollection
    private List<String> images;

    @Column(nullable = false)
    private  String address;

    @Column(nullable = false)
    private  String phoneNumber;

    @Column(nullable = false)
    private  String email;

    @Column(nullable = false)
    private  String city;

    @Column(nullable = false)
    private  Long ownerId;

    @Column(nullable = false)
    private LocalDate openTime;

    @Column(nullable = false)
    private LocalDate closeTime;
}

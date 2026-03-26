package com.saloon.service.impl;

import com.saloon.model.Salon;
import com.saloon.payload.dto.SalonDTO;
import com.saloon.payload.dto.UserDTO;

import java.util.List;

public interface ISalonService {
    Salon createSalon(SalonDTO salonDTO, UserDTO user);
    Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception;
    List<Salon> getAllSalons();
    Salon getSalonById(Long salonId) throws Exception;
    Salon getSalonByOwnerId(Long ownerId);
    List<Salon> searchSalonByCity(String city);
}

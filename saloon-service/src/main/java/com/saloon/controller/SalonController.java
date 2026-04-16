package com.saloon.controller;

import com.saloon.mapper.SalonMapper;
import com.saloon.model.Salon;
import com.saloon.payload.dto.SalonDTO;
import com.saloon.payload.dto.UserDTO;
import com.saloon.service.SalonService;
import com.saloon.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SalonController {

    private  final SalonService salonService;
    private  final UserFeignClient userFeignClient;

    @PostMapping("/salons")
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        Salon salon = salonService.createSalon(salonDTO, userDTO);
        SalonDTO salonDTO1 = SalonMapper.toDTO(salon);
        return new ResponseEntity<>(salonDTO1, HttpStatus.CREATED);
    }

    @PutMapping("/salons/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(@PathVariable Long salonId ,@RequestBody SalonDTO salonDTO, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        Salon salon = salonService.updateSalon(salonDTO, userDTO, salonId);
        SalonDTO salonDTO1 = SalonMapper.toDTO(salon);
        return new ResponseEntity<>(salonDTO1, HttpStatus.CREATED);
    }

    @GetMapping("/salons")
    public ResponseEntity<List<SalonDTO>> getSalon() throws Exception {
        List<Salon> salon = salonService.getAllSalons();
        List<SalonDTO> salonDTOS=salon.stream().map((salons) -> {
            SalonDTO salonDTO = SalonMapper.toDTO(salons);
            return salonDTO;
        }).toList();

        return new ResponseEntity<>(salonDTOS, HttpStatus.OK);
    }

    @GetMapping("/salons/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable("salonId") Long salonId) throws Exception {
       Salon salon = salonService.getSalonById(salonId);
       SalonDTO salonDTO = SalonMapper.toDTO(salon);
       return  ResponseEntity.ok(salonDTO);
    }

    @GetMapping("/salons/search")
    public ResponseEntity<List<SalonDTO>> searchSalon(@RequestParam("city") String city) throws Exception {
        List<Salon> salon = salonService.searchSalonByCity(city);
        List<SalonDTO> salonDTO =salon.stream().map((salons) -> {
            SalonDTO salonDTO1 = SalonMapper.toDTO(salons);
            return salonDTO1;
        }).toList();
        return  ResponseEntity.ok(salonDTO);
    }

    @GetMapping("/salons/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId( @PathVariable Long ownerId, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        if (userDTO==null){
            throw  new Exception("user not found");
        }
        Salon salon = salonService.getSalonByOwnerId(ownerId);
        SalonDTO salonDTO = SalonMapper.toDTO(salon);
        return  ResponseEntity.ok(salonDTO);
    }




}

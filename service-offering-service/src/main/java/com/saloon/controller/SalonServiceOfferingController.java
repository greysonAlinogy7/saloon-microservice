package com.saloon.controller;

import com.saloon.entity.ServiceOffering;
import com.saloon.payload.CategoryDTO;
import com.saloon.payload.SalonDTO;
import com.saloon.payload.ServiceDTO;
import com.saloon.service.ServiceOfferingService;
import com.saloon.service.client.CategoryFeignClient;
import com.saloon.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {
    private  final ServiceOfferingService serviceOfferingService;
    private  final SalonFeignClient salonFeignClient;
    private  final CategoryFeignClient categoryFeignClient;


    @PostMapping()
    public ResponseEntity<ServiceOffering> createService(@RequestBody ServiceDTO serviceDTO, @RequestHeader("Authorization") String jwt) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        CategoryDTO categoryDTO = categoryFeignClient.getCategoryByIdAndSalon(serviceDTO.getCategory(), salonDTO.getId()).getBody();

        ServiceOffering serviceOfferings = serviceOfferingService.createService(salonDTO, serviceDTO, categoryDTO);
        return ResponseEntity.ok(serviceOfferings);
    }

    @PutMapping()
    public ResponseEntity<ServiceOffering> updateService(@PathVariable Long serviceId ,@RequestBody ServiceOffering serviceOffering) throws Exception {
        ServiceOffering serviceOfferings = serviceOfferingService.updateService(serviceId, serviceOffering);
        return ResponseEntity.ok(serviceOfferings);
    }
}

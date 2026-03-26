package com.saloon.service.impl;

import com.saloon.entity.ServiceOffering;
import com.saloon.payload.CategoryDTO;
import com.saloon.payload.SalonDTO;
import com.saloon.payload.ServiceDTO;

import java.util.Set;

public interface IServiceOfferingService {
    ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO);
    ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception;
    Set<ServiceOffering> getAllServiceBySalon(Long salonId, Long categoryId);
   Set<ServiceOffering> getServicesByIds(Set<Long> ids);
   ServiceOffering getServiceById(Long id) throws Exception;
}

package com.saloon.service;

import com.saloon.entity.ServiceOffering;
import com.saloon.payload.CategoryDTO;
import com.saloon.payload.SalonDTO;
import com.saloon.payload.ServiceDTO;
import com.saloon.repository.ServiceOfferingRepository;
import com.saloon.service.impl.IServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingService implements IServiceOfferingService {

    private  final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {
        ServiceOffering serviceOffering = new ServiceOffering();
        serviceOffering.setImage(serviceDTO.getImage());
        serviceOffering.setSalonId(salonDTO.getId());
        serviceOffering.setCategoryId(serviceDTO.getCategory());
        serviceOffering.setName(serviceDTO.getName());
        serviceOffering.setDescription(serviceDTO.getDescription());
        serviceOffering.setPrice(serviceDTO.getPrice());
        serviceOffering.setDuration(serviceDTO.getDuration());
        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceId).orElse(null);
        if (serviceOffering == null){
            throw  new Exception("service not exist");
        }
        serviceOffering.setImage(service.getImage());

        serviceOffering.setName(service.getName());
        serviceOffering.setDescription(service.getDescription());
        serviceOffering.setPrice(service.getPrice());
        serviceOffering.setDuration(service.getDuration());

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public Set<ServiceOffering> getAllServiceBySalon(Long salonId, Long categoryId) {
        Set<ServiceOffering> service = serviceOfferingRepository.findBySalonId(salonId);
        if (categoryId != null){service =service.stream()
                .filter((services) ->
                        services.getCategoryId() != null
                                && services.getCategoryId()==categoryId)
                .collect(Collectors.toSet());
        }
        return service;
    }

    @Override
    public Set<ServiceOffering> getServicesByIds(Set<Long> ids) {
       List<ServiceOffering> services = serviceOfferingRepository.findAllById(ids);
       return  new HashSet<>(services);
    }

    @Override
    public ServiceOffering getServiceById(Long id) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(id).orElse(null);
        if (serviceOffering==null){
            throw  new Exception("service not exist with is");
        }
        return serviceOffering;
    }
}

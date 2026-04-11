package com.salon.client;


import com.salon.payload.dto.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("SERVICE-OFFERING-SERVICE")
public interface ServiceFeignClient {


    @GetMapping("/api/service-offering/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long id) throws Exception;


}

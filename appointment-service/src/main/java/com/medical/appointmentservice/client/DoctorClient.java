package com.medical.appointmentservice.client;

import com.medical.appointmentservice.dto.external.DoctorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DOCTOR-SERVICE")
public interface DoctorClient {

    @GetMapping("/api/v1/doctors/{id}")
    DoctorDTO getDoctorById(@PathVariable("id") Long id);
}
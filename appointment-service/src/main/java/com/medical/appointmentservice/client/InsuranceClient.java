package com.medical.appointmentservice.client;

import com.medical.appointmentservice.dto.external.InsuranceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "INSURANCE-SERVICE")
public interface InsuranceClient {

    @GetMapping("/api/v1/insurance/check/{patientId}")
    InsuranceDTO checkInsurance(@PathVariable("patientId") Long patientId);
}
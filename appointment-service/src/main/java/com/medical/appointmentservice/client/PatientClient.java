package com.medical.appointmentservice.client;

import com.medical.appointmentservice.dto.external.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign Client gọi PATIENT-SERVICE qua Eureka.
 * - name = "PATIENT-SERVICE": tên đăng ký trong Eureka.
 * - Không cần viết @LoadBalanced — Feign tự tích hợp LoadBalancer.
 * - Không cần viết code gọi HTTP thủ công — Feign tự generate.
 */
@FeignClient(name = "PATIENT-SERVICE")
public interface PatientClient {

    @GetMapping("/api/v1/patients/{id}")
    PatientDTO getPatientById(@PathVariable("id") Long id);
}
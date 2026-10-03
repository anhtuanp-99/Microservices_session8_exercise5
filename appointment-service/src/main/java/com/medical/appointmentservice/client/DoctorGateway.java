package com.medical.appointmentservice.client;

import com.medical.appointmentservice.dto.external.DoctorDTO;
import com.medical.appointmentservice.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorGateway {

    private final DoctorClient doctorClient;

    @CircuitBreaker(name = "doctorServiceCB", fallbackMethod = "getDoctorFallback")
    public DoctorDTO getDoctor(Long doctorId) {
        log.info("Gọi Doctor-Service qua Feign, doctorId={}", doctorId);
        return doctorClient.getDoctorById(doctorId);
    }

    public DoctorDTO getDoctorFallback(Long doctorId, Exception e) {
        log.warn(">>> FALLBACK: Doctor-Service lỗi, doctorId={}, nguyên nhân={}",
                doctorId, e.getMessage());
        throw new ServiceUnavailableException(
                "Hiện tại không thể kiểm tra thông tin bác sĩ, vui lòng thử lại sau vài giây",
                true);
    }
}
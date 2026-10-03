package com.medical.appointmentservice.client;

import com.medical.appointmentservice.dto.external.PatientDTO;
import com.medical.appointmentservice.exception.ServiceUnavailableException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientGateway {

    private final PatientClient patientClient;

    @Retry(name = "patientRetry", fallbackMethod = "getPatientFallback")
    public PatientDTO getPatient(Long patientId) {
        log.info("Gọi Patient-Service, patientId={}", patientId);
        return patientClient.getPatientById(patientId);
    }

    /**
     * Fallback — gọi khi đã retry 3 lần mà vẫn lỗi.
     */
    public PatientDTO getPatientFallback(Long patientId, Exception e) {
        log.warn(">>> FALLBACK sau 3 lần retry: patientId={}, nguyên nhân={}",
                patientId, e.getMessage());
        throw new ServiceUnavailableException(
                "Hiện tại không thể kiểm tra thông tin bệnh nhân, vui lòng thử lại sau vài giây",
                true);
    }
}
package com.medical.appointmentservice.client;

import com.medical.appointmentservice.dto.external.InsuranceDTO;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceGateway {

    private final InsuranceClient insuranceClient;
    private final ExecutorService virtualThreadExecutor;

    /**
     * @TimeLimiter yêu cầu:
     *   1. Method phải trả về CompletableFuture<T>.
     *   2. Phải thực thi async qua ExecutorService.
     *   3. Caller phải gọi .join() hoặc .get() để timeout có tác dụng.
     */
    @TimeLimiter(name = "insuranceTimeout", fallbackMethod = "insuranceFallback")
    public CompletableFuture<InsuranceDTO> checkInsurance(Long patientId) {
        log.info("Bắt đầu kiểm tra bảo hiểm cho patientId={}", patientId);

        return CompletableFuture.supplyAsync(
                () -> insuranceClient.checkInsurance(patientId),
                virtualThreadExecutor
        );
    }

    /**
     * Fallback khi:
     *   1. Timeout 1s → TimeoutException
     *   2. Lỗi kết nối Insurance-Service
     * <p>
     * Trả về InsuranceDTO "chưa xác nhận" — cho phép luồng đặt lịch tiếp tục.
     */
    public CompletableFuture<InsuranceDTO> insuranceFallback(Long patientId, Throwable throwable) {
        log.warn(">>> FALLBACK Insurance: patientId={}, nguyên nhân={}",
                patientId, throwable.getClass().getSimpleName());

        String message;
        if (throwable instanceof TimeoutException) {
            message = "Không thể kiểm tra bảo hiểm trong thời gian cho phép. " +
                    "Bệnh nhân vui lòng thanh toán trực tiếp.";
        } else {
            message = "Dịch vụ bảo hiểm hiện không khả dụng. " +
                    "Bệnh nhân vui lòng thanh toán trực tiếp.";
        }

        return CompletableFuture.completedFuture(
                InsuranceDTO.builder()
                        .patientId(patientId)
                        .approved(false)
                        .message(message)
                        .insuranceCode(null)
                        .build()
        );
    }
}
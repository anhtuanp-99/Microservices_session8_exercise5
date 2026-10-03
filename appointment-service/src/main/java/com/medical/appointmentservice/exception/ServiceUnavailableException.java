package com.medical.appointmentservice.exception;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String serviceName) {
        super(String.format("%s hiện không khả dụng (Không tìm thấy instance nào)", serviceName));
    }

    public ServiceUnavailableException(String message, boolean isCustom) {
        super(message);
    }
}
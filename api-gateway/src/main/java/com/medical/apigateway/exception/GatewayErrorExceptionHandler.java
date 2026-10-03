package com.medical.apigateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.apigateway.dto.ApiResponseError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Component
@Order(-2)
@RequiredArgsConstructor
@Slf4j
public class GatewayErrorExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        String path = exchange.getRequest().getPath().value();
        log.error("Gateway error at [{}]: {} - {}",
                path, ex.getClass().getSimpleName(), ex.getMessage());

        HttpStatus status = determineStatus(ex);
        String message = determineMessage(ex, status);

        ApiResponseError error = ApiResponseError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(error);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    private HttpStatus determineStatus(Throwable ex) {
        if (ex instanceof NotFoundException) return HttpStatus.SERVICE_UNAVAILABLE;
        if (ex instanceof ConnectException || ex.getCause() instanceof ConnectException)
            return HttpStatus.SERVICE_UNAVAILABLE;
        if (ex instanceof TimeoutException || ex.getCause() instanceof TimeoutException)
            return HttpStatus.SERVICE_UNAVAILABLE;
        if (ex instanceof ResponseStatusException rse)
            return HttpStatus.valueOf(rse.getStatusCode().value());
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String determineMessage(Throwable ex, HttpStatus status) {
        if (status == HttpStatus.SERVICE_UNAVAILABLE)
            return "Cổng Gateway không thể kết nối tới dịch vụ đích";
        if (ex instanceof ResponseStatusException rse && rse.getReason() != null)
            return rse.getReason();
        if (status == HttpStatus.NOT_FOUND)
            return "Không tìm thấy endpoint yêu cầu";
        if (status == HttpStatus.INTERNAL_SERVER_ERROR)
            return "Lỗi hệ thống: " + ex.getMessage();
        return status.getReasonPhrase();
    }
}
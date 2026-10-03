package com.medical.doctorservice.controller;

import com.medical.doctorservice.dto.request.DoctorRequestDTO;
import com.medical.doctorservice.dto.response.DoctorResponseDTO;
import com.medical.doctorservice.dto.response.DoctorSummaryDTO;
import com.medical.doctorservice.service.DoctorService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(
            @Valid @RequestBody DoctorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.createDoctor(request));
    }

    @GetMapping
    public ResponseEntity<List<DoctorSummaryDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    /**
     * API search — được bảo vệ bởi RateLimiter.
     * Tối đa 5 request / 10 giây cho mỗi client.
     * Vượt → ném RequestNotPermitted → GlobalExceptionHandler bắt → 429.
     */
    @GetMapping("/search")
    @RateLimiter(name = "searchDoctorLimit")
    public ResponseEntity<List<DoctorSummaryDTO>> searchDoctors(
            @RequestParam(name = "keyword") String keyword) {
        log.info("Search doctors với keyword={}", keyword);
        return ResponseEntity.ok(doctorService.searchDoctors(keyword));
    }
}
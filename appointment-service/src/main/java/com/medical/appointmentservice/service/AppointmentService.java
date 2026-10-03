package com.medical.appointmentservice.service;

import com.medical.appointmentservice.client.DoctorGateway;
import com.medical.appointmentservice.client.InsuranceGateway;
import com.medical.appointmentservice.client.PatientGateway;
import com.medical.appointmentservice.dto.external.DoctorDTO;
import com.medical.appointmentservice.dto.external.InsuranceDTO;
import com.medical.appointmentservice.dto.external.PatientDTO;
import com.medical.appointmentservice.dto.request.AppointmentRequestDTO;
import com.medical.appointmentservice.dto.response.AppointmentResponseDTO;
import com.medical.appointmentservice.entity.Appointment;
import com.medical.appointmentservice.entity.AppointmentStatus;
import com.medical.appointmentservice.exception.ResourceNotFoundException;
import com.medical.appointmentservice.mapper.AppointmentMapper;
import com.medical.appointmentservice.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientGateway patientGateway;
    private final DoctorGateway doctorGateway;
    private final InsuranceGateway insuranceGateway;

    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {
        // BƯỚC 1: Patient (có @Retry)
        PatientDTO patient = patientGateway.getPatient(request.getPatientId());
        log.info("Đã xác thực patient id={}", patient.getId());

        // BƯỚC 2: Doctor (có @CircuitBreaker)
        DoctorDTO doctor = doctorGateway.getDoctor(request.getDoctorId());
        log.info("Đã xác thực doctor id={}", doctor.getId());

        // BƯỚC 3: Kiểm tra status bác sĩ
        if (Boolean.FALSE.equals(doctor.getStatus())) {
            throw new IllegalStateException(
                    "Bác sĩ " + doctor.getName() + " hiện đang nghỉ phép!");
        }

        // BƯỚC 4: Kiểm tra trùng lịch
        boolean isDuplicate = appointmentRepository
                .existsByDoctorIdAndAppointmentDate(
                        request.getDoctorId(),
                        request.getAppointmentDate());
        if (isDuplicate) {
            throw new IllegalStateException(
                    "Bác sĩ đã có lịch hẹn vào thời điểm " + request.getAppointmentDate());
        }

        // BƯỚC 5: Kiểm tra bảo hiểm (có @TimeLimiter)
        // QUAN TRỌNG: phải .join() để TimeLimiter kích hoạt timeout.
        InsuranceDTO insurance = insuranceGateway
                .checkInsurance(request.getPatientId())
                .join();

        log.info("Kết quả bảo hiểm: approved={}, message={}",
                insurance.getApproved(), insurance.getMessage());

        // BƯỚC 6: Lưu Appointment
        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .reason(request.getReason())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Đã tạo appointment id={}", saved.getId());

        return appointmentMapper.toResponseDTO(saved);
    }

    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "ID", id));
        return appointmentMapper.toResponseDTO(appointment);
    }

    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentMapper.toResponseDTOList(appointmentRepository.findAll());
    }
}
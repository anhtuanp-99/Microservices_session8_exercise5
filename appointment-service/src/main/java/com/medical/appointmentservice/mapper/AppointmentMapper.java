package com.medical.appointmentservice.mapper;

import com.medical.appointmentservice.dto.response.AppointmentResponseDTO;
import com.medical.appointmentservice.entity.Appointment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentMapper {

    public AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        if (appointment == null) return null;
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .reason(appointment.getReason())
                .status(appointment.getStatus() != null ? appointment.getStatus().name() : null)
                .build();
    }

    public List<AppointmentResponseDTO> toResponseDTOList(List<Appointment> appointments) {
        if (appointments == null) return List.of();
        return appointments.stream().map(this::toResponseDTO).toList();
    }
}
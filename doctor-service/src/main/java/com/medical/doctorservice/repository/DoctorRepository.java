package com.medical.doctorservice.repository;

import com.medical.doctorservice.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByEmail(String email);

    List<Doctor> findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(
            String name, String specialization);
}
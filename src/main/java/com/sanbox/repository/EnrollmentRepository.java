package com.sanbox.repository;

import com.sanbox.model.Enrollment;
import com.sanbox.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    boolean exitsByEmployeeIdAndStatus(Long employeeId, EnrollmentStatus status);
}

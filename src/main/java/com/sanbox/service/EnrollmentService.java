package com.sanbox.service;

import com.sanbox.model.Enrollment;

public interface EnrollmentService {
    Enrollment enroll(Long employeeId, Long planId);
    Enrollment cancelledEnroll(Long enrollmentId);
}

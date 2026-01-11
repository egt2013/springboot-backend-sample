package com.sanbox.service.impl;

import com.sanbox.model.*;
import com.sanbox.repository.CompanyRepository;
import com.sanbox.repository.EmployeeRepository;
import com.sanbox.repository.EnrollmentRepository;
import com.sanbox.repository.PlanRepository;
import com.sanbox.service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private EmployeeRepository employeeRepository;
    private CompanyRepository companyRepository;
    private PlanRepository planRepository;
    private EnrollmentRepository enrollmentRepository;
    @Override
    public Enrollment enroll(Long employeeId, Long planId) {
        //1. Load employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new IllegalArgumentException("Employee not found."));

        //2. load company
        Company company = companyRepository.findById(employee.getCompanyId())
                .orElseThrow(()->new IllegalArgumentException("Company not found."));

        //3. Business unit rule#1
        if(company.getEmployeeCount() < 250){
            throw new IllegalArgumentException("Company is not eligible for enrollment.");
        }

        //4. loop plan
        Plan plan = planRepository.findById(planId)
                .orElseThrow(()->new IllegalArgumentException("Plan not found."));

        //5. Business rule#2
        if(!plan.getActive()){
            throw new IllegalArgumentException("Plan is not active.");
        }

        //6. Business rule#3
        boolean hasActiveEnrollment = enrollmentRepository.exitsByEmployeeIdAndStatus(employeeId,EnrollmentStatus.ACTIVE);
        if(hasActiveEnrollment){
            throw new IllegalArgumentException("Employee already active in enrollment.");
        }

        Enrollment createEnrollment = Enrollment.builder()
                .employeeId(employeeId)
                .planId(planId)
                .status(EnrollmentStatus.ACTIVE)
                .startDate(LocalDate.now()).build();



        return enrollmentRepository.save(createEnrollment);
    }

    @Override
    public Enrollment cancelledEnroll(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->new IllegalArgumentException("Enrollment is not found."));

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollment.setEndDate(LocalDate.now());
        return enrollmentRepository.save(enrollment);
    }
}

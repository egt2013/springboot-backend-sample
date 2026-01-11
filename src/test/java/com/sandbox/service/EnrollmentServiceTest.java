package com.sandbox.service;

import com.sanbox.model.*;
import com.sanbox.repository.CompanyRepository;
import com.sanbox.repository.EmployeeRepository;
import com.sanbox.repository.EnrollmentRepository;
import com.sanbox.repository.PlanRepository;
import com.sanbox.service.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;
    //Business Rule1:
    //Company mush have employeeCount >=250 to enroll
    @Test
    void shouldEnrollSuccessfully_whenCompanyEmployeeCountAtLeast250() {
        var employeeId = 1L;
        var planId = 10L;
        var companyId = 5L;

        // Given
        Plan plan1 = Plan.builder()
                .id(planId)
                .planCode("P01")
                .name("PLAN_1")
                .monthlyPremium(1)
                .build();

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Porntip")
                .lastName("Mock_lastname")
                .email("mockmail@gmail.com")
                .companyId(companyId)
                .build();

        Company company = Company.builder().id(companyId)
                .name("big company").employeeCount(254).build();

        Enrollment enrollment = Enrollment.builder()
                .id(1L)
                .employeeId(employeeId)
                .planId(planId)
                .status(EnrollmentStatus.ACTIVE)
                .startDate(LocalDate.now())
                .build();

        when(planRepository.findById(planId)).thenReturn(Optional.of(plan1));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(enrollmentRepository.exitsByEmployeeIdAndStatus(employeeId, EnrollmentStatus.ACTIVE)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        // When
        Enrollment result = enrollmentService.enroll(employeeId, planId);
        // Then
        assertNotNull(result);
        assertEquals(planId,result.getPlanId());
        assertEquals(employeeId,result.getEmployeeId());
        assertEquals(EnrollmentStatus.ACTIVE,result.getStatus());


    }

    
    @Test
    void shouldThrowException_whenCompanyEmployeeLessThen250() {
        // Given
        var employeeId = 1L;
        var planId = 10L;
        var companyId = 5L;

        // Given

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Porntip")
                .lastName("Mock_lastname")
                .email("mockmail@gmail.com")
                .companyId(companyId)
                .build();

        Company company = Company.builder().id(companyId)
                .name("big company").employeeCount(100).build();

        Enrollment enrollment = Enrollment.builder()
                .id(1L)
                .employeeId(employeeId)
                .planId(planId)
                .status(EnrollmentStatus.ACTIVE)
                .startDate(LocalDate.now())
                .build();


        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        // When + Then
         IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    enrollmentService.enroll(employeeId,planId);
                }
        );

        assertEquals("Company is not eligible for enrollment.", ex.getMessage());
    }

    @Test
    void shouldThrowException_whenPlanIsNotActive() {
        // Given
        var employeeId = 1L;
        var planId = 10L;
        var companyId = 5L;

        // Given
        Plan plan1 = Plan.builder()
                .id(planId)
                .planCode("P01")
                .name("PLAN_1")
                .monthlyPremium(1)
                .active(false)
                .build();

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Porntip")
                .lastName("Mock_lastname")
                .email("mockmail@gmail.com")
                .companyId(companyId)
                .build();

        Company company = Company.builder().id(companyId)
                .name("big company").employeeCount(254).build();


        when(planRepository.findById(planId)).thenReturn(Optional.of(plan1));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        
    
        // When + Then
         IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    enrollmentService.enroll(employeeId,planId);
                }
        );
    
        assertEquals("Plan is not active.", ex.getMessage());
    }

    @Test
    void shouldThrowException_whenHasActiveEnrollment() {
        // Given
        var employeeId = 1L;
        var planId = 10L;
        var companyId = 5L;

        // Given
        Plan plan1 = Plan.builder()
                .id(planId)
                .planCode("P01")
                .name("PLAN_1")
                .monthlyPremium(1)
                .build();

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Porntip")
                .lastName("Mock_lastname")
                .email("mockmail@gmail.com")
                .companyId(companyId)
                .build();

        Company company = Company.builder().id(companyId)
                .name("big company").employeeCount(254).build();



        when(planRepository.findById(planId)).thenReturn(Optional.of(plan1));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(enrollmentRepository.exitsByEmployeeIdAndStatus(employeeId, EnrollmentStatus.ACTIVE)).thenReturn(true);

        // When + Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    enrollmentService.enroll(employeeId,planId);
                }
        );

        assertEquals("Employee already active in enrollment.", ex.getMessage());
    }
    

    //cancelled enrollment change status to CANCELLED and stamp endDate
    @Test
    void shouldCancelledEnrollmentAndStampEndDate_whenEnrollmentIsCancelled() {
        // Given
        Long enrollmentId = 1L;
        Enrollment activeEnrollment = Enrollment.builder()
                .id(enrollmentId)
                .employeeId(1L)
                .planId(1L)
                .status(EnrollmentStatus.ACTIVE)
                .startDate(LocalDate.now().minusDays(5))
                .build();


        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(activeEnrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        // When
        Enrollment result = enrollmentService.cancelledEnroll(enrollmentId);
        // Then
        assertEquals(EnrollmentStatus.CANCELLED,result.getStatus());
        assertNotNull(result.getEndDate());
        assertEquals(LocalDate.now() , result.getEndDate());
    }

    @Test
    void shouldThrowException_whenEnrollmentIsNotExists() {
        // Given
        Long enrollmentId = 1L;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        // When + Then
         IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    enrollmentService.cancelledEnroll(enrollmentId);
                }
        );

        assertEquals("Enrollment is not found.", ex.getMessage());
    }




}

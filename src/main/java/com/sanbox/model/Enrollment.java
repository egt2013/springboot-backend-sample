package com.sanbox.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name="employee_id",nullable = false)
    private Long employeeId;

    @NotNull
    @Column(name = "plan_id",nullable = false)
    private Long planId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private EnrollmentStatus status;

    @NotNull
    @Column(name = "start_date",nullable = false)
    private LocalDate startDate;


    @Column(name = "end_date")
    private LocalDate endDate;


}
